package br.com.fiap.MoovUp.models;

import br.com.fiap.MoovUp.enums.Modalidade;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ImpactoAmbiental {
    private String id;
    private Usuario usuario;      // no banco: ID_USUARIO
    private TicketDigital ticket; // no banco: ID_TICKET
    private Viagem viagem;        // a viagem que gerou este impacto (null nos impactos antigos)
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


    public static ImpactoAmbiental criarParaViagem(Viagem viagem, Modalidade modalidade) {
        ImpactoAmbiental impacto = new ImpactoAmbiental();
        impacto.setUsuario(viagem.getUsuario());
        impacto.setTicket(viagem.getTicket());
        impacto.setViagem(viagem);
        impacto.setModalidade(modalidade);
        impacto.setViagensRealizadas(1);
        impacto.setKmPercorridos(modalidade.getKm());
        impacto.setCo2Evitado(modalidade.getKm() * modalidade.getCo2PorKm());
        return impacto;
    }
}
