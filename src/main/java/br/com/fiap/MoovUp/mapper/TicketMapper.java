package br.com.fiap.MoovUp.mapper;

import br.com.fiap.MoovUp.dto.TicketResponse;
import br.com.fiap.MoovUp.models.TicketDigital;

public class TicketMapper {

    public static TicketResponse toDTO(TicketDigital ticket) {
        return new TicketResponse(
                ticket.getId(),
                ticket.getModalidade(),
                ticket.getStatus(),
                ticket.getTarifaReais(),
                ticket.getCustoEmPontos(),
                ticket.getFormaRecebimento(),
                ticket.getDataExpiracao(),
                ticket.getQrCodeHash()
        );
    }
}
