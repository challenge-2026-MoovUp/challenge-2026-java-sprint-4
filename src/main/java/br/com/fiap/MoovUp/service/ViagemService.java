package br.com.fiap.MoovUp.service;

import br.com.fiap.MoovUp.dao.ImpactoAmbientalDAO;
import br.com.fiap.MoovUp.dao.UsuarioDAO;
import br.com.fiap.MoovUp.dao.ViagemDAO;
import br.com.fiap.MoovUp.dto.EstacaoResponse;
import br.com.fiap.MoovUp.dto.LinhaResponse;
import br.com.fiap.MoovUp.dto.ViagemRequest;
import br.com.fiap.MoovUp.dto.ViagemResponse;
import br.com.fiap.MoovUp.enums.Estacao;
import br.com.fiap.MoovUp.enums.Linha;
import br.com.fiap.MoovUp.enums.Modalidade;
import br.com.fiap.MoovUp.enums.StatusTicket;
import br.com.fiap.MoovUp.exception.RecursoNaoEncontradoException;
import br.com.fiap.MoovUp.exception.RegraNegocioException;
import br.com.fiap.MoovUp.mapper.LinhaMapper;
import br.com.fiap.MoovUp.mapper.ViagemMapper;
import br.com.fiap.MoovUp.models.ImpactoAmbiental;
import br.com.fiap.MoovUp.models.TicketDigital;
import br.com.fiap.MoovUp.models.Usuario;
import br.com.fiap.MoovUp.models.Viagem;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ViagemService {

    private static final int PONTOS_POR_VIAGEM = 35;
    private static final int VIAGENS_POR_TICKET = 1;

    private final ViagemDAO viagemDAO = new ViagemDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final ImpactoAmbientalDAO impactoDAO = new ImpactoAmbientalDAO();
    private final UsuarioService usuarioService;
    private final TicketService ticketService;

    public ViagemService(UsuarioService usuarioService, TicketService ticketService) {
        this.usuarioService = usuarioService;
        this.ticketService = ticketService;
    }

    public ViagemResponse registrar(String idUsuario, ViagemRequest request) {
        Usuario usuario = usuarioService.buscarAtivo(idUsuario);

        if (request.idTicket() == null || request.idTicket().trim().isEmpty()) {
            throw new IllegalArgumentException("O idTicket é obrigatório");
        }
        Linha linha = converterLinha(request.linha());
        Estacao estacao = converterEstacao(request.estacaoEmbarque());

        TicketDigital ticket = ticketService.buscarExistente(request.idTicket().trim());

        if (ticket.getUsuario() == null || !usuario.getId().equals(ticket.getUsuario().getId())) {
            throw new RegraNegocioException("Este ticket não pertence ao usuário");
        }

        if (ticket.getStatus() != StatusTicket.UTILIZADO) {
            throw new RegraNegocioException("Use o ticket na catraca antes de registrar a viagem (status atual: " + ticket.getStatus() + ")");
        }

        if (viagemDAO.contarPorTicket(ticket.getId()) >= VIAGENS_POR_TICKET) {
            throw new RegraNegocioException("Já existe uma viagem registrada para este ticket");
        }

        if (!linha.getEstacoes().contains(estacao)) {
            throw new RegraNegocioException("A estação " + estacao.getNome() + " não pertence à " + linha.getNome());
        }

        Viagem viagem = new Viagem(UUID.randomUUID().toString(), usuario, ticket,
                linha, estacao, PONTOS_POR_VIAGEM, LocalDateTime.now().withNano(0));
        viagemDAO.cadastrarViagem(viagem);

        usuario.adicionarPontos(PONTOS_POR_VIAGEM);
        usuarioDAO.alterarUsuario(usuario);

        ImpactoAmbiental impacto = ImpactoAmbiental.criarParaViagem(viagem, linha.getModalidade());
        impacto.setId(UUID.randomUUID().toString());
        impactoDAO.cadastrarImpactoAmbiental(impacto);

        return ViagemMapper.toDTO(viagem);
    }

    public List<ViagemResponse> listar(String idUsuario) {
        usuarioService.buscarAtivo(idUsuario);

        List<ViagemResponse> resposta = new ArrayList<>();
        for (Viagem viagem : viagemDAO.buscarPorUsuario(idUsuario)) {
            resposta.add(ViagemMapper.toDTO(viagem));
        }
        return resposta;
    }

    public List<LinhaResponse> listarLinhas(String modalidade) {
        Modalidade filtro = null;
        if (modalidade != null) {
            filtro = ticketService.converterModalidade(modalidade);
        }

        List<LinhaResponse> resposta = new ArrayList<>();
        for (Linha linha : Linha.values()) {
            if (filtro == null || linha.getModalidade() == filtro) {
                resposta.add(LinhaMapper.toDTO(linha));
            }
        }
        return resposta;
    }

    public List<EstacaoResponse> listarEstacoes(String codigoLinha) {
        Linha linha;
        try {
            linha = Linha.valueOf(codigoLinha.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RecursoNaoEncontradoException("Linha não encontrada");
        }

        List<EstacaoResponse> resposta = new ArrayList<>();
        for (Estacao estacao : linha.getEstacoes()) {
            resposta.add(LinhaMapper.toDTO(estacao));
        }
        return resposta;
    }

    private Linha converterLinha(String linha) {
        if (linha == null) {
            throw new IllegalArgumentException("A linha é obrigatória (veja GET /linhas)");
        }
        try {
            return Linha.valueOf(linha.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Linha inválida (veja GET /linhas)");
        }
    }

    private Estacao converterEstacao(String estacao) {
        if (estacao == null) {
            throw new IllegalArgumentException("A estação de embarque é obrigatória (veja GET /linhas/{codigo}/estacoes)");
        }
        try {
            return Estacao.valueOf(estacao.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Estação inválida (veja GET /linhas/{codigo}/estacoes)");
        }
    }
}
