package br.com.fiap.MoovUp.dao;

import br.com.fiap.MoovUp.dto.ImpactoResumoResponse;
import br.com.fiap.MoovUp.enums.Modalidade;
import br.com.fiap.MoovUp.models.ImpactoAmbiental;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ImpactoAmbientalDAO {

    public void cadastrarImpactoAmbiental(ImpactoAmbiental impacto) {
        validarImpactoAmbiental(impacto);

        String sql = "INSERT INTO IMPACTO_AMBIENTAL_MOOVPASS (ID_IMPACTO, ID_USUARIO, ID_TICKET, MODALIDADE, CO2_EVITADO, KM_PERCORRIDOS, VIAGENS_REALIZADAS, DATA_CALCULO) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, impacto.getId());
            ps.setString(2, impacto.getIdUsuario());
            ps.setString(3, impacto.getIdTicket());
            ps.setString(4, impacto.getModalidade() != null ? impacto.getModalidade().name() : null);
            ps.setDouble(5, impacto.getCo2Evitado());
            ps.setDouble(6, impacto.getKmPercorridos());
            ps.setInt(7, impacto.getViagensRealizadas());
            ps.setObject(8, impacto.getDataCalculo());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar impacto ambiental: " + e.getMessage(), e);
        }
    }

    public void registrarImpactoDoTicket(String idUsuario, String idTicket, Modalidade modalidade) {
        if (idUsuario == null || idUsuario.trim().isEmpty()) {
            throw new IllegalArgumentException("ID do usuário inválido.");
        }

        if (idTicket == null || idTicket.trim().isEmpty()) {
            throw new IllegalArgumentException("ID do ticket inválido.");
        }

        if (buscarPorTicketUsuario(idUsuario, idTicket) != null) {
            return;
        }

        ImpactoAmbiental impacto = ImpactoAmbiental.criarParaTicket(idUsuario, idTicket, modalidade);
        cadastrarImpactoAmbiental(impacto);
    }

    public ImpactoAmbiental buscarPorId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("O ID informado é inválido.");
        }

        String sql = "SELECT ID_IMPACTO, ID_USUARIO, ID_TICKET, MODALIDADE, CO2_EVITADO, KM_PERCORRIDOS, VIAGENS_REALIZADAS, DATA_CALCULO FROM IMPACTO_AMBIENTAL_MOOVPASS WHERE ID_IMPACTO = ?";

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, id.trim());

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
        if (idUsuario == null || idUsuario.trim().isEmpty()) {
            throw new IllegalArgumentException("ID do usuário inválido.");
        }

        String sql = "SELECT ID_IMPACTO, ID_USUARIO, ID_TICKET, MODALIDADE, CO2_EVITADO, KM_PERCORRIDOS, VIAGENS_REALIZADAS, DATA_CALCULO FROM IMPACTO_AMBIENTAL_MOOVPASS WHERE ID_USUARIO = ? ORDER BY DATA_CALCULO DESC";
        List<ImpactoAmbiental> impactos = new ArrayList<>();

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, idUsuario.trim());

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

    public ImpactoAmbiental buscarPorTicketUsuario(String idUsuario, String idTicket) {
        if (idUsuario == null || idUsuario.trim().isEmpty()) {
            throw new IllegalArgumentException("ID do usuário inválido.");
        }

        if (idTicket == null || idTicket.trim().isEmpty()) {
            throw new IllegalArgumentException("ID do ticket inválido.");
        }

        String sql = "SELECT ID_IMPACTO, ID_USUARIO, ID_TICKET, MODALIDADE, CO2_EVITADO, KM_PERCORRIDOS, VIAGENS_REALIZADAS, DATA_CALCULO FROM IMPACTO_AMBIENTAL_MOOVPASS WHERE ID_USUARIO = ? AND ID_TICKET = ?";

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, idUsuario.trim());
            ps.setString(2, idTicket.trim());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearImpacto(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar impacto por ticket do usuário: " + e.getMessage(), e);
        }

        return null;
    }

    public List<ImpactoAmbiental> listarTodos() {
        String sql = "SELECT ID_IMPACTO, ID_USUARIO, ID_TICKET, MODALIDADE, CO2_EVITADO, KM_PERCORRIDOS, VIAGENS_REALIZADAS, DATA_CALCULO FROM IMPACTO_AMBIENTAL_MOOVPASS";
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

    public void alterarImpactoAmbiental(ImpactoAmbiental impacto) {
        validarImpactoAmbiental(impacto);

        String sql = "UPDATE IMPACTO_AMBIENTAL_MOOVPASS SET ID_USUARIO = ?, ID_TICKET = ?, MODALIDADE = ?, CO2_EVITADO = ?, KM_PERCORRIDOS = ?, VIAGENS_REALIZADAS = ?, DATA_CALCULO = ? WHERE ID_IMPACTO = ?";

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, impacto.getIdUsuario());
            ps.setString(2, impacto.getIdTicket());
            ps.setString(3, impacto.getModalidade() != null ? impacto.getModalidade().name() : null);
            ps.setDouble(4, impacto.getCo2Evitado());
            ps.setDouble(5, impacto.getKmPercorridos());
            ps.setInt(6, impacto.getViagensRealizadas());
            ps.setObject(7, impacto.getDataCalculo());
            ps.setString(8, impacto.getId());

            int linhasAfetadas = ps.executeUpdate();
            if (linhasAfetadas == 0) {
                throw new RuntimeException("Nenhum impacto ambiental encontrado com o ID informado.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao alterar impacto ambiental no banco: " + e.getMessage(), e);
        }
    }

    public void excluirImpactoAmbiental(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("O ID informado é inválido.");
        }

        String sql = "DELETE FROM IMPACTO_AMBIENTAL_MOOVPASS WHERE ID_IMPACTO = ?";

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, id.trim());

            int linhasAfetadas = ps.executeUpdate();
            if (linhasAfetadas == 0) {
                throw new RuntimeException("Nenhum impacto ambiental encontrado com o ID informado.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir impacto ambiental: " + e.getMessage(), e);
        }
    }

    private void validarImpactoAmbiental(ImpactoAmbiental impacto) {
        if (impacto == null) {
            throw new IllegalArgumentException("Impacto ambiental não pode ser nulo.");
        }

        if (impacto.getId() == null || impacto.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("O ID do impacto ambiental é obrigatório.");
        }

        if (impacto.getIdUsuario() == null || impacto.getIdUsuario().trim().isEmpty()) {
            throw new IllegalArgumentException("O usuário do impacto ambiental é obrigatório.");
        }

        if (impacto.getIdTicket() == null || impacto.getIdTicket().trim().isEmpty()) {
            throw new IllegalArgumentException("O ticket do impacto ambiental é obrigatório.");
        }

        if (impacto.getDataCalculo() == null) {
            throw new IllegalArgumentException("A data de cálculo é obrigatória.");
        }

        if (impacto.getKmPercorridos() < 0) {
            throw new IllegalArgumentException("Os quilômetros percorridos não podem ser negativos.");
        }

        if (impacto.getViagensRealizadas() < 0) {
            throw new IllegalArgumentException("A quantidade de viagens não pode ser negativa.");
        }

        if (impacto.getCo2Evitado() < 0) {
            throw new IllegalArgumentException("O CO₂ evitado não pode ser negativo.");
        }
    }

    // Resumo do GET /usuarios/{id}/impacto. NVL troca o null do SUM (usuário sem impactos) por 0
    public ImpactoResumoResponse resumirPorUsuario(String idUsuario) {
        String sql = "SELECT NVL(SUM(VIAGENS_REALIZADAS), 0) AS TOTAL_VIAGENS, NVL(SUM(KM_PERCORRIDOS), 0) AS TOTAL_KM, " +
                "NVL(SUM(CO2_EVITADO), 0) AS TOTAL_CO2 FROM IMPACTO_AMBIENTAL_MOOVPASS WHERE ID_USUARIO = ?";

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                rs.next(); // SUM sempre devolve uma linha
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

    private ImpactoAmbiental mapearImpacto(ResultSet rs) throws SQLException {
        ImpactoAmbiental impacto = new ImpactoAmbiental();
        impacto.setId(rs.getString("ID_IMPACTO"));
        impacto.setIdUsuario(rs.getString("ID_USUARIO"));
        impacto.setIdTicket(rs.getString("ID_TICKET"));

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
