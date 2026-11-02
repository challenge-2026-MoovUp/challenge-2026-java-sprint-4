package br.com.fiap.MoovUp.models;

import br.com.fiap.MoovUp.enums.Estacao;
import br.com.fiap.MoovUp.enums.Linha;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Viagem {
    private String id;
    private Usuario usuario;
    private TicketDigital ticket;
    private Linha linha;
    private Estacao estacaoEmbarque;
    private int pontosGerados;
    private LocalDateTime dataViagem;

    public Viagem() {
    }

    public Viagem(String id, Usuario usuario, TicketDigital ticket, Linha linha, Estacao estacaoEmbarque, int pontosGerados, LocalDateTime dataViagem) {
        this.id = id;
        this.usuario = usuario;
        this.ticket = ticket;
        this.linha = linha;
        this.estacaoEmbarque = estacaoEmbarque;
        this.pontosGerados = pontosGerados;
        this.dataViagem = dataViagem;
    }
}
