package br.com.fiap.MoovUp.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Comentario {
    private String id;
    private String idPostagem;
    private String autor;
    private String conteudo;
    private int curtidas;
    private LocalDateTime dataComentario;

    public Comentario(String id, String idPostagem, String autor, String conteudo, int curtidas, LocalDateTime dataComentario){
        this.id = id;
        this.idPostagem = idPostagem;
        this.autor = autor;
        this.conteudo = conteudo;
        this.curtidas = curtidas;
        this.dataComentario = dataComentario;
    }
}
