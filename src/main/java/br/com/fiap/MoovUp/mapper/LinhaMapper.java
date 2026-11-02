package br.com.fiap.MoovUp.mapper;

import br.com.fiap.MoovUp.dto.EstacaoResponse;
import br.com.fiap.MoovUp.dto.LinhaResponse;
import br.com.fiap.MoovUp.enums.Estacao;
import br.com.fiap.MoovUp.enums.Linha;

public class LinhaMapper {

    public static LinhaResponse toDTO(Linha linha) {
        return new LinhaResponse(linha.name(), linha.getNome(), linha.getModalidade());
    }

    public static EstacaoResponse toDTO(Estacao estacao) {
        return new EstacaoResponse(estacao.name(), estacao.getNome());
    }
}
