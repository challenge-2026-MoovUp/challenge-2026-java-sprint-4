package br.com.fiap.MoovUp.dto;

import br.com.fiap.MoovUp.enums.FormaRecebimento;
import br.com.fiap.MoovUp.enums.Modalidade;
import br.com.fiap.MoovUp.enums.StatusTicket;

import java.time.LocalDate;

public record TicketResponse(
        String id,
        Modalidade modalidade,
        StatusTicket status,
        double tarifaReais,
        int custoEmPontos,
        FormaRecebimento formaRecebimento,
        LocalDate dataExpiracao,
        String qrCodeHash
) {
}
