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
        impacto.calcularValoresPorModalidade(modalidade);
        impacto.setDataCalculo(LocalDateTime.now().withNano(0));
        return impacto;
    }

    public void calcularValoresPorModalidade(Modalidade modalidade) {
        if (modalidade == null) {
            return;
        }

        this.modalidade = modalidade;
        this.viagensRealizadas = 1;

        switch (modalidade) {
            case TREM:
                this.kmPercorridos = 20.0;
                this.co2Evitado = 200.0 * this.kmPercorridos;
                break;
            case METRO:
                this.kmPercorridos = 10.0;
                this.co2Evitado = 200.0 * this.kmPercorridos;
                break;
            case ONIBUS:
                this.kmPercorridos = 15.0;
                this.co2Evitado = 170.0 * this.kmPercorridos;
                break;
            default:
                this.kmPercorridos = 0.0;
                this.co2Evitado = 0.0;
                break;
        }
    }
}
