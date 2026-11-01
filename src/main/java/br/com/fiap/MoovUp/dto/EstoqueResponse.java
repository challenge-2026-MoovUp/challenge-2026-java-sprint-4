package br.com.fiap.MoovUp.dto;

// GET /tickets/estoque: quantidade de tickets em cada status
public record EstoqueResponse(int disponiveis, int reservados, int utilizados, int expirados) {
}
