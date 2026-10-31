package br.com.fiap.MoovUp.dto;

// Uma linha do GET /usuarios/ranking
public record RankingResponse(int posicao, String id, String nome, int saldoPontos) {
}
