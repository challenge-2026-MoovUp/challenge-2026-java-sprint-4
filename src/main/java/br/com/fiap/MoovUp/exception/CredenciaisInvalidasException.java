package br.com.fiap.MoovUp.exception;

// Lançada no login quando o email ou a senha estão errados. Vira 401 na API.
public class CredenciaisInvalidasException extends RuntimeException {
    public CredenciaisInvalidasException(String mensagem) {
        super(mensagem);
    }
}
