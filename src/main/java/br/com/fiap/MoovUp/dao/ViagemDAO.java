package br.com.fiap.MoovUp.dao;

import br.com.fiap.MoovUp.enums.Estacao;
import br.com.fiap.MoovUp.enums.Linha;
import br.com.fiap.MoovUp.models.TicketDigital;
import br.com.fiap.MoovUp.models.Usuario;
import br.com.fiap.MoovUp.models.Viagem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ViagemDAO {

    public void cadastrarViagem(Viagem viagem) {
        String sql = "INSERT INTO VIAGEM (ID_VIAGEM, ID_USUARIO, ID_TICKET, LINHA, ESTACAO_EMBARQUE, PONTOS_GERADOS, DATA_VIAGEM) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, viagem.getId());
            ps.setString(2, viagem.getUsuario().getId());
            ps.setString(3, viagem.getTicket().getId());
            ps.setString(4, viagem.getLinha().name());
            ps.setString(5, viagem.getEstacaoEmbarque().name());
            ps.setInt(6, viagem.getPontosGerados());
            ps.setObject(7, viagem.getDataViagem());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar viagem: " + e.getMessage(), e);
        }
    }

    public List<Viagem> buscarPorUsuario(String idUsuario) {
        String sql = "SELECT ID_VIAGEM, ID_USUARIO, ID_TICKET, LINHA, ESTACAO_EMBARQUE, PONTOS_GERADOS, DATA_VIAGEM " +
                "FROM VIAGEM WHERE ID_USUARIO = ? ORDER BY DATA_VIAGEM DESC";
        List<Viagem> viagens = new ArrayList<>();

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    viagens.add(mapearViagem(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar viagens do usuário: " + e.getMessage(), e);
        }

        return viagens;
    }

    // Usado para a regra de no máximo 2 viagens por ticket
    public int contarPorTicket(String idTicket) {
        String sql = "SELECT COUNT(*) AS TOTAL FROM VIAGEM WHERE ID_TICKET = ?";

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, idTicket);

            try (ResultSet rs = ps.executeQuery()) {
                rs.next(); // COUNT sempre devolve uma linha
                return rs.getInt("TOTAL");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao contar viagens do ticket: " + e.getMessage(), e);
        }
    }

    // Usuário e ticket só com o id (é o que a tabela VIAGEM guarda)
    private Viagem mapearViagem(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getString("ID_USUARIO"));

        TicketDigital ticket = new TicketDigital();
        ticket.setId(rs.getString("ID_TICKET"));

        return new Viagem(
                rs.getString("ID_VIAGEM"),
                usuario,
                ticket,
                Linha.valueOf(rs.getString("LINHA")),
                Estacao.valueOf(rs.getString("ESTACAO_EMBARQUE")),
                rs.getInt("PONTOS_GERADOS"),
                rs.getObject("DATA_VIAGEM", LocalDateTime.class)
        );
    }
}
