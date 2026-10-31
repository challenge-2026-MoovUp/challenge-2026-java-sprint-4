package br.com.fiap.MoovUp.dto;

import java.time.LocalDateTime;

// Formato de erro de todos os endpoints: { status, mensagem, timestamp }
public record ErroResponse(int status, String mensagem, LocalDateTime timestamp) {

    public ErroResponse(int status, String mensagem) {
        this(status, mensagem, LocalDateTime.now().withNano(0));
    }
}
