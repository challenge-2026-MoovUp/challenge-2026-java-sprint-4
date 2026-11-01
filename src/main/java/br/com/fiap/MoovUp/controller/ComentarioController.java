package br.com.fiap.MoovUp.controller;

import br.com.fiap.MoovUp.dto.ComentarioRequest;
import br.com.fiap.MoovUp.exception.TratadorDeErros;
import br.com.fiap.MoovUp.service.ComentarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Sem @RequestMapping na classe: os caminhos são /postagens/{id}/comentarios e /comentarios/{id}
@RestController
public class ComentarioController {

    private final ComentarioService comentarioService;

    public ComentarioController(ComentarioService comentarioService) {
        this.comentarioService = comentarioService;
    }

    @PostMapping("/postagens/{id}/comentarios")
    public ResponseEntity<?> criar(@PathVariable String id, @RequestBody ComentarioRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(comentarioService.criar(id, request));
        } catch (RuntimeException e) {
            return TratadorDeErros.tratarErro(e);
        }
    }

    @GetMapping("/postagens/{id}/comentarios")
    public ResponseEntity<?> listarPorPostagem(@PathVariable String id) {
        try {
            return ResponseEntity.ok(comentarioService.listarPorPostagem(id));
        } catch (RuntimeException e) {
            return TratadorDeErros.tratarErro(e);
        }
    }

    @DeleteMapping("/comentarios/{id}")
    public ResponseEntity<?> excluir(@PathVariable String id) {
        try {
            comentarioService.excluir(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return TratadorDeErros.tratarErro(e);
        }
    }
}
