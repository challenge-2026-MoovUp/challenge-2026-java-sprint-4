package br.com.fiap.MoovUp.service;

import br.com.fiap.MoovUp.dao.ImpactoAmbientalDAO;
import br.com.fiap.MoovUp.dao.UsuarioDAO;
import br.com.fiap.MoovUp.dto.*;
import br.com.fiap.MoovUp.enums.StatusUsuario;
import br.com.fiap.MoovUp.exception.CredenciaisInvalidasException;
import br.com.fiap.MoovUp.exception.RecursoNaoEncontradoException;
import br.com.fiap.MoovUp.exception.RegraNegocioException;
import br.com.fiap.MoovUp.mapper.UsuarioMapper;
import br.com.fiap.MoovUp.models.Usuario;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UsuarioService {

    // DAOs criados com new, como no projeto da professora (decisão de 29/10)
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final ImpactoAmbientalDAO impactoDAO = new ImpactoAmbientalDAO();

    public UsuarioResponse cadastrar(UsuarioRequest request) {
        validarNome(request.nome());
        validarEmail(request.email());
        validarSenha(request.senha());
        validarEmailUnico(request.email(), null);
        validarNomeUnico(request.nome(), null);

        Usuario usuario = UsuarioMapper.toEntity(request, UUID.randomUUID().toString());
        usuarioDAO.cadastrarUsuario(usuario);
        return UsuarioMapper.toDTO(usuario);
    }

    // Login simples, sem token: o front guarda o id devolvido
    public UsuarioResponse login(LoginRequest request) {
        if (request.email() == null || request.senha() == null) {
            throw new IllegalArgumentException("Email e senha são obrigatórios");
        }

        Usuario usuario = usuarioDAO.buscarPorEmail(request.email().trim());

        if (usuario == null || usuario.getStatus() != StatusUsuario.A || !usuario.getSenha().equals(request.senha())) {
            throw new CredenciaisInvalidasException("Email ou senha inválidos");
        }
        return UsuarioMapper.toDTO(usuario);
    }

    // Só os ativos: os inativos foram "excluídos" (exclusão lógica)
    public List<UsuarioResponse> listar() {
        List<UsuarioResponse> resposta = new ArrayList<>();
        for (Usuario usuario : usuarioDAO.buscarUsuariosPorStatus(StatusUsuario.A)) {
            resposta.add(UsuarioMapper.toDTO(usuario));
        }
        return resposta;
    }

    public UsuarioResponse buscarPorId(String id) {
        return UsuarioMapper.toDTO(buscarAtivo(id));
    }

    public UsuarioResponse alterar(String id, UsuarioAlteracaoRequest request) {
        Usuario usuario = buscarAtivo(id);
        validarNome(request.nome());
        validarEmail(request.email());
        validarEmailUnico(request.email(), id);
        validarNomeUnico(request.nome(), id);

        usuario.setNome(request.nome().trim());
        usuario.setEmail(request.email().trim());
        usuarioDAO.alterarUsuario(usuario);
        return UsuarioMapper.toDTO(usuario);
    }

    // Exclusão lógica: o usuário tem tickets, viagens e impactos que não podem sumir
    public void excluir(String id) {
        Usuario usuario = buscarAtivo(id);
        usuario.setStatus(StatusUsuario.I);
        usuarioDAO.alterarUsuario(usuario);
    }

    public PontosResponse consultarPontos(String id) {
        Usuario usuario = buscarAtivo(id);
        return new PontosResponse(usuario.getId(), usuario.getSaldoPontos());
    }

    public ImpactoResumoResponse consultarImpacto(String id) {
        buscarAtivo(id);
        return impactoDAO.resumirPorUsuario(id);
    }

    public List<RankingResponse> ranking() {
        List<RankingResponse> ranking = new ArrayList<>();
        int posicao = 1;
        for (Usuario usuario : usuarioDAO.listarRanking()) {
            ranking.add(new RankingResponse(posicao++, usuario.getId(), usuario.getNome(), usuario.getSaldoPontos()));
        }
        return ranking;
    }

    // Usado também pelos outros services (postagem, resgate, viagem) para validar o usuário
    public Usuario buscarAtivo(String id) {
        Usuario usuario = usuarioDAO.buscarPorId(id);
        if (usuario == null || usuario.getStatus() != StatusUsuario.A) {
            throw new RecursoNaoEncontradoException("Usuário não encontrado");
        }
        return usuario;
    }

    // Validações dos dados do JSON (IllegalArgumentException vira 400 no controller)
    private void validarNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome é obrigatório");
        }
        if (nome.trim().length() > 40) {
            throw new IllegalArgumentException("O nome pode ter no máximo 40 caracteres");
        }
    }

    private void validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("O email é obrigatório");
        }
        if (!email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("Email inválido");
        }
        if (email.trim().length() > 60) {
            throw new IllegalArgumentException("O email pode ter no máximo 60 caracteres");
        }
    }

    private void validarSenha(String senha) {
        if (senha == null || senha.length() < 6 || senha.length() > 100) {
            throw new IllegalArgumentException("A senha deve ter entre 6 e 100 caracteres");
        }
    }

    // idAtual = null no cadastro; no PUT, o próprio usuário pode manter o email dele
    private void validarEmailUnico(String email, String idAtual) {
        Usuario existente = usuarioDAO.buscarPorEmail(email.trim());
        if (existente != null && !existente.getId().equals(idAtual)) {
            throw new RegraNegocioException("Email já cadastrado");
        }
    }

    private void validarNomeUnico(String nome, String idAtual) {
        Usuario existente = usuarioDAO.buscarPorNome(nome.trim());
        if (existente != null && !existente.getId().equals(idAtual)) {
            throw new RegraNegocioException("Nome de usuário já cadastrado");
        }
    }
}
