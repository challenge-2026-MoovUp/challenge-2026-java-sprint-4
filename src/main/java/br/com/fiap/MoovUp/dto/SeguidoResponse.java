package br.com.fiap.MoovUp.dto;

// Um usuário que é seguido: resposta do GET (lista) e do POST /usuarios/{id}/seguindo
public record SeguidoResponse(String id, String nome) {
}
