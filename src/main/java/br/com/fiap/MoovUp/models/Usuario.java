package br.com.fiap.MoovUp.models;

import br.com.fiap.MoovUp.enums.StatusUsuario;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Usuario {
    private String id;
    private String nome;
    private String email;
    private String senha;
    private int saldoPontos;
    private StatusUsuario status;
    private LocalDate dataCadastro;

    // Vazio: usado pelos DAOs quando o usuário vem de um JOIN (só id e nome preenchidos)
    public Usuario() {
    }

    public Usuario(String id, String nome, String email, String senha, int saldoPontos) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.saldoPontos = saldoPontos;
        this.status = StatusUsuario.A;
        this.dataCadastro = LocalDate.now();
    }

    public void adicionarPontos(int qtd){
        this.saldoPontos += qtd;
    }

    // Math.max: se o saldo já foi gasto, para em 0 (o banco não aceita saldo negativo)
    public void removerPontos(int qtd){
        this.saldoPontos = Math.max(0, this.saldoPontos - qtd);
    }
}
