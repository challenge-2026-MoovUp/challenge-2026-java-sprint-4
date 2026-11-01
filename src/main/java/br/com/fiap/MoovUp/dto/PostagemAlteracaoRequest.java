package br.com.fiap.MoovUp.dto;

// Corpo do PUT /postagens/{id}: só título e conteúdo (o tipo define os pontos e não muda)
public record PostagemAlteracaoRequest(String titulo, String conteudo) {
}
