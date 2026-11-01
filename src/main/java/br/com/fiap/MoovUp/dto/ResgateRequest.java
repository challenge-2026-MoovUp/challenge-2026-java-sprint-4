package br.com.fiap.MoovUp.dto;

// Corpo do POST /usuarios/{id}/resgates
public record ResgateRequest(String modalidade, String formaRecebimento) {
}
