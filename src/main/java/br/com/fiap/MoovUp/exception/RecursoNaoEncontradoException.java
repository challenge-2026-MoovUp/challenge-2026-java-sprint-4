package br.com.fiap.MoovUp.exception;

// Lançada pelos services quando o ID não existe no banco. Vira 404 na API.
public class RecursoNaoEncontradoException extends RuntimeException {
    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
