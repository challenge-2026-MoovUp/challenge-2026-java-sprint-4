package br.com.fiap.MoovUp.dto;

// Corpo do POST /usuarios/{id}/seguindo: quem o usuário {id} quer seguir
public record SeguirRequest(String idSeguido) {
}
