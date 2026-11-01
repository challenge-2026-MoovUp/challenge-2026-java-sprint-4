package br.com.fiap.MoovUp.controller;

import br.com.fiap.MoovUp.dto.CurtidaRequest;
import br.com.fiap.MoovUp.dto.PostagemAlteracaoRequest;
import br.com.fiap.MoovUp.dto.PostagemRequest;
import br.com.fiap.MoovUp.exception.TratadorDeErros;
import br.com.fiap.MoovUp.service.PostagemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/postagens")
public class PostagemController {

    private final PostagemService postagemService;

    public PostagemController(PostagemService postagemService) {
        this.postagemService = postagemService;
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody PostagemRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(postagemService.criar(request));
        } catch (RuntimeException e) {
            return TratadorDeErros.tratarErro(e);
        }
    }

    @GetMapping
    public ResponseEntity<?> listar() {
        try {
            return ResponseEntity.ok(postagemService.listar());
        } catch (RuntimeException e) {
            return TratadorDeErros.tratarErro(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable String id) {
        try {
            return ResponseEntity.ok(postagemService.buscarPorId(id));
        } catch (RuntimeException e) {
            return TratadorDeErros.tratarErro(e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> alterar(@PathVariable String id, @RequestBody PostagemAlteracaoRequest request) {
        try {
            return ResponseEntity.ok(postagemService.alterar(id, request));
        } catch (RuntimeException e) {
            return TratadorDeErros.tratarErro(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable String id) {
        try {
            postagemService.excluir(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return TratadorDeErros.tratarErro(e);
        }
    }

    @PostMapping("/{id}/curtidas")
    public ResponseEntity<?> curtir(@PathVariable String id, @RequestBody CurtidaRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(postagemService.curtir(id, request));
        } catch (RuntimeException e) {
            return TratadorDeErros.tratarErro(e);
        }
    }
}
