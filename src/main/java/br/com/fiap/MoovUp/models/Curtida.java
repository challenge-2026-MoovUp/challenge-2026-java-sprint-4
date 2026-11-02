package br.com.fiap.MoovUp.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

// Tabela CURTIDA: a PK (ID_USUARIO, POSTAGEM_ID) impede o mesmo usuário de curtir duas vezes
@Getter
@Setter
public class Curtida {
    private Usuario usuario;   // quem curtiu (no banco: ID_USUARIO)
    private Postagem postagem; // no banco: POSTAGEM_ID
    private LocalDateTime dataCurtida;

    public Curtida(Usuario usuario, Postagem postagem) {
        this.usuario = usuario;
        this.postagem = postagem;
        this.dataCurtida = LocalDateTime.now().withNano(0);
    }
}
