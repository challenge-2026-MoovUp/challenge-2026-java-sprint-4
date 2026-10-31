package br.com.fiap.MoovUp.dto;

// Corpo do POST /usuarios/login
public record LoginRequest(String email, String senha) {
}
