package br.com.fiap.MoovUp.models;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Comentario {
    private int comentarioId;
    private int postagemId;
    private String autor;
    private String conteudo;
    private int curtidas;
    private Date dataComentario;

    public Comentario(int comentarioId, int postagemId, String autor, String conteudo, int curtidas, Date dataComentario){
        this.comentarioId = comentarioId;
        this.postagemId = postagemId;
        this.autor = autor;
        this.conteudo = conteudo;
        this.curtidas = curtidas;
        this.dataComentario = dataComentario;
    }
}
