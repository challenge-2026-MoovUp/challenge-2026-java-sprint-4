package br.com.fiap.MoovUp.dto;

// Corpo do POST /postagens/{id}/comentarios
public record ComentarioRequest(String idUsuario, String conteudo) {
}
