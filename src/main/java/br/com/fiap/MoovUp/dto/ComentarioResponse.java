package br.com.fiap.MoovUp.dto;

import java.time.LocalDateTime;

public record ComentarioResponse(
        String id,
        String idPostagem,
        String idUsuario,
        String nomeUsuario,
        String conteudo,
        int curtidas,
        LocalDateTime dataComentario
) {
}
