package br.com.fiap.MoovUp.dto;

// Corpo do POST /tickets (abastece o estoque). Integer para saber se o campo veio vazio (null).
public record TicketRequest(String modalidade, Integer quantidade) {
}
