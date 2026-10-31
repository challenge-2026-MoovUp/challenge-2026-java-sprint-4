package br.com.fiap.MoovUp.dto;

// GET /usuarios/{id}/pontos
public record PontosResponse(String idUsuario, int saldoPontos) {
}
