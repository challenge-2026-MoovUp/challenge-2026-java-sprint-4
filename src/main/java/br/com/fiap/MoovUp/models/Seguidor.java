package br.com.fiap.MoovUp.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

// Tabela SEGUIDOR: ID_USUARIO segue ID_SEGUIDO.
// A PK (ID_USUARIO, ID_SEGUIDO) impede seguir a mesma pessoa duas vezes.
@Getter
@Setter
public class Seguidor {
    private String idUsuario;
    private String idSeguido;
    private LocalDateTime dataSeguido;
    private String nomeSeguido; // não é coluna: vem do JOIN com USUARIO_MOOVUP

    public Seguidor(String idUsuario, String idSeguido) {
        this.idUsuario = idUsuario;
        this.idSeguido = idSeguido;
        this.dataSeguido = LocalDateTime.now().withNano(0);
    }
}
