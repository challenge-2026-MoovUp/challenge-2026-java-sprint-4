package br.com.fiap.MoovUp.mapper;

import br.com.fiap.MoovUp.dto.SeguidoResponse;
import br.com.fiap.MoovUp.models.Seguidor;

public class SeguidorMapper {

    // O front só precisa de quem é seguido: o id e o nome dele
    public static SeguidoResponse toDTO(Seguidor seguidor) {
        return new SeguidoResponse(seguidor.getIdSeguido(), seguidor.getNomeSeguido());
    }
}
