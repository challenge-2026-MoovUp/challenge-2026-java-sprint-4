package br.com.fiap.MoovUp.dao;

import br.com.fiap.MoovUp.enums.Modalidade;
import br.com.fiap.MoovUp.enums.StatusTicket;
import br.com.fiap.MoovUp.models.TicketDigital;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TicketDigitalDAO {

    public void cadastrarTicket(TicketDigital ticket) {
        validarTicket(ticket);

        String sql = "INSERT INTO TICKET_DIGITAL (ID_TICKET, MODALIDADE, STATUS, TARIFA_REAIS, CUSTO_EM_PONTOS, DATA_EXPIRACAO, QR_CODE_HASH) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, ticket.getId());
            ps.setString(2, ticket.getModalidade().name());
            ps.setString(3, ticket.getStatus().name());
            ps.setDouble(4, ticket.getTarifaReais());
            ps.setInt(5, ticket.getCustoEmPontos());
            ps.setObject(6, ticket.getDataExpiracao());
            ps.setString(7, ticket.getQrCodeHash());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar ticket digital: " + e.getMessage(), e);
        }
    }

    public TicketDigital buscarPorId(String idTicket) {
        if (idTicket == null || idTicket.trim().isEmpty()) {
            throw new IllegalArgumentException("ID do ticket inválido.");
        }

        String sql = "SELECT ID_TICKET, MODALIDADE, STATUS, TARIFA_REAIS, CUSTO_EM_PONTOS, DATA_EXPIRACAO, QR_CODE_HASH FROM TICKET_DIGITAL WHERE ID_TICKET = ?";

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, idTicket.trim());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearTicket(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar ticket por ID: " + e.getMessage(), e);
        }

        return null;
    }

    public List<TicketDigital> listarTodos() {
        String sql = "SELECT ID_TICKET, MODALIDADE, STATUS, TARIFA_REAIS, CUSTO_EM_PONTOS, DATA_EXPIRACAO, QR_CODE_HASH FROM TICKET_DIGITAL";
        List<TicketDigital> tickets = new ArrayList<>();

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                tickets.add(mapearTicket(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar tickets: " + e.getMessage(), e);
        }

        return tickets;
    }

    public List<TicketDigital> buscarPorUsuario(String idUsuario) {
        if (idUsuario == null || idUsuario.trim().isEmpty()) {
            throw new IllegalArgumentException("ID do usuário inválido.");
        }

        String sql = "SELECT ID_TICKET, MODALIDADE, STATUS, TARIFA_REAIS, CUSTO_EM_PONTOS, DATA_EXPIRACAO, QR_CODE_HASH FROM TICKET_DIGITAL WHERE ID_USUARIO = ?";
        List<TicketDigital> tickets = new ArrayList<>();

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, idUsuario.trim());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tickets.add(mapearTicket(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar tickets do usuário: " + e.getMessage(), e);
        }

        return tickets;
    }

    public void atualizarTicket(TicketDigital ticket) {
        validarTicket(ticket);

        String sql = "UPDATE TICKET_DIGITAL SET MODALIDADE = ?, STATUS = ?, TARIFA_REAIS = ?, CUSTO_EM_PONTOS = ?, DATA_EXPIRACAO = ?, QR_CODE_HASH = ? WHERE ID_TICKET = ?";

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, ticket.getModalidade().name());
            ps.setString(2, ticket.getStatus().name());
            ps.setDouble(3, ticket.getTarifaReais());
            ps.setInt(4, ticket.getCustoEmPontos());
            ps.setObject(5, ticket.getDataExpiracao());
            ps.setString(6, ticket.getQrCodeHash());
            ps.setString(7, ticket.getId());

            int linhasAfetadas = ps.executeUpdate();
            if (linhasAfetadas == 0) {
                throw new RuntimeException("Nenhum ticket encontrado com o ID informado.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar ticket: " + e.getMessage(), e);
        }
    }

    public void atribuirTicketAoUsuario(String idTicket, String idUsuario) {
        if (idTicket == null || idTicket.trim().isEmpty()) {
            throw new IllegalArgumentException("ID do ticket inválido.");
        }

        if (idUsuario == null || idUsuario.trim().isEmpty()) {
            throw new IllegalArgumentException("ID do usuário inválido.");
        }

        String sql = "UPDATE TICKET_DIGITAL SET ID_USUARIO = ?, STATUS = ? WHERE ID_TICKET = ? AND STATUS = ?";

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, idUsuario.trim());
            ps.setString(2, StatusTicket.RESERVADO.name());
            ps.setString(3, idTicket.trim());
            ps.setString(4, StatusTicket.DISPONIVEL.name());

            int linhasAfetadas = ps.executeUpdate();
            if (linhasAfetadas == 0) {
                throw new RuntimeException("Não foi possível atribuir o ticket ao usuário.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao associar ticket ao usuário: " + e.getMessage(), e);
        }
    }

    public void marcarComoUtilizado(String idTicket) {
        if (idTicket == null || idTicket.trim().isEmpty()) {
            throw new IllegalArgumentException("ID do ticket inválido.");
        }

        String sql = "UPDATE TICKET_DIGITAL SET STATUS = ? WHERE ID_TICKET = ? AND ID_USUARIO IS NOT NULL";

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, StatusTicket.UTILIZADO.name());
            ps.setString(2, idTicket.trim());

            int linhasAfetadas = ps.executeUpdate();
            if (linhasAfetadas == 0) {
                throw new RuntimeException("Nenhum ticket válido para uso foi encontrado com o ID informado.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar o status do ticket no banco: " + e.getMessage(), e);
        }
    }

    public void excluirTicket(String idTicket) {
        if (idTicket == null || idTicket.trim().isEmpty()) {
            throw new IllegalArgumentException("ID do ticket inválido.");
        }

        String sql = "DELETE FROM TICKET_DIGITAL WHERE ID_TICKET = ?";

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, idTicket.trim());
            int linhasAfetadas = ps.executeUpdate();

            if (linhasAfetadas == 0) {
                throw new RuntimeException("Nenhum ticket encontrado com o ID informado.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir ticket: " + e.getMessage(), e);
        }
    }

    private void validarTicket(TicketDigital ticket) {
        if (ticket == null) {
            throw new IllegalArgumentException("Ticket não pode ser nulo.");
        }

        if (ticket.getId() == null || ticket.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("O ID do ticket é obrigatório.");
        }

        if (ticket.getModalidade() == null) {
            throw new IllegalArgumentException("A modalidade do ticket é obrigatória.");
        }

        if (ticket.getStatus() == null) {
            throw new IllegalArgumentException("O status do ticket é obrigatório.");
        }

        if (ticket.getDataExpiracao() == null) {
            throw new IllegalArgumentException("A data de expiração do ticket é obrigatória.");
        }
    }

    private TicketDigital mapearTicket(ResultSet rs) throws SQLException {
        String id = rs.getString("ID_TICKET");
        Modalidade modalidade = Modalidade.valueOf(rs.getString("MODALIDADE"));
        TicketDigital ticket = new TicketDigital(id, modalidade);

        ticket.setStatus(StatusTicket.valueOf(rs.getString("STATUS")));
        ticket.setTarifaReais(rs.getDouble("TARIFA_REAIS"));
        ticket.setCustoEmPontos(rs.getInt("CUSTO_EM_PONTOS"));

        ticket.setDataExpiracao(rs.getObject("DATA_EXPIRACAO", LocalDate.class));

        ticket.setQrCodeHash(rs.getString("QR_CODE_HASH"));
        return ticket;
    }
}
