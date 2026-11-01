package br.com.fiap.MoovUp.mapper;

import br.com.fiap.MoovUp.dto.ViagemResponse;
import br.com.fiap.MoovUp.enums.Modalidade;
import br.com.fiap.MoovUp.models.Viagem;

public class ViagemMapper {

    // modalidade e CO₂ não são colunas da VIAGEM: saem da linha (enum) e da regra da Modalidade
    public static ViagemResponse toDTO(Viagem viagem) {
        Modalidade modalidade = viagem.getLinha().getModalidade();
        return new ViagemResponse(
                viagem.getId(),
                viagem.getIdUsuario(),
                viagem.getIdTicket(),
                modalidade,
                viagem.getLinha(),
                viagem.getEstacaoEmbarque(),
                viagem.getDataViagem(),
                viagem.getPontosGerados(),
                modalidade.getKm() * modalidade.getCo2PorKm()
        );
    }
}
