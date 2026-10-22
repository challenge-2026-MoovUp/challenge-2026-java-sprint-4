package br.com.fiap.MoovUp.enums;

public enum Modalidade {
    ONIBUS(4.40),
    METRO(5.00),
    TREM(5.00);

    private final double tarifaReais;

    Modalidade(double tarifaReais){
        this.tarifaReais = tarifaReais;
    }

    public double getTarifa(){
        return tarifaReais;
    }
}
