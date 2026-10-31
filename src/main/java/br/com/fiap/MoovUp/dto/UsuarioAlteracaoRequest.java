package br.com.fiap.MoovUp.dto;

// Corpo do PUT /usuarios/{id}: só nome e email (a senha não muda por aqui)
public record UsuarioAlteracaoRequest(String nome, String email) {
}
