package br.com.fiap.MoovUp.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

// Tabela CURTIDA: a PK (ID_USUARIO, POSTAGEM_ID) impede o mesmo usuário de curtir duas vezes
@Getter
@Setter
public class Curtida {
    private String idUsuario;
    private String idPostagem;
    private LocalDateTime dataCurtida;

    public Curtida(String idUsuario, String idPostagem) {
        this.idUsuario = idUsuario;
        this.idPostagem = idPostagem;
        this.dataCurtida = LocalDateTime.now().withNano(0);
    }
}
