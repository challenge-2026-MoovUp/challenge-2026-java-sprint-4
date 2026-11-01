package br.com.fiap.MoovUp.dto;

// Corpo do POST /usuarios/{id}/viagens. linha e estacaoEmbarque são os códigos dos enums (ex.: "AZUL", "JABAQUARA").
public record ViagemRequest(String idTicket, String linha, String estacaoEmbarque) {
}
