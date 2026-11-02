package br.com.fiap.MoovUp.dao;

import br.com.fiap.MoovUp.dto.ImpactoResumoResponse;
import br.com.fiap.MoovUp.enums.Modalidade;
import br.com.fiap.MoovUp.models.ImpactoAmbiental;
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

public class ImpactoAmbientalDAO {

    private static final String SELECT_BASE =
            "SELECT ID_IMPACTO, ID_USUARIO, ID_TICKET, ID_VIAGEM, MODALIDADE, CO2_EVITADO, KM_PERCORRIDOS, VIAGENS_REALIZADAS, DATA_CALCULO " +
            "FROM IMPACTO_AMBIENTAL_MOOVPASS";

    public void cadastrarImpactoAmbiental(ImpactoAmbiental impacto) {
        String sql = "INSERT INTO IMPACTO_AMBIENTAL_MOOVPASS (ID_IMPACTO, ID_USUARIO, ID_TICKET, ID_VIAGEM, MODALIDADE, CO2_EVITADO, KM_PERCORRIDOS, VIAGENS_REALIZADAS, DATA_CALCULO) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, impacto.getId());
            ps.setString(2, impacto.getUsuario().getId());
            ps.setString(3, impacto.getTicket().getId());
            ps.setString(4, impacto.getViagem() == null ? null : impacto.getViagem().getId());
            ps.setString(5, impacto.getModalidade().name());
            ps.setDouble(6, impacto.getCo2Evitado());
            ps.setDouble(7, impacto.getKmPercorridos());
            ps.setInt(8, impacto.getViagensRealizadas());
            ps.setObject(9, impacto.getDataCalculo());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar impacto ambiental: " + e.getMessage(), e);
        }
    }

    public ImpactoAmbiental buscarPorId(String id) {
        String sql = SELECT_BASE + " WHERE ID_IMPACTO = ?";

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearImpacto(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar impacto ambiental por ID: " + e.getMessage(), e);
        }

        return null;
    }

    public List<ImpactoAmbiental> buscarPorUsuario(String idUsuario) {
        String sql = SELECT_BASE + " WHERE ID_USUARIO = ? ORDER BY DATA_CALCULO DESC";
        List<ImpactoAmbiental> impactos = new ArrayList<>();

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    impactos.add(mapearImpacto(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar impactos do usuário: " + e.getMessage(), e);
        }

        return impactos;
    }

    public List<ImpactoAmbiental> listarTodos() {
        String sql = SELECT_BASE + " ORDER BY DATA_CALCULO DESC";
        List<ImpactoAmbiental> impactos = new ArrayList<>();

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                impactos.add(mapearImpacto(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar impactos ambientais: " + e.getMessage(), e);
        }

        return impactos;
    }

    public ImpactoResumoResponse resumirPorUsuario(String idUsuario) {
        String sql = "SELECT NVL(SUM(VIAGENS_REALIZADAS), 0) AS TOTAL_VIAGENS, NVL(SUM(KM_PERCORRIDOS), 0) AS TOTAL_KM, " +
                "NVL(SUM(CO2_EVITADO), 0) AS TOTAL_CO2 FROM IMPACTO_AMBIENTAL_MOOVPASS WHERE ID_USUARIO = ?";

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return new ImpactoResumoResponse(
                        idUsuario,
                        rs.getInt("TOTAL_VIAGENS"),
                        rs.getDouble("TOTAL_KM"),
                        rs.getDouble("TOTAL_CO2")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao resumir impactos do usuário: " + e.getMessage(), e);
        }
    }

    public void excluirImpactoAmbiental(String id) {
        String sql = "DELETE FROM IMPACTO_AMBIENTAL_MOOVPASS WHERE ID_IMPACTO = ?";

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir impacto ambiental: " + e.getMessage(), e);
        }
    }

    private ImpactoAmbiental mapearImpacto(ResultSet rs) throws SQLException {
        ImpactoAmbiental impacto = new ImpactoAmbiental();
        impacto.setId(rs.getString("ID_IMPACTO"));

        Usuario usuario = new Usuario();
        usuario.setId(rs.getString("ID_USUARIO"));
        impacto.setUsuario(usuario);

        TicketDigital ticket = new TicketDigital();
        ticket.setId(rs.getString("ID_TICKET"));
        impacto.setTicket(ticket);

        String idViagem = rs.getString("ID_VIAGEM");
        if (idViagem != null) {
            Viagem viagem = new Viagem();
            viagem.setId(idViagem);
            impacto.setViagem(viagem);
        }

        String modalidade = rs.getString("MODALIDADE");
        if (modalidade != null) {
            impacto.setModalidade(Modalidade.valueOf(modalidade));
        }

        impacto.setCo2Evitado(rs.getDouble("CO2_EVITADO"));
        impacto.setKmPercorridos(rs.getDouble("KM_PERCORRIDOS"));
        impacto.setViagensRealizadas(rs.getInt("VIAGENS_REALIZADAS"));
        impacto.setDataCalculo(rs.getObject("DATA_CALCULO", LocalDateTime.class));
        return impacto;
    }
}
