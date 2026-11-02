package br.com.fiap.MoovUp.dto;

import br.com.fiap.MoovUp.enums.Modalidade;

public record LinhaResponse(String codigo, String nome, Modalidade modalidade) {
}
