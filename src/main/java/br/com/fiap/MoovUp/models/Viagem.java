package br.com.fiap.MoovUp.models;

import br.com.fiap.MoovUp.enums.Estacao;
import br.com.fiap.MoovUp.enums.Linha;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

// Uma viagem feita com um ticket resgatado (máximo de 2 por ticket, regra do ViagemService)
@Getter
@Setter
public class Viagem {
    private String id;
    private String idUsuario;
    private String idTicket;
    private Linha linha;
    private Estacao estacaoEmbarque;
    private int pontosGerados;
    private LocalDateTime dataViagem;

    public Viagem(String id, String idUsuario, String idTicket, Linha linha, Estacao estacaoEmbarque, int pontosGerados, LocalDateTime dataViagem) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idTicket = idTicket;
        this.linha = linha;
        this.estacaoEmbarque = estacaoEmbarque;
        this.pontosGerados = pontosGerados;
        this.dataViagem = dataViagem;
    }
}
