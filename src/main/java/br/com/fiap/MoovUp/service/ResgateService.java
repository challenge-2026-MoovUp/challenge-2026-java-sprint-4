package br.com.fiap.MoovUp.service;

import br.com.fiap.MoovUp.dao.TicketDigitalDAO;
import br.com.fiap.MoovUp.dao.UsuarioDAO;
import br.com.fiap.MoovUp.dto.ResgateRequest;
import br.com.fiap.MoovUp.dto.ResgateResponse;
import br.com.fiap.MoovUp.dto.TicketResponse;
import br.com.fiap.MoovUp.enums.FormaRecebimento;
import br.com.fiap.MoovUp.enums.Modalidade;
import br.com.fiap.MoovUp.exception.RegraNegocioException;
import br.com.fiap.MoovUp.mapper.TicketMapper;
import br.com.fiap.MoovUp.models.TicketDigital;
import br.com.fiap.MoovUp.models.Usuario;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResgateService {

    private final TicketDigitalDAO ticketDAO = new TicketDigitalDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final UsuarioService usuarioService;
    private final TicketService ticketService;

    public ResgateService(UsuarioService usuarioService, TicketService ticketService) {
        this.usuarioService = usuarioService;
        this.ticketService = ticketService;
    }

    public ResgateResponse resgatar(String idUsuario, ResgateRequest request) {
        Usuario usuario = usuarioService.buscarAtivo(idUsuario);
        Modalidade modalidade = ticketService.converterModalidade(request.modalidade());
        FormaRecebimento forma = converterForma(request.formaRecebimento());

        TicketDigital ticket = ticketDAO.buscarDisponivel(modalidade);
        if (ticket == null) {
            throw new RegraNegocioException("Não há tickets de " + modalidade + " no estoque");
        }

        if (usuario.getSaldoPontos() < ticket.getCustoEmPontos()) {
            throw new RegraNegocioException("Saldo insuficiente: o ticket custa " + ticket.getCustoEmPontos()
                    + " pontos e o usuário tem " + usuario.getSaldoPontos());
        }

        ticket.setFormaRecebimento(forma);
        ticket.resgatar(usuario);

        if (!ticketDAO.reservarTicket(ticket)) {
            throw new RegraNegocioException("Esse ticket acabou de ser resgatado por outra pessoa, tente novamente");
        }
        usuarioDAO.alterarUsuario(usuario);

        return new ResgateResponse(TicketMapper.toDTO(ticket), usuario.getSaldoPontos());
    }

    public List<TicketResponse> listar(String idUsuario) {
        usuarioService.buscarAtivo(idUsuario);

        List<TicketResponse> resposta = new ArrayList<>();
        for (TicketDigital ticket : ticketDAO.buscarPorUsuario(idUsuario)) {
            resposta.add(TicketMapper.toDTO(ticket));
        }
        return resposta;
    }

    private FormaRecebimento converterForma(String forma) {
        if (forma == null) {
            throw new IllegalArgumentException("A forma de recebimento é obrigatória (QR_CODE ou RECARGA)");
        }
        try {
            return FormaRecebimento.valueOf(forma.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Forma de recebimento inválida: use QR_CODE ou RECARGA");
        }
    }
}
