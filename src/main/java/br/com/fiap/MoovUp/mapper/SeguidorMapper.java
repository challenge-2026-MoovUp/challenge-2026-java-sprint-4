package br.com.fiap.MoovUp.mapper;

import br.com.fiap.MoovUp.dto.SeguidoResponse;
import br.com.fiap.MoovUp.models.Seguidor;

public class SeguidorMapper {

    public static SeguidoResponse toDTO(Seguidor seguidor) {
        return new SeguidoResponse(seguidor.getSeguido().getId(), seguidor.getSeguido().getNome());
    }
}
