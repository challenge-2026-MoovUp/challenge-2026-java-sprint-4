package br.com.fiap.MoovUp.models;

import br.com.fiap.MoovUp.enums.StatusUsuario;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Usuario {
    private String id;
    private String nome;
    private String email;
    private int saldoPontos;
    private List<Ticket> historicoResgates;
    private StatusUsuario status;
    private ImpactoAmbiental impactoAmbiental;
    private Date dataCadastro;

    public Usuario(String id, String nome, String email, int saldoPontos) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.saldoPontos = saldoPontos;
        this.historicoResgates = new ArrayList<>();
        this.impactoAmbiental = new ImpactoAmbiental();
        this.status = StatusUsuario.A;
        this.dataCadastro = new Date();
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


    //Métodos getter e setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getSaldoPontos() {
        return saldoPontos;
    }

    public void setSaldoPontos(int saldoPontos) {
        this.saldoPontos = saldoPontos;
    }

    public List<Ticket> getHistoricoResgates() {
        return historicoResgates;
    }

    public void setHistoricoResgates(List<Ticket> historicoResgates) {
        this.historicoResgates = historicoResgates;
    }

    public ImpactoAmbiental getImpactoAmbiental() {
        return this.impactoAmbiental;
    }

    public void setImpactoAmbiental(ImpactoAmbiental impactoAmbiental) {
        this.impactoAmbiental = impactoAmbiental;
    }

    public StatusUsuario getStatus() {
        return status;
    }

    public void setStatus(StatusUsuario status) {
        this.status = status;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
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
