package br.com.fiap.MoovUp.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Curtida {
    private Usuario usuario;
    private Postagem postagem;
    private LocalDateTime dataCurtida;

    public Curtida(Usuario usuario, Postagem postagem) {
        this.usuario = usuario;
        this.postagem = postagem;
        this.dataCurtida = LocalDateTime.now().withNano(0);
    }
}
