package br.com.fiap.MoovUp.mapper;

import br.com.fiap.MoovUp.dto.ViagemResponse;
import br.com.fiap.MoovUp.enums.Modalidade;
import br.com.fiap.MoovUp.models.Viagem;

public class ViagemMapper {

    public static ViagemResponse toDTO(Viagem viagem) {
        Modalidade modalidade = viagem.getLinha().getModalidade();
        return new ViagemResponse(
                viagem.getId(),
                viagem.getUsuario().getId(),
                viagem.getTicket().getId(),
                modalidade,
                viagem.getLinha(),
                viagem.getEstacaoEmbarque(),
                viagem.getDataViagem(),
                viagem.getPontosGerados(),
                modalidade.getKm() * modalidade.getCo2PorKm()
        );
    }
}
