package br.com.fiap.MoovUp.models;

import br.com.fiap.MoovUp.enums.StatusUsuario;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Usuario {
    private String id;
    private String nome;
    private String email;
    private String senha;
    private int saldoPontos;
    private List<Ticket> historicoResgates;
    private StatusUsuario status;
    private ImpactoAmbiental impactoAmbiental;
    private LocalDate dataCadastro;

    public Usuario(String id, String nome, String email, String senha, int saldoPontos) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.saldoPontos = saldoPontos;
        this.historicoResgates = new ArrayList<>();
        this.impactoAmbiental = new ImpactoAmbiental();
        this.status = StatusUsuario.A;
        this.dataCadastro = LocalDate.now();
    }

    public void adicionarPontos(int qtd){
        this.saldoPontos += qtd;
    }
}
