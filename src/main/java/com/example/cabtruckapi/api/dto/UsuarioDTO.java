package com.example.cabtruckapi.api.dto;

import com.example.cabtruckapi.model.entity.Usuario;
import lombok.Data;

@Data
public class UsuarioDTO {
    private Integer id;
    private String login;
    private String senha;
    private String senhaRepeticao;
    private boolean admin;

    public static UsuarioDTO create(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setLogin(usuario.getLogin());
        dto.setAdmin(usuario.isAdmin());
        return dto;
    }
}
