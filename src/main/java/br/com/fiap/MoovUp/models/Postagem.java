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
    private String idUsuario;
    private String nomeUsuario; // não é coluna: vem do JOIN com USUARIO_MOOVUP nos SELECTs
    private String conteudo;
    private String imagem;
    private String video;
    private TipoPostagem tipoPostagem;
    private int curtidas;
    private LocalDateTime dataPostagem;

    public Postagem(String id, String titulo, String idUsuario, String conteudo, String imagem, String video, TipoPostagem tipoPostagem, int curtidas, LocalDateTime dataPostagem){
        this.id = id;
        this.titulo = titulo;
        this.idUsuario = idUsuario;
        this.conteudo = conteudo;
        this.imagem = imagem;
        this.video = video;
        this.tipoPostagem = tipoPostagem;
        this.curtidas = curtidas;
        this.dataPostagem = dataPostagem;
    }
}
