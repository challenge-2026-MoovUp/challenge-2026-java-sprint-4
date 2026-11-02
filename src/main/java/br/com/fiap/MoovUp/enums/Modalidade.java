package br.com.fiap.MoovUp.enums;

public enum Modalidade {
    ONIBUS(4.40, 15.0, 170.0),
    METRO(5.00, 10.0, 200.0),
    TREM(5.00, 20.0, 200.0);

    private final double tarifaReais;
    private final double km;
    private final double co2PorKm;

    Modalidade(double tarifaReais, double km, double co2PorKm){
        this.tarifaReais = tarifaReais;
        this.km = km;
        this.co2PorKm = co2PorKm;
    }

    public double getTarifa(){
        return tarifaReais;
    }

    public double getKm(){
        return km;
    }

    public double getCo2PorKm(){
        return co2PorKm;
    }
}
