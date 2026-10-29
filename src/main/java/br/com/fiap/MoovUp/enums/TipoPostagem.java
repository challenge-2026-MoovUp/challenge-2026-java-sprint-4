package br.com.fiap.MoovUp.enums;

public enum TipoPostagem {
    VIDEO(150),
    IMAGEM(100),
    TEXTO(50);

    private final int pontos;

    TipoPostagem(int pontos){
        this.pontos = pontos;
    }

    public int getPontos(){
        return pontos;
    }
}
