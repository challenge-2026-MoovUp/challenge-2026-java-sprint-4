package br.com.fiap.MoovUp.dto;

import br.com.fiap.MoovUp.enums.Modalidade;

import java.time.LocalDateTime;

// Uma linha do GET /usuarios/{id}/impacto/historico
public record ImpactoResponse(
        String id,
        String idTicket,
        Modalidade modalidade,
        double co2Evitado,
        double kmPercorridos,
        LocalDateTime dataCalculo
) {
}
