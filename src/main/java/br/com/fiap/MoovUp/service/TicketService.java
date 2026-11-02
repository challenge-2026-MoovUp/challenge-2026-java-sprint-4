package br.com.fiap.MoovUp.service;

import br.com.fiap.MoovUp.dao.TicketDigitalDAO;
import br.com.fiap.MoovUp.dto.EstoqueResponse;
import br.com.fiap.MoovUp.dto.TicketRequest;
import br.com.fiap.MoovUp.dto.TicketResponse;
import br.com.fiap.MoovUp.enums.Modalidade;
import br.com.fiap.MoovUp.enums.StatusTicket;
import br.com.fiap.MoovUp.exception.RecursoNaoEncontradoException;
import br.com.fiap.MoovUp.exception.RegraNegocioException;
import br.com.fiap.MoovUp.mapper.TicketMapper;
import br.com.fiap.MoovUp.models.TicketDigital;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TicketService {

    private static final int MAXIMO_POR_ABASTECIMENTO = 50;

    private final TicketDigitalDAO ticketDAO = new TicketDigitalDAO();

    public List<TicketResponse> abastecer(TicketRequest request) {
        Modalidade modalidade = converterModalidade(request.modalidade());

        if (request.quantidade() == null || request.quantidade() < 1 || request.quantidade() > MAXIMO_POR_ABASTECIMENTO) {
            throw new IllegalArgumentException("A quantidade deve ser entre 1 e " + MAXIMO_POR_ABASTECIMENTO);
        }

        List<TicketResponse> criados = new ArrayList<>();
        for (int i = 0; i < request.quantidade(); i++) {
            TicketDigital ticket = new TicketDigital(UUID.randomUUID().toString(), modalidade);
            ticketDAO.cadastrarTicket(ticket);
            criados.add(TicketMapper.toDTO(ticket));
        }
        return criados;
    }

    public List<TicketResponse> listar(String status, String modalidade) {
        StatusTicket filtroStatus = null;
        if (status != null) {
            filtroStatus = converterStatus(status);
        }

        Modalidade filtroModalidade = null;
        if (modalidade != null) {
            filtroModalidade = converterModalidadeFiltro(modalidade);
        }

        List<TicketResponse> resposta = new ArrayList<>();
        for (TicketDigital ticket : ticketDAO.listar(filtroStatus, filtroModalidade)) {
            resposta.add(TicketMapper.toDTO(ticket));
        }
        return resposta;
    }

    public TicketResponse buscarPorId(String id) {
        return TicketMapper.toDTO(buscarExistente(id));
    }

    public EstoqueResponse consultarEstoque() {
        return new EstoqueResponse(
                ticketDAO.contarPorStatus(StatusTicket.DISPONIVEL),
                ticketDAO.contarPorStatus(StatusTicket.RESERVADO),
                ticketDAO.contarPorStatus(StatusTicket.UTILIZADO),
                ticketDAO.contarPorStatus(StatusTicket.EXPIRADO)
        );
    }

    public TicketResponse marcarUso(String id) {
        TicketDigital ticket = buscarExistente(id);

        if (ticket.getStatus() != StatusTicket.RESERVADO) {
            throw new RegraNegocioException("Só um ticket RESERVADO pode ser marcado como utilizado (status atual: " + ticket.getStatus() + ")");
        }
        verificarValidade(ticket);

        ticket.confirmarUsoTicket();
        ticketDAO.alterarStatus(id, ticket.getStatus());
        return TicketMapper.toDTO(ticket);
    }

    public void excluir(String id) {
        TicketDigital ticket = buscarExistente(id);

        if (ticket.getStatus() != StatusTicket.DISPONIVEL) {
            throw new RegraNegocioException("Só é possível excluir tickets DISPONIVEL (status atual: " + ticket.getStatus() + ")");
        }
        ticketDAO.excluirTicket(id);
    }

    public TicketDigital buscarExistente(String id) {
        TicketDigital ticket = ticketDAO.buscarPorId(id);
        if (ticket == null) {
            throw new RecursoNaoEncontradoException("Ticket não encontrado");
        }
        return ticket;
    }

    public void verificarValidade(TicketDigital ticket) {
        if (ticket.getDataExpiracao().isBefore(LocalDate.now())) {
            ticketDAO.alterarStatus(ticket.getId(), StatusTicket.EXPIRADO);
            throw new RegraNegocioException("Ticket expirado em " + ticket.getDataExpiracao());
        }
    }

    public Modalidade converterModalidade(String modalidade) {
        Modalidade convertida = converterModalidadeFiltro(modalidade);
        if (convertida == Modalidade.ONIBUS) {
            throw new IllegalArgumentException("Ônibus não está disponível: use METRO ou TREM");
        }
        return convertida;
    }

    private Modalidade converterModalidadeFiltro(String modalidade) {
        if (modalidade == null) {
            throw new IllegalArgumentException("A modalidade é obrigatória (METRO ou TREM)");
        }
        try {
            return Modalidade.valueOf(modalidade.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Modalidade inválida: use METRO ou TREM");
        }
    }

    private StatusTicket converterStatus(String status) {
        try {
            return StatusTicket.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Status inválido: use DISPONIVEL, RESERVADO, UTILIZADO ou EXPIRADO");
        }
    }
}
