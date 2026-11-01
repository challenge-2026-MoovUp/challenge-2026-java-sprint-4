package br.com.fiap.MoovUp.controller;

import br.com.fiap.MoovUp.dto.TicketRequest;
import br.com.fiap.MoovUp.exception.TratadorDeErros;
import br.com.fiap.MoovUp.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Gestão do estoque (admin)
@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public ResponseEntity<?> abastecer(@RequestBody TicketRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(ticketService.abastecer(request));
        } catch (RuntimeException e) {
            return TratadorDeErros.tratarErro(e);
        }
    }

    // @RequestParam lê os filtros da URL: /tickets?status=DISPONIVEL&modalidade=METRO
    // required = false: sem o filtro na URL, o valor chega como null
    @GetMapping
    public ResponseEntity<?> listar(@RequestParam(required = false) String status,
                                    @RequestParam(required = false) String modalidade) {
        try {
            return ResponseEntity.ok(ticketService.listar(status, modalidade));
        } catch (RuntimeException e) {
            return TratadorDeErros.tratarErro(e);
        }
    }

    @GetMapping("/estoque")
    public ResponseEntity<?> consultarEstoque() {
        try {
            return ResponseEntity.ok(ticketService.consultarEstoque());
        } catch (RuntimeException e) {
            return TratadorDeErros.tratarErro(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable String id) {
        try {
            return ResponseEntity.ok(ticketService.buscarPorId(id));
        } catch (RuntimeException e) {
            return TratadorDeErros.tratarErro(e);
        }
    }

    @PatchMapping("/{id}/uso")
    public ResponseEntity<?> marcarUso(@PathVariable String id) {
        try {
            return ResponseEntity.ok(ticketService.marcarUso(id));
        } catch (RuntimeException e) {
            return TratadorDeErros.tratarErro(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable String id) {
        try {
            ticketService.excluir(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return TratadorDeErros.tratarErro(e);
        }
    }
}
