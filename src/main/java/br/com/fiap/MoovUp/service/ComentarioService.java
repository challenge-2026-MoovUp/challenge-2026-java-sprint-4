package br.com.fiap.MoovUp.service;

import br.com.fiap.MoovUp.dao.ComentarioDAO;
import br.com.fiap.MoovUp.dto.ComentarioRequest;
import br.com.fiap.MoovUp.dto.ComentarioResponse;
import br.com.fiap.MoovUp.exception.RecursoNaoEncontradoException;
import br.com.fiap.MoovUp.mapper.ComentarioMapper;
import br.com.fiap.MoovUp.models.Comentario;
import br.com.fiap.MoovUp.models.Usuario;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ComentarioService {

    private final ComentarioDAO comentarioDAO = new ComentarioDAO();
    private final PostagemService postagemService;
    private final UsuarioService usuarioService;

    public ComentarioService(PostagemService postagemService, UsuarioService usuarioService) {
        this.postagemService = postagemService;
        this.usuarioService = usuarioService;
    }

    // Regra: a postagem e o usuário precisam existir
    public ComentarioResponse criar(String idPostagem, ComentarioRequest request) {
        postagemService.buscarExistente(idPostagem);
        validarConteudo(request.conteudo());
        Usuario usuario = usuarioService.buscarAtivo(request.idUsuario());

        Comentario comentario = ComentarioMapper.toEntity(request, UUID.randomUUID().toString(), idPostagem);
        comentario.setNomeUsuario(usuario.getNome());
        comentarioDAO.cadastrarComentario(comentario);
        return ComentarioMapper.toDTO(comentario);
    }

    public List<ComentarioResponse> listarPorPostagem(String idPostagem) {
        postagemService.buscarExistente(idPostagem);

        List<ComentarioResponse> resposta = new ArrayList<>();
        for (Comentario comentario : comentarioDAO.buscarPorPostagem(idPostagem)) {
            resposta.add(ComentarioMapper.toDTO(comentario));
        }
        return resposta;
    }

    public void excluir(String id) {
        if (comentarioDAO.buscarPorId(id) == null) {
            throw new RecursoNaoEncontradoException("Comentário não encontrado");
        }
        comentarioDAO.excluirComentario(id);
    }

    private void validarConteudo(String conteudo) {
        if (conteudo == null || conteudo.trim().isEmpty()) {
            throw new IllegalArgumentException("O conteúdo do comentário é obrigatório");
        }
        if (conteudo.trim().length() > 200) {
            throw new IllegalArgumentException("O comentário pode ter no máximo 200 caracteres");
        }
    }
}
