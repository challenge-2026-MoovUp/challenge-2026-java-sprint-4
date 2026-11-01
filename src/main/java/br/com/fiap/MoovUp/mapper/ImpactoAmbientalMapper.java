package br.com.fiap.MoovUp.mapper;

import br.com.fiap.MoovUp.dto.ImpactoResponse;
import br.com.fiap.MoovUp.models.ImpactoAmbiental;

public class ImpactoAmbientalMapper {

    public static ImpactoResponse toDTO(ImpactoAmbiental impacto) {
        return new ImpactoResponse(
                impacto.getId(),
                impacto.getIdTicket(),
                impacto.getModalidade(),
                impacto.getCo2Evitado(),
                impacto.getKmPercorridos(),
                impacto.getDataCalculo()
        );
    }
}
