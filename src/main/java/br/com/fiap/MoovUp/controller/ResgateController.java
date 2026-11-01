package br.com.fiap.MoovUp.controller;

import br.com.fiap.MoovUp.dto.ResgateRequest;
import br.com.fiap.MoovUp.exception.TratadorDeErros;
import br.com.fiap.MoovUp.service.ResgateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios/{id}/resgates")
public class ResgateController {

    private final ResgateService resgateService;

    public ResgateController(ResgateService resgateService) {
        this.resgateService = resgateService;
    }

    @PostMapping
    public ResponseEntity<?> resgatar(@PathVariable String id, @RequestBody ResgateRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(resgateService.resgatar(id, request));
        } catch (RuntimeException e) {
            return TratadorDeErros.tratarErro(e);
        }
    }

    @GetMapping
    public ResponseEntity<?> listar(@PathVariable String id) {
        try {
            return ResponseEntity.ok(resgateService.listar(id));
        } catch (RuntimeException e) {
            return TratadorDeErros.tratarErro(e);
        }
    }
}
