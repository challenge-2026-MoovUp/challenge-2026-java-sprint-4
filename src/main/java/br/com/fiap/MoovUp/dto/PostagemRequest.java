package br.com.fiap.MoovUp.dto;

public record PostagemRequest(String idUsuario, String titulo, String conteudo, String imagem, String video, String tipoPostagem) {
}
