package br.com.fiap.MoovUp.service;

import br.com.fiap.MoovUp.dao.SeguidorDAO;
import br.com.fiap.MoovUp.dto.SeguidoResponse;
import br.com.fiap.MoovUp.dto.SeguirRequest;
import br.com.fiap.MoovUp.exception.RecursoNaoEncontradoException;
import br.com.fiap.MoovUp.exception.RegraNegocioException;
import br.com.fiap.MoovUp.mapper.SeguidorMapper;
import br.com.fiap.MoovUp.models.Seguidor;
import br.com.fiap.MoovUp.models.Usuario;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SeguidorService {

    private final SeguidorDAO seguidorDAO = new SeguidorDAO();
    private final UsuarioService usuarioService;

    public SeguidorService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Ordem: 400 (sem idSeguido) -> 404 (usuário/seguido) -> 422 (ele mesmo / já segue)
    public SeguidoResponse seguir(String idUsuario, SeguirRequest request) {
        if (request.idSeguido() == null || request.idSeguido().trim().isEmpty()) {
            throw new IllegalArgumentException("O idSeguido é obrigatório");
        }

        Usuario usuario = usuarioService.buscarAtivo(idUsuario);
        Usuario seguido = usuarioService.buscarAtivo(request.idSeguido());

        if (usuario.getId().equals(seguido.getId())) {
            throw new RegraNegocioException("O usuário não pode seguir a si mesmo");
        }
        if (seguidorDAO.existeSeguidor(usuario.getId(), seguido.getId())) {
            throw new RegraNegocioException("O usuário já segue esta pessoa");
        }

        Seguidor seguidor = new Seguidor(usuario, seguido);
        seguidorDAO.cadastrarSeguidor(seguidor);
        return SeguidorMapper.toDTO(seguidor);
    }

    public List<SeguidoResponse> listarSeguidos(String idUsuario) {
        usuarioService.buscarAtivo(idUsuario);

        List<SeguidoResponse> resposta = new ArrayList<>();
        for (Seguidor seguidor : seguidorDAO.listarSeguidos(idUsuario)) {
            resposta.add(SeguidorMapper.toDTO(seguidor));
        }
        return resposta;
    }

    public void deixarDeSeguir(String idUsuario, String idSeguido) {
        usuarioService.buscarAtivo(idUsuario);

        if (!seguidorDAO.existeSeguidor(idUsuario, idSeguido)) {
            throw new RecursoNaoEncontradoException("O usuário não segue esta pessoa");
        }
        seguidorDAO.excluirSeguidor(idUsuario, idSeguido);
    }
}
