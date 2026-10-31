package br.com.fiap.MoovUp.controller;

import br.com.fiap.MoovUp.dto.*;
import br.com.fiap.MoovUp.exception.CredenciaisInvalidasException;
import br.com.fiap.MoovUp.exception.RecursoNaoEncontradoException;
import br.com.fiap.MoovUp.exception.RegraNegocioException;
import br.com.fiap.MoovUp.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    // O Spring entrega o service pronto (injeção pelo construtor)
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody UsuarioRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.cadastrar(request));
        } catch (RuntimeException e) {
            return tratarErro(e);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            return ResponseEntity.ok(usuarioService.login(request));
        } catch (RuntimeException e) {
            return tratarErro(e);
        }
    }

    @GetMapping
    public ResponseEntity<?> listar() {
        try {
            return ResponseEntity.ok(usuarioService.listar());
        } catch (RuntimeException e) {
            return tratarErro(e);
        }
    }

    // Caminho fixo: o Spring escolhe ele antes do /{id}, então "ranking" não é lido como ID
    @GetMapping("/ranking")
    public ResponseEntity<?> ranking() {
        try {
            return ResponseEntity.ok(usuarioService.ranking());
        } catch (RuntimeException e) {
            return tratarErro(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable String id) {
        try {
            return ResponseEntity.ok(usuarioService.buscarPorId(id));
        } catch (RuntimeException e) {
            return tratarErro(e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> alterar(@PathVariable String id, @RequestBody UsuarioAlteracaoRequest request) {
        try {
            return ResponseEntity.ok(usuarioService.alterar(id, request));
        } catch (RuntimeException e) {
            return tratarErro(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable String id) {
        try {
            usuarioService.excluir(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return tratarErro(e);
        }
    }

    @GetMapping("/{id}/pontos")
    public ResponseEntity<?> consultarPontos(@PathVariable String id) {
        try {
            return ResponseEntity.ok(usuarioService.consultarPontos(id));
        } catch (RuntimeException e) {
            return tratarErro(e);
        }
    }

    @GetMapping("/{id}/impacto")
    public ResponseEntity<?> consultarImpacto(@PathVariable String id) {
        try {
            return ResponseEntity.ok(usuarioService.consultarImpacto(id));
        } catch (RuntimeException e) {
            return tratarErro(e);
        }
    }

    // Escolhe o status HTTP pelo tipo da exceção que o service lançou
    private ResponseEntity<ErroResponse> tratarErro(RuntimeException e) {
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
