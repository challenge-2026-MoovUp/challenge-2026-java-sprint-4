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

    public SeguidoResponse seguir(String idUsuario, SeguirRequest request) {
        boolean temId = request.idSeguido() != null && !request.idSeguido().trim().isEmpty();
        boolean temNome = request.nomeSeguido() != null && !request.nomeSeguido().trim().isEmpty();

        if (!temId && !temNome) {
            throw new IllegalArgumentException("Informe o idSeguido ou o nomeSeguido");
        }
        if (temId && temNome) {
            throw new IllegalArgumentException("Informe só um: o idSeguido ou o nomeSeguido");
        }

        Usuario usuario = usuarioService.buscarAtivo(idUsuario);
        Usuario seguido;
        if (temId) {
            seguido = usuarioService.buscarAtivo(request.idSeguido().trim());
        } else {
            seguido = usuarioService.buscarAtivoPorNome(request.nomeSeguido());
        }

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
