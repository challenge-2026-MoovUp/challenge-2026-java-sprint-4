package br.com.fiap.MoovUp.controller;

import br.com.fiap.MoovUp.dto.ViagemRequest;
import br.com.fiap.MoovUp.exception.TratadorDeErros;
import br.com.fiap.MoovUp.service.ViagemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Sem @RequestMapping na classe: os caminhos são /usuarios/{id}/viagens e /linhas
@RestController
public class ViagemController {

    private final ViagemService viagemService;

    public ViagemController(ViagemService viagemService) {
        this.viagemService = viagemService;
    }

    @PostMapping("/usuarios/{id}/viagens")
    public ResponseEntity<?> registrar(@PathVariable String id, @RequestBody ViagemRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(viagemService.registrar(id, request));
        } catch (RuntimeException e) {
            return TratadorDeErros.tratarErro(e);
        }
    }

    @GetMapping("/usuarios/{id}/viagens")
    public ResponseEntity<?> listar(@PathVariable String id) {
        try {
            return ResponseEntity.ok(viagemService.listar(id));
        } catch (RuntimeException e) {
            return TratadorDeErros.tratarErro(e);
        }
    }

    // /linhas?modalidade=METRO (o filtro é opcional)
    @GetMapping("/linhas")
    public ResponseEntity<?> listarLinhas(@RequestParam(required = false) String modalidade) {
        try {
            return ResponseEntity.ok(viagemService.listarLinhas(modalidade));
        } catch (RuntimeException e) {
            return TratadorDeErros.tratarErro(e);
        }
    }

    @GetMapping("/linhas/{codigo}/estacoes")
    public ResponseEntity<?> listarEstacoes(@PathVariable String codigo) {
        try {
            return ResponseEntity.ok(viagemService.listarEstacoes(codigo));
        } catch (RuntimeException e) {
            return TratadorDeErros.tratarErro(e);
        }
    }
}
