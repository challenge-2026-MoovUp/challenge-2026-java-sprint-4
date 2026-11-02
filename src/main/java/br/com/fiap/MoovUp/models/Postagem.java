package br.com.fiap.MoovUp.models;

import br.com.fiap.MoovUp.enums.TipoPostagem;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Postagem {
    private String id;
    private String titulo;
    private Usuario usuario;
    private String conteudo;
    private String imagem;
    private String video;
    private TipoPostagem tipoPostagem;
    private int curtidas;
    private LocalDateTime dataPostagem;

    public Postagem() {
    }

    public Postagem(String id, String titulo, Usuario usuario, String conteudo, String imagem, String video, TipoPostagem tipoPostagem, int curtidas, LocalDateTime dataPostagem){
        this.id = id;
        this.titulo = titulo;
        this.usuario = usuario;
        this.conteudo = conteudo;
        this.imagem = imagem;
        this.video = video;
        this.tipoPostagem = tipoPostagem;
        this.curtidas = curtidas;
        this.dataPostagem = dataPostagem;
    }
}
