package br.com.fiap.MoovUp.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

// Tabela SEGUIDOR: o usuario segue o seguido (as duas pontas são Usuario).
// A PK (ID_USUARIO, ID_SEGUIDO) impede seguir a mesma pessoa duas vezes.
@Getter
@Setter
public class Seguidor {
    private Usuario usuario; // quem segue (no banco: ID_USUARIO)
    private Usuario seguido; // quem é seguido (no banco: ID_SEGUIDO)
    private LocalDateTime dataSeguido;

    public Seguidor(Usuario usuario, Usuario seguido) {
        this.usuario = usuario;
        this.seguido = seguido;
        this.dataSeguido = LocalDateTime.now().withNano(0);
    }
}
