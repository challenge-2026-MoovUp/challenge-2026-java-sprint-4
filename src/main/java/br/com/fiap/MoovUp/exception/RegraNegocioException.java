package br.com.fiap.MoovUp.exception;

// Lançada pelos services quando uma regra de negócio é violada
// (saldo insuficiente, ticket expirado, sem estoque...). Vira 422 na API.
public class RegraNegocioException extends RuntimeException {
    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
