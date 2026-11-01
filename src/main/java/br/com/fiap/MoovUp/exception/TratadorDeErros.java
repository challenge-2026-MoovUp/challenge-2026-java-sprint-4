package br.com.fiap.MoovUp.exception;

import br.com.fiap.MoovUp.dto.ErroResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

// Usado no catch de todos os controllers: escolhe o status HTTP pelo tipo da exceção
public class TratadorDeErros {

    public static ResponseEntity<ErroResponse> tratarErro(RuntimeException e) {
        HttpStatus status;

        if (e instanceof IllegalArgumentException) {
            status = HttpStatus.BAD_REQUEST;             // 400: dado inválido no JSON
        } else if (e instanceof CredenciaisInvalidasException) {
            status = HttpStatus.UNAUTHORIZED;            // 401: login errado
        } else if (e instanceof RecursoNaoEncontradoException) {
            status = HttpStatus.NOT_FOUND;               // 404: ID não existe
        } else if (e instanceof RegraNegocioException) {
            status = HttpStatus.UNPROCESSABLE_CONTENT;   // 422: regra de negócio
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;   // 500: erro inesperado (ex.: banco)
        }

        return ResponseEntity.status(status).body(new ErroResponse(status.value(), e.getMessage()));
    }
}
