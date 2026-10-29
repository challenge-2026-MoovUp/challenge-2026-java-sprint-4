package br.com.fiap.MoovUp.models;

import br.com.fiap.MoovUp.enums.Modalidade;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ImpactoAmbiental {
    private String id;
    private String idUsuario;
    private String idTicket;
    private Modalidade modalidade;
    private double co2Evitado;
    private int viagensRealizadas;
    private double kmPercorridos;
    private LocalDateTime dataCalculo;

    public ImpactoAmbiental() {
        this.co2Evitado = 0;
        this.viagensRealizadas = 0;
        this.kmPercorridos = 0;
        this.dataCalculo = LocalDateTime.now().withNano(0);
    }


    public static ImpactoAmbiental criarParaTicket(String idUsuario, String idTicket, Modalidade modalidade) {
        ImpactoAmbiental impacto = new ImpactoAmbiental();
        impacto.setIdUsuario(idUsuario);
        impacto.setIdTicket(idTicket);
        impacto.setModalidade(modalidade);
        impacto.setViagensRealizadas(1);
        impacto.setKmPercorridos(modalidade.getKm());
        impacto.setCo2Evitado(modalidade.getKm() * modalidade.getCo2PorKm());
        return impacto;
    }
}
