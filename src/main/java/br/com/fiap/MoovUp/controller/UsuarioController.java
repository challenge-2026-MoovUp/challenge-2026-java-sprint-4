package br.com.fiap.MoovUp.controller;

import br.com.fiap.MoovUp.dto.*;
import br.com.fiap.MoovUp.exception.TratadorDeErros;
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
            return TratadorDeErros.tratarErro(e);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            return ResponseEntity.ok(usuarioService.login(request));
        } catch (RuntimeException e) {
            return TratadorDeErros.tratarErro(e);
        }
    }

    @GetMapping
    public ResponseEntity<?> listar() {
        try {
            return ResponseEntity.ok(usuarioService.listar());
        } catch (RuntimeException e) {
            return TratadorDeErros.tratarErro(e);
        }
    }

    // Caminho fixo: o Spring escolhe ele antes do /{id}, então "ranking" não é lido como ID
    @GetMapping("/ranking")
    public ResponseEntity<?> ranking() {
        try {
            return ResponseEntity.ok(usuarioService.ranking());
        } catch (RuntimeException e) {
            return TratadorDeErros.tratarErro(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable String id) {
        try {
            return ResponseEntity.ok(usuarioService.buscarPorId(id));
        } catch (RuntimeException e) {
            return TratadorDeErros.tratarErro(e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> alterar(@PathVariable String id, @RequestBody UsuarioAlteracaoRequest request) {
        try {
            return ResponseEntity.ok(usuarioService.alterar(id, request));
        } catch (RuntimeException e) {
            return TratadorDeErros.tratarErro(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable String id) {
        try {
            usuarioService.excluir(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return TratadorDeErros.tratarErro(e);
        }
    }

    @GetMapping("/{id}/pontos")
    public ResponseEntity<?> consultarPontos(@PathVariable String id) {
        try {
            return ResponseEntity.ok(usuarioService.consultarPontos(id));
        } catch (RuntimeException e) {
            return TratadorDeErros.tratarErro(e);
        }
    }

    @GetMapping("/{id}/impacto")
    public ResponseEntity<?> consultarImpacto(@PathVariable String id) {
        try {
            return ResponseEntity.ok(usuarioService.consultarImpacto(id));
        } catch (RuntimeException e) {
            return TratadorDeErros.tratarErro(e);
        }
    }

    @GetMapping("/{id}/impacto/historico")
    public ResponseEntity<?> consultarHistoricoImpacto(@PathVariable String id) {
        try {
            return ResponseEntity.ok(usuarioService.consultarHistoricoImpacto(id));
        } catch (RuntimeException e) {
            return TratadorDeErros.tratarErro(e);
        }
    }
}
