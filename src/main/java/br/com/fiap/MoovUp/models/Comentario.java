package br.com.fiap.MoovUp.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Comentario {
    private String id;
    private Postagem postagem;
    private Usuario usuario;
    private String conteudo;
    private int curtidas;
    private LocalDateTime dataComentario;

    public Comentario(String id, Postagem postagem, Usuario usuario, String conteudo, int curtidas, LocalDateTime dataComentario){
        this.id = id;
        this.postagem = postagem;
        this.usuario = usuario;
        this.conteudo = conteudo;
        this.curtidas = curtidas;
        this.dataComentario = dataComentario;
    }
}
