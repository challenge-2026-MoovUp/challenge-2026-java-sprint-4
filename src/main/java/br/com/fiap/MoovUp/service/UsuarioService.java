package br.com.fiap.MoovUp.service;

import br.com.fiap.MoovUp.dao.ImpactoAmbientalDAO;
import br.com.fiap.MoovUp.dao.UsuarioDAO;
import br.com.fiap.MoovUp.dto.*;
import br.com.fiap.MoovUp.enums.StatusUsuario;
import br.com.fiap.MoovUp.exception.CredenciaisInvalidasException;
import br.com.fiap.MoovUp.exception.RecursoNaoEncontradoException;
import br.com.fiap.MoovUp.exception.RegraNegocioException;
import br.com.fiap.MoovUp.mapper.ImpactoAmbientalMapper;
import br.com.fiap.MoovUp.mapper.UsuarioMapper;
import br.com.fiap.MoovUp.models.ImpactoAmbiental;
import br.com.fiap.MoovUp.models.Usuario;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UsuarioService {

    // GET /usuarios/nome/pontos bateria também com GET /usuarios/{id}/pontos (id = "nome"), e o Spring
    // escolhe a segunda: um usuário com esses nomes nunca seria achado pela busca por nome.
    // Toda rota nova no formato /usuarios/{id}/xxx precisa ter o "xxx" incluído aqui.
    private static final List<String> NOMES_RESERVADOS = List.of("pontos", "impacto", "seguindo", "resgates", "viagens");

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

    // GET /usuarios/nome/{nome}: o front acha o usuário pelo nome e daí em diante usa o id
    public UsuarioResponse buscarPorNome(String nome) {
        return UsuarioMapper.toDTO(buscarAtivoPorNome(nome));
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

    public List<ImpactoResponse> consultarHistoricoImpacto(String id) {
        buscarAtivo(id);

        List<ImpactoResponse> resposta = new ArrayList<>();
        for (ImpactoAmbiental impacto : impactoDAO.buscarPorUsuario(id)) {
            resposta.add(ImpactoAmbientalMapper.toDTO(impacto));
        }
        return resposta;
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

    // O nome é único (sem diferenciar maiúsculas: o DAO compara com UPPER). Usado também pelo SeguidorService
    public Usuario buscarAtivoPorNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome é obrigatório");
        }
        Usuario usuario = usuarioDAO.buscarPorNome(nome.trim());
        if (usuario == null || usuario.getStatus() != StatusUsuario.A) {
            throw new RecursoNaoEncontradoException("Nenhum usuário com o nome " + nome.trim());
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
        if (NOMES_RESERVADOS.contains(nome.trim().toLowerCase())) {
            throw new IllegalArgumentException("O nome " + nome.trim() + " é reservado pelo sistema, escolha outro");
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
