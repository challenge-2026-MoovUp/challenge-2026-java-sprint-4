package br.com.fiap.MoovUp.dto;

public record EstoqueResponse(int disponiveis, int reservados, int utilizados, int expirados) {
}
