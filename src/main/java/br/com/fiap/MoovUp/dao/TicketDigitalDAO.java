package br.com.fiap.MoovUp.dao;

import br.com.fiap.MoovUp.enums.FormaRecebimento;
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

    private static final String SELECT_BASE =
            "SELECT T.ID_TICKET, T.MODALIDADE, T.STATUS, T.TARIFA_REAIS, T.CUSTO_EM_PONTOS, T.DATA_EXPIRACAO, " +
            "T.QR_CODE_HASH, T.ID_USUARIO, T.FORMA_RECEBIMENTO " +
            "FROM TICKET_DIGITAL T";

    // Ticket novo vai para o estoque: sem dono e sem forma de recebimento
    public void cadastrarTicket(TicketDigital ticket) {
        String sql = "INSERT INTO TICKET_DIGITAL (ID_TICKET, MODALIDADE, STATUS, TARIFA_REAIS, CUSTO_EM_PONTOS, DATA_EXPIRACAO) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, ticket.getId());
            ps.setString(2, ticket.getModalidade().name());
            ps.setString(3, ticket.getStatus().name());
            ps.setDouble(4, ticket.getTarifaReais());
            ps.setInt(5, ticket.getCustoEmPontos());
            ps.setObject(6, ticket.getDataExpiracao());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar ticket digital: " + e.getMessage(), e);
        }
    }

    public TicketDigital buscarPorId(String idTicket) {
        String sql = SELECT_BASE + " WHERE T.ID_TICKET = ?";

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, idTicket);

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

    // Filtros opcionais: quando status ou modalidade vêm null, não entram no WHERE
    public List<TicketDigital> listar(StatusTicket status, Modalidade modalidade) {
        String sql = SELECT_BASE + " WHERE 1 = 1";
        if (status != null) {
            sql += " AND T.STATUS = ?";
        }
        if (modalidade != null) {
            sql += " AND T.MODALIDADE = ?";
        }
        sql += " ORDER BY T.MODALIDADE, T.STATUS";

        List<TicketDigital> tickets = new ArrayList<>();

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            int posicao = 1;
            if (status != null) {
                ps.setString(posicao++, status.name());
            }
            if (modalidade != null) {
                ps.setString(posicao, modalidade.name());
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tickets.add(mapearTicket(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar tickets: " + e.getMessage(), e);
        }

        return tickets;
    }

    public List<TicketDigital> buscarPorUsuario(String idUsuario) {
        String sql = SELECT_BASE + " WHERE T.ID_USUARIO = ? ORDER BY T.DATA_EXPIRACAO DESC";
        List<TicketDigital> tickets = new ArrayList<>();

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, idUsuario);

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

    // Primeiro ticket DISPONIVEL da modalidade (FETCH FIRST = só uma linha)
    public TicketDigital buscarDisponivel(Modalidade modalidade) {
        String sql = SELECT_BASE + " WHERE T.STATUS = 'DISPONIVEL' AND T.MODALIDADE = ? FETCH FIRST 1 ROWS ONLY";

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, modalidade.name());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearTicket(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar ticket disponível: " + e.getMessage(), e);
        }

        return null;
    }

    public int contarPorStatus(StatusTicket status) {
        String sql = "SELECT COUNT(*) AS TOTAL FROM TICKET_DIGITAL WHERE STATUS = ?";

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, status.name());

            try (ResultSet rs = ps.executeQuery()) {
                rs.next(); // COUNT sempre devolve uma linha
                return rs.getInt("TOTAL");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao contar tickets: " + e.getMessage(), e);
        }
    }

    // Grava o resgate. O "AND STATUS = 'DISPONIVEL'" impede que dois usuários levem o mesmo ticket:
    // se outro resgate chegou antes, nenhuma linha é alterada e o método devolve false.
    public boolean reservarTicket(TicketDigital ticket) {
        String sql = "UPDATE TICKET_DIGITAL SET ID_USUARIO = ?, STATUS = ?, FORMA_RECEBIMENTO = ?, QR_CODE_HASH = ?, DATA_EXPIRACAO = ? " +
                "WHERE ID_TICKET = ? AND STATUS = 'DISPONIVEL'";

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, ticket.getIdUsuario());
            ps.setString(2, ticket.getStatus().name());
            ps.setString(3, ticket.getFormaRecebimento().name());
            ps.setString(4, ticket.getQrCodeHash());
            ps.setObject(5, ticket.getDataExpiracao());
            ps.setString(6, ticket.getId());

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao reservar ticket: " + e.getMessage(), e);
        }
    }

    public void alterarStatus(String idTicket, StatusTicket status) {
        String sql = "UPDATE TICKET_DIGITAL SET STATUS = ? WHERE ID_TICKET = ?";

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, status.name());
            ps.setString(2, idTicket);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao alterar o status do ticket: " + e.getMessage(), e);
        }
    }

    public void excluirTicket(String idTicket) {
        String sql = "DELETE FROM TICKET_DIGITAL WHERE ID_TICKET = ?";

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, idTicket);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir ticket: " + e.getMessage(), e);
        }
    }

    private TicketDigital mapearTicket(ResultSet rs) throws SQLException {
        TicketDigital ticket = new TicketDigital(rs.getString("ID_TICKET"), Modalidade.valueOf(rs.getString("MODALIDADE")));

        ticket.setStatus(StatusTicket.valueOf(rs.getString("STATUS")));
        ticket.setTarifaReais(rs.getDouble("TARIFA_REAIS"));
        ticket.setCustoEmPontos(rs.getInt("CUSTO_EM_PONTOS"));
        ticket.setDataExpiracao(rs.getObject("DATA_EXPIRACAO", LocalDate.class));
        ticket.setQrCodeHash(rs.getString("QR_CODE_HASH"));
        ticket.setIdUsuario(rs.getString("ID_USUARIO"));

        String forma = rs.getString("FORMA_RECEBIMENTO");
        if (forma != null) {
            ticket.setFormaRecebimento(FormaRecebimento.valueOf(forma));
        }
        return ticket;
    }
}
