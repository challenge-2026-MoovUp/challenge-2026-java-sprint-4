package br.com.fiap.MoovUp.dto;

// Corpo do POST /postagens. O tipoPostagem chega como texto e o service converte para o enum.
public record PostagemRequest(String idUsuario, String titulo, String conteudo, String imagem, String video, String tipoPostagem) {
}
