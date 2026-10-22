package br.com.fiap.MoovUp.models;

import br.com.fiap.MoovUp.enums.StatusTicket;
import br.com.fiap.MoovUp.interfaces.Resgatavel;

import java.time.LocalDate;
import java.util.Objects;

public abstract class Ticket implements Resgatavel {
    protected String id;
    protected double tarifaReais;
    protected int custoEmPontos;
    protected StatusTicket status;
    protected LocalDate dataExpiracao;

    public String gerarQRCode(){
        return "QR-" + this.id + "-" + System.currentTimeMillis();
    }

    public boolean validar() {
        return this.status == StatusTicket.DISPONIVEL && this.dataExpiracao != null && LocalDate.now().isBefore(this.dataExpiracao);
    }

    public boolean confirmarUsoTicket(String idTicket, Usuario usuario) {
        if (usuario == null) {
            System.out.println("Usuário não informado.");
            return false;
        }

        if (idTicket == null || idTicket.trim().isEmpty()) {
            System.out.println("ID do ticket inválido.");
            return false;
        }

        if (!Objects.equals(this.id, idTicket.trim())) {
            System.out.println("Ticket informado não corresponde ao ticket atual.");
            return false;
        }

        if (!this.validar()) {
            System.out.println("Ticket inválido ou expirado.");
            return false;
        }

        if (this.status == StatusTicket.RESERVADO && !usuario.consultarHistorico().contains(this)) {
            usuario.consultarHistorico().add(this);
        }

        if (!usuario.consultarHistorico().contains(this) && this.status != StatusTicket.RESERVADO) {
            System.out.println("O usuário não possui este ticket em seu histórico/reserva.");
            return false;
        }

        this.status = StatusTicket.UTILIZADO;
        if (this instanceof TicketDigital) {
            ((TicketDigital) this).invalidarAposUso();
        }

        System.out.println("Uso do ticket confirmado com sucesso.");
        return true;
    }

    public abstract int calcularCustoEmPontos();

    //Métodos getter e setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getTarifaReais() {
        return tarifaReais;
    }

    public void setTarifaReais(double tarifaReais) {
        this.tarifaReais = tarifaReais;
    }

    public int getCustoEmPontos() {
        return custoEmPontos;
    }

    public void setCustoEmPontos(int custoEmPontos) {
        this.custoEmPontos = custoEmPontos;
    }

    public StatusTicket getStatus() {
        return status;
    }

    public void setStatus(StatusTicket status) {
        this.status = status;
    }

    public LocalDate getDataExpiracao() {
        return dataExpiracao;
    }

    public void setDataExpiracao(LocalDate dataExpiracao) {
        this.dataExpiracao = dataExpiracao;
    }


}
