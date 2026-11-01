package br.com.fiap.MoovUp.dto;

import br.com.fiap.MoovUp.enums.Estacao;
import br.com.fiap.MoovUp.enums.Linha;
import br.com.fiap.MoovUp.enums.Modalidade;

import java.time.LocalDateTime;

public record ViagemResponse(
        String id,
        String idUsuario,
        String idTicket,
        Modalidade modalidade,
        Linha linha,
        Estacao estacaoEmbarque,
        LocalDateTime dataViagem,
        int pontosGerados,
        double co2Evitado
) {
}
