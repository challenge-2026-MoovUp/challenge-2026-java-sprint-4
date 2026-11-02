package br.com.fiap.MoovUp.mapper;

import br.com.fiap.MoovUp.dto.PostagemRequest;
import br.com.fiap.MoovUp.dto.PostagemResponse;
import br.com.fiap.MoovUp.enums.TipoPostagem;
import br.com.fiap.MoovUp.models.Postagem;
import br.com.fiap.MoovUp.models.Usuario;

import java.time.LocalDateTime;

public class PostagemMapper {

    // O ID, o tipo (já convertido para enum) e o autor (já buscado no banco) vêm do service
    public static Postagem toEntity(PostagemRequest request, String id, TipoPostagem tipo, Usuario autor) {
        return new Postagem(
                id,
                request.titulo().trim(),
                autor,
                request.conteudo().trim(),
                request.imagem(),
                request.video(),
                tipo,
                0,
                LocalDateTime.now().withNano(0)
        );
    }

    // pontosGerados não é coluna: sai do enum TipoPostagem
    public static PostagemResponse toDTO(Postagem postagem) {
        return new PostagemResponse(
                postagem.getId(),
                postagem.getUsuario().getId(),
                postagem.getUsuario().getNome(),
                postagem.getTitulo(),
                postagem.getConteudo(),
                postagem.getImagem(),
                postagem.getVideo(),
                postagem.getTipoPostagem(),
                postagem.getCurtidas(),
                postagem.getDataPostagem(),
                postagem.getTipoPostagem().getPontos()
        );
    }
}
