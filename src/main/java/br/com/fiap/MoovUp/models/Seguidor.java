package br.com.fiap.MoovUp.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Seguidor {
    private Usuario usuario;
    private Usuario seguido;
    private LocalDateTime dataSeguido;

    public Seguidor(Usuario usuario, Usuario seguido) {
        this.usuario = usuario;
        this.seguido = seguido;
        this.dataSeguido = LocalDateTime.now().withNano(0);
    }
}
