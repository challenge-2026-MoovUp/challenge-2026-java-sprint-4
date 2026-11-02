package br.com.fiap.MoovUp.exception;

import br.com.fiap.MoovUp.dto.ErroResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class TratadorDeErros {

    public static ResponseEntity<ErroResponse> tratarErro(RuntimeException e) {
        HttpStatus status;

        if (e instanceof IllegalArgumentException) {
            status = HttpStatus.BAD_REQUEST;
        } else if (e instanceof CredenciaisInvalidasException) {
            status = HttpStatus.UNAUTHORIZED;
        } else if (e instanceof RecursoNaoEncontradoException) {
            status = HttpStatus.NOT_FOUND;
        } else if (e instanceof RegraNegocioException) {
            status = HttpStatus.UNPROCESSABLE_CONTENT;
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return ResponseEntity.status(status).body(new ErroResponse(status.value(), e.getMessage()));
    }
}
