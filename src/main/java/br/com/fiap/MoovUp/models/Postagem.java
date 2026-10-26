package br.com.fiap.MoovUp.models;

import br.com.fiap.MoovUp.enums.TipoPostagem;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class Postagem {
    private int id;
    private String titulo;
    private String autor;
    private String conteudo;
    private String imagem;
    private String video;
    private TipoPostagem tipoPostagem;
    private int curtidas;
    private List<Comentario> comentarios;
    private Date dataPostagem;

    public Postagem(int id, String titulo, String autor, String conteudo, String imagem, String video, TipoPostagem tipoPostagem, int curtidas, Date dataPostagem){
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.conteudo = conteudo;
        this.imagem = imagem;
        this.video = video;
        this.tipoPostagem = tipoPostagem;
        this.curtidas = curtidas;
        this.comentarios = new ArrayList<>();
        this.dataPostagem = dataPostagem;
    }
}
