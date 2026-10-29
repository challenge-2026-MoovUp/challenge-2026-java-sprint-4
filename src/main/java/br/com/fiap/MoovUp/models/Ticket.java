package br.com.fiap.MoovUp.models;

import br.com.fiap.MoovUp.enums.StatusTicket;
import br.com.fiap.MoovUp.interfaces.Resgatavel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

// As validações (status, validade, saldo, dono, limite de viagens) ficam nos services.
// Aqui ficam só as ações que mudam o estado do ticket.
@Getter
@Setter
public abstract class Ticket implements Resgatavel {
    protected String id;
    protected String idUsuario; // dono do ticket; null enquanto está no estoque (DISPONIVEL)
    protected double tarifaReais;
    protected int custoEmPontos;
    protected StatusTicket status;
    protected LocalDate dataExpiracao;

    public String gerarQRCode(){
        return "QR-" + this.id + "-" + System.currentTimeMillis();
    }

    public void confirmarUsoTicket() {
        this.status = StatusTicket.UTILIZADO;
    }

    public abstract int calcularCustoEmPontos();
}
