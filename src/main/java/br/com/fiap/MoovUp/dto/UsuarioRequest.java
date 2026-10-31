package br.com.fiap.MoovUp.dto;

// Corpo do POST /usuarios (cadastro). As validações ficam no UsuarioService.
public record UsuarioRequest(String nome, String email, String senha) {
}
