package br.com.fiap.MoovUp.mapper;

import br.com.fiap.MoovUp.dto.UsuarioRequest;
import br.com.fiap.MoovUp.dto.UsuarioResponse;
import br.com.fiap.MoovUp.models.Usuario;

// Converte entre o JSON (DTO) e o model. O ID é gerado no service.
public class UsuarioMapper {

    public static Usuario toEntity(UsuarioRequest request, String id) {
        return new Usuario(id, request.nome().trim(), request.email().trim(), request.senha(), 0);
    }

    public static UsuarioResponse toDTO(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getSaldoPontos(),
                usuario.getStatus(),
                usuario.getDataCadastro()
        );
    }
}
