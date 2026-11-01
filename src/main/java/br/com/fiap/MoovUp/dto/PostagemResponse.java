package br.com.fiap.MoovUp.dto;

import br.com.fiap.MoovUp.enums.TipoPostagem;

import java.time.LocalDateTime;

public record PostagemResponse(
        String id,
        String idUsuario,
        String nomeUsuario,
        String titulo,
        String conteudo,
        String imagem,
        String video,
        TipoPostagem tipoPostagem,
        int curtidas,
        LocalDateTime dataPostagem,
        int pontosGerados
) {
}
