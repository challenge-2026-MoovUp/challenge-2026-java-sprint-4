package br.com.fiap.MoovUp.mapper;

import br.com.fiap.MoovUp.dto.ComentarioRequest;
import br.com.fiap.MoovUp.dto.ComentarioResponse;
import br.com.fiap.MoovUp.models.Comentario;

import java.time.LocalDateTime;

public class ComentarioMapper {

    // O idPostagem vem da URL (/postagens/{id}/comentarios), não do JSON
    public static Comentario toEntity(ComentarioRequest request, String id, String idPostagem) {
        return new Comentario(id, idPostagem, request.idUsuario(), request.conteudo().trim(), 0, LocalDateTime.now().withNano(0));
    }

    public static ComentarioResponse toDTO(Comentario comentario) {
        return new ComentarioResponse(
                comentario.getId(),
                comentario.getIdPostagem(),
                comentario.getIdUsuario(),
                comentario.getNomeUsuario(),
                comentario.getConteudo(),
                comentario.getCurtidas(),
                comentario.getDataComentario()
        );
    }
}
