package br.com.fiap.MoovUp.dto;

// Resposta da curtida: o total atualizado da postagem
public record CurtidaResponse(String idPostagem, int curtidas) {
}
