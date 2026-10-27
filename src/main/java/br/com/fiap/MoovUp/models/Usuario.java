package br.com.fiap.MoovUp.models;

import br.com.fiap.MoovUp.enums.StatusUsuario;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class Usuario {
    private String id;
    private String nome;
    private String email;
    private int saldoPontos;
    private List<Ticket> historicoResgates;
    private StatusUsuario status;
    private ImpactoAmbiental impactoAmbiental;
    private LocalDate dataCadastro;

    public Usuario(String id, String nome, String email, int saldoPontos) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.saldoPontos = saldoPontos;
        this.historicoResgates = new ArrayList<>();
        this.impactoAmbiental = new ImpactoAmbiental();
        this.status = StatusUsuario.A;
        this.dataCadastro = LocalDate.now();
    }

    public void adicionarPontos(int qtd){
        this.saldoPontos += qtd;
    }

    public boolean resgatarTicket(Ticket t){
        return t.resgatar(this);
    }

    public int consultarSaldo(){
        return  this.saldoPontos;
    }

    public List<Ticket> consultarHistorico(){
        return this.historicoResgates;
    }



    @Override
    public String toString() {
        return "ID: " + id + "\n" +
                "Nome: " + nome + "\n" +
                "Email: " + email + "\n" +
                "Saldo: " + saldoPontos + "\n" +
                "Status: " + status;
    }
}
