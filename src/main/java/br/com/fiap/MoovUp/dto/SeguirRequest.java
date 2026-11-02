package br.com.fiap.MoovUp.dto;

// Corpo do POST /usuarios/{id}/seguindo: quem o usuário {id} quer seguir.
// Mandar UM dos dois: o idSeguido ou o nomeSeguido.
public record SeguirRequest(String idSeguido, String nomeSeguido) {
}
