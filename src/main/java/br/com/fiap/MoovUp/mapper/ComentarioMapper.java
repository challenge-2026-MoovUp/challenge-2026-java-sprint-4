package br.com.fiap.MoovUp.mapper;

import br.com.fiap.MoovUp.dto.ComentarioRequest;
import br.com.fiap.MoovUp.dto.ComentarioResponse;
import br.com.fiap.MoovUp.models.Comentario;
import br.com.fiap.MoovUp.models.Postagem;
import br.com.fiap.MoovUp.models.Usuario;

import java.time.LocalDateTime;

public class ComentarioMapper {

    // A postagem (da URL) e o autor (do JSON) já foram buscados no banco pelo service
    public static Comentario toEntity(ComentarioRequest request, String id, Postagem postagem, Usuario autor) {
        return new Comentario(id, postagem, autor, request.conteudo().trim(), 0, LocalDateTime.now().withNano(0));
    }

    public static ComentarioResponse toDTO(Comentario comentario) {
        return new ComentarioResponse(
                comentario.getId(),
                comentario.getPostagem().getId(),
                comentario.getUsuario().getId(),
                comentario.getUsuario().getNome(),
                comentario.getConteudo(),
                comentario.getCurtidas(),
                comentario.getDataComentario()
        );
    }
}
