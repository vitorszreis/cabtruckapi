package com.example.cabtruckapi.api.controller;

import com.example.cabtruckapi.api.dto.CredenciaisDTO;
import com.example.cabtruckapi.api.dto.TokenDTO;
import com.example.cabtruckapi.api.dto.UsuarioDTO;
import com.example.cabtruckapi.api.exception.RegraNegocioException;
import com.example.cabtruckapi.api.exception.SenhaInvalidaException;
import com.example.cabtruckapi.model.entity.Usuario;
import com.example.cabtruckapi.security.JwtService;
import com.example.cabtruckapi.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name = "Usuários", description = "Gerenciamento de usuários e autenticação")
public class UsuarioController {

    private final UsuarioService service;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Operation(summary = "Listar todos os usuários")
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> get() {
        return ResponseEntity.ok(service.getUsuarios().stream().map(UsuarioDTO::create).collect(Collectors.toList()));
    }

    @Operation(summary = "Buscar usuário por ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Integer id) {
        Optional<Usuario> usuario = service.getUsuarioById(id);
        if (usuario.isEmpty()) {
            return new ResponseEntity<>("Usuário não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(UsuarioDTO.create(usuario.get()));
    }

    @Operation(summary = "Cadastrar novo usuário")
    @PostMapping
    public ResponseEntity<?> post(@RequestBody UsuarioDTO dto) {
        try {
            if (dto.getSenha() == null || dto.getSenha().isBlank() ||
                    dto.getSenhaRepeticao() == null || dto.getSenhaRepeticao().isBlank()) {
                return ResponseEntity.badRequest().body("Senha inválida");
            }
            if (!dto.getSenha().equals(dto.getSenhaRepeticao())) {
                return ResponseEntity.badRequest().body("Senhas não conferem");
            }
            Usuario usuario = Usuario.builder()
                    .login(dto.getLogin())
                    .senha(passwordEncoder.encode(dto.getSenha()))
                    .admin(dto.isAdmin())
                    .build();
            return new ResponseEntity<>(UsuarioDTO.create(service.salvar(usuario)), HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Autenticar usuário e obter token JWT")
    @PostMapping("/auth")
    public TokenDTO autenticar(@RequestBody CredenciaisDTO credenciais) {
        try {
            Usuario usuario = Usuario.builder()
                    .login(credenciais.getLogin())
                    .senha(credenciais.getSenha())
                    .build();
            service.autenticar(usuario);
            String token = jwtService.gerarToken(usuario);
            return new TokenDTO(usuario.getLogin(), token);
        } catch (UsernameNotFoundException | SenhaInvalidaException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @Operation(summary = "Excluir usuário")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Integer id) {
        Optional<Usuario> usuario = service.getUsuarioById(id);
        if (usuario.isEmpty()) {
            return new ResponseEntity<>("Usuário não encontrado", HttpStatus.NOT_FOUND);
        }
        service.excluir(usuario.get());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
