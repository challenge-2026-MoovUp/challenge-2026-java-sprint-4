package br.com.fiap.MoovUp.dto;

import br.com.fiap.MoovUp.enums.StatusUsuario;

import java.time.LocalDate;

// Usuário devolvido pela API. A senha nunca volta no JSON.
public record UsuarioResponse(
        String id,
        String nome,
        String email,
        int saldoPontos,
        StatusUsuario status,
        LocalDate dataCadastro
) {
}
