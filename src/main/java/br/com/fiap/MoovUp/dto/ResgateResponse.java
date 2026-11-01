package br.com.fiap.MoovUp.dto;

// O ticket resgatado e o saldo que sobrou
public record ResgateResponse(TicketResponse ticket, int saldoRestante) {
}
