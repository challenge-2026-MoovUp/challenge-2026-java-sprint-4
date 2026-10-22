package br.com.fiap.MoovUp.models;

import br.com.fiap.MoovUp.enums.Modalidade;

import java.util.Date;

public class ImpactoAmbiental {
    private String id;
    private String idUsuario;
    private String idTicket;
    private Modalidade modalidade;
    private double co2Evitado;
    private int viagensRealizadas;
    private double kmPercorridos;
    private Date dataCalculo;

    public ImpactoAmbiental() {
        this.co2Evitado = 0;
        this.viagensRealizadas = 0;
        this.kmPercorridos = 0;
        this.dataCalculo = new Date();
    }


    public static ImpactoAmbiental criarParaTicket(String idUsuario, String idTicket, Modalidade modalidade) {
        ImpactoAmbiental impacto = new ImpactoAmbiental();
        impacto.setIdUsuario(idUsuario);
        impacto.setIdTicket(idTicket);
        impacto.setModalidade(modalidade);
        impacto.calcularValoresPorModalidade(modalidade);
        impacto.setDataCalculo(new Date());
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

    public void acumularViagemPorModalidade(Modalidade modalidade) {
        if (modalidade == null) {
            throw new IllegalArgumentException("Modalidade inválida.");
        }

        double km = 0;
        double gramasPorKm = 0;

        switch (modalidade) {
            case TREM:
                km = 20.0;
                gramasPorKm = 200.0;
                break;
            case METRO:
                km = 10.0;
                gramasPorKm = 200.0;
                break;
            case ONIBUS:
                km = 15.0;
                gramasPorKm = 170.0;
                break;
            default:
                throw new IllegalArgumentException("Modalidade não suportada para cálculo de impacto.");
        }

        this.modalidade = modalidade;
        acumularViagem(km, gramasPorKm);
    }

    public void acumularViagem(double km, double co2PorKm) {
        if (km < 0) {
            throw new IllegalArgumentException("A distância percorrida não pode ser negativa.");
        }

        this.kmPercorridos += km;
        this.viagensRealizadas++;
        this.co2Evitado += km * co2PorKm;
    }


    public String gerarRelatorio(){
        return String.format(
                " Viagens realizadas : %d\n" +
                        " Km percorridos     : %.1f km\n" +
                        " CO₂ evitado        : %.2f g\n" +
                        " Modalidade          : %s",
                viagensRealizadas,
                kmPercorridos,
                co2Evitado,
                modalidade != null ? modalidade.name() : "N/A"
        );
    }

    //Métodos getter e setter

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(String idTicket) {
        this.idTicket = idTicket;
    }

    public Modalidade getModalidade() {
        return modalidade;
    }

    public void setModalidade(Modalidade modalidade) {
        this.modalidade = modalidade;
    }

    public double getCo2Evitado() {
        return co2Evitado;
    }

    public void setCo2Evitado(double co2Evitado) {
        this.co2Evitado = co2Evitado;
    }

    public int getViagensRealizadas() {
        return viagensRealizadas;
    }

    public void setViagensRealizadas(int viagensRealizadas) {
        this.viagensRealizadas = viagensRealizadas;
    }

    public double getKmPercorridos() {
        return kmPercorridos;
    }

    public void setKmPercorridos(double kmPercorridos) {
        this.kmPercorridos = kmPercorridos;
    }

    public java.util.Date getDataCalculo() {
        return dataCalculo;
    }

    public void setDataCalculo(java.util.Date dataCalculo) {
        this.dataCalculo = dataCalculo;
    }


}
