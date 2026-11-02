package br.com.fiap.MoovUp.controller;

import br.com.fiap.MoovUp.dto.SeguirRequest;
import br.com.fiap.MoovUp.exception.TratadorDeErros;
import br.com.fiap.MoovUp.service.SeguidorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios/{id}/seguindo")
public class SeguidorController {

    private final SeguidorService seguidorService;

    public SeguidorController(SeguidorService seguidorService) {
        this.seguidorService = seguidorService;
    }

    @GetMapping
    public ResponseEntity<?> listar(@PathVariable String id) {
        try {
            return ResponseEntity.ok(seguidorService.listarSeguidos(id));
        } catch (RuntimeException e) {
            return TratadorDeErros.tratarErro(e);
        }
    }

    @PostMapping
    public ResponseEntity<?> seguir(@PathVariable String id, @RequestBody SeguirRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(seguidorService.seguir(id, request));
        } catch (RuntimeException e) {
            return TratadorDeErros.tratarErro(e);
        }
    }

    @DeleteMapping("/{idSeguido}")
    public ResponseEntity<?> deixarDeSeguir(@PathVariable String id, @PathVariable String idSeguido) {
        try {
            seguidorService.deixarDeSeguir(id, idSeguido);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return TratadorDeErros.tratarErro(e);
        }
    }
}
