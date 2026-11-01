package br.com.fiap.MoovUp.mapper;

import br.com.fiap.MoovUp.dto.PostagemRequest;
import br.com.fiap.MoovUp.dto.PostagemResponse;
import br.com.fiap.MoovUp.enums.TipoPostagem;
import br.com.fiap.MoovUp.models.Postagem;

import java.time.LocalDateTime;

public class PostagemMapper {

    // O ID e o tipo (já convertido para enum) vêm do service
    public static Postagem toEntity(PostagemRequest request, String id, TipoPostagem tipo) {
        return new Postagem(
                id,
                request.titulo().trim(),
                request.idUsuario(),
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
                postagem.getIdUsuario(),
                postagem.getNomeUsuario(),
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
