package br.com.fiap.MoovUp.service;

import br.com.fiap.MoovUp.dao.CurtidaDAO;
import br.com.fiap.MoovUp.dao.PostagemDAO;
import br.com.fiap.MoovUp.dao.UsuarioDAO;
import br.com.fiap.MoovUp.dto.*;
import br.com.fiap.MoovUp.enums.TipoPostagem;
import br.com.fiap.MoovUp.exception.RecursoNaoEncontradoException;
import br.com.fiap.MoovUp.exception.RegraNegocioException;
import br.com.fiap.MoovUp.mapper.PostagemMapper;
import br.com.fiap.MoovUp.models.Curtida;
import br.com.fiap.MoovUp.models.Postagem;
import br.com.fiap.MoovUp.models.Usuario;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PostagemService {

    private final PostagemDAO postagemDAO = new PostagemDAO();
    private final CurtidaDAO curtidaDAO = new CurtidaDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final UsuarioService usuarioService;

    // O Spring entrega o UsuarioService pronto (injeção pelo construtor)
    public PostagemService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Regra: a postagem dá pontos ao autor conforme o tipo (TEXTO 50, IMAGEM 100, VIDEO 150)
    public PostagemResponse criar(PostagemRequest request) {
        TipoPostagem tipo = converterTipo(request.tipoPostagem());
        validarTitulo(request.titulo());
        validarConteudo(request.conteudo());
        validarMidia(tipo, request.imagem(), request.video());

        Usuario autor = usuarioService.buscarAtivo(request.idUsuario());

        Postagem postagem = PostagemMapper.toEntity(request, UUID.randomUUID().toString(), tipo);
        postagem.setNomeUsuario(autor.getNome());
        postagemDAO.cadastrarPostagem(postagem);

        autor.adicionarPontos(tipo.getPontos());
        usuarioDAO.alterarUsuario(autor);

        return PostagemMapper.toDTO(postagem);
    }

    public List<PostagemResponse> listar() {
        List<PostagemResponse> resposta = new ArrayList<>();
        for (Postagem postagem : postagemDAO.listarTodos()) {
            resposta.add(PostagemMapper.toDTO(postagem));
        }
        return resposta;
    }

    public PostagemResponse buscarPorId(String id) {
        return PostagemMapper.toDTO(buscarExistente(id));
    }

    public PostagemResponse alterar(String id, PostagemAlteracaoRequest request) {
        Postagem postagem = buscarExistente(id);
        validarTitulo(request.titulo());
        validarConteudo(request.conteudo());

        postagem.setTitulo(request.titulo().trim());
        postagem.setConteudo(request.conteudo().trim());
        postagemDAO.alterarPostagem(postagem);
        return PostagemMapper.toDTO(postagem);
    }

    // Regra: excluir a postagem tira do autor os pontos que ela deu
    public void excluir(String id) {
        Postagem postagem = buscarExistente(id);
        postagemDAO.excluirPostagem(id);

        Usuario autor = usuarioDAO.buscarPorId(postagem.getIdUsuario());
        if (autor != null) {
            autor.removerPontos(postagem.getTipoPostagem().getPontos());
            usuarioDAO.alterarUsuario(autor);
        }
    }

    // Regra: cada usuário curte uma postagem uma vez só
    public CurtidaResponse curtir(String idPostagem, CurtidaRequest request) {
        Postagem postagem = buscarExistente(idPostagem);
        Usuario usuario = usuarioService.buscarAtivo(request.idUsuario());

        if (curtidaDAO.existeCurtida(usuario.getId(), idPostagem)) {
            throw new RegraNegocioException("O usuário já curtiu esta postagem");
        }

        curtidaDAO.cadastrarCurtida(new Curtida(usuario.getId(), idPostagem));
        postagemDAO.somarCurtida(idPostagem);
        return new CurtidaResponse(idPostagem, postagem.getCurtidas() + 1);
    }

    // Usado também pelo ComentarioService
    public Postagem buscarExistente(String id) {
        Postagem postagem = postagemDAO.buscarPorId(id);
        if (postagem == null) {
            throw new RecursoNaoEncontradoException("Postagem não encontrada");
        }
        return postagem;
    }

    // valueOf lança IllegalArgumentException se o texto não for TEXTO, IMAGEM ou VIDEO
    private TipoPostagem converterTipo(String tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("O tipo da postagem é obrigatório (TEXTO, IMAGEM ou VIDEO)");
        }
        try {
            return TipoPostagem.valueOf(tipo.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo de postagem inválido: use TEXTO, IMAGEM ou VIDEO");
        }
    }

    private void validarTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("O título é obrigatório");
        }
        if (titulo.trim().length() > 50) {
            throw new IllegalArgumentException("O título pode ter no máximo 50 caracteres");
        }
    }

    private void validarConteudo(String conteudo) {
        if (conteudo == null || conteudo.trim().isEmpty()) {
            throw new IllegalArgumentException("O conteúdo é obrigatório");
        }
        if (conteudo.trim().length() > 200) {
            throw new IllegalArgumentException("O conteúdo pode ter no máximo 200 caracteres");
        }
    }

    // Regra: IMAGEM exige o link da imagem e VIDEO exige o link do vídeo (senão ganharia pontos sem mídia)
    private void validarMidia(TipoPostagem tipo, String imagem, String video) {
        if (tipo == TipoPostagem.IMAGEM && (imagem == null || imagem.trim().isEmpty())) {
            throw new IllegalArgumentException("Postagem do tipo IMAGEM precisa do link da imagem");
        }
        if (tipo == TipoPostagem.VIDEO && (video == null || video.trim().isEmpty())) {
            throw new IllegalArgumentException("Postagem do tipo VIDEO precisa do link do vídeo");
        }
        if (imagem != null && imagem.length() > 300) {
            throw new IllegalArgumentException("O link da imagem pode ter no máximo 300 caracteres");
        }
        if (video != null && video.length() > 350) {
            throw new IllegalArgumentException("O link do vídeo pode ter no máximo 350 caracteres");
        }
    }
}
