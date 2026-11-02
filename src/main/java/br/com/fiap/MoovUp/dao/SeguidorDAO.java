package br.com.fiap.MoovUp.dao;

import br.com.fiap.MoovUp.models.Seguidor;
import br.com.fiap.MoovUp.models.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SeguidorDAO {

    public void cadastrarSeguidor(Seguidor seguidor) {
        String sql = "INSERT INTO SEGUIDOR (ID_USUARIO, ID_SEGUIDO, DATA_SEGUIDO) VALUES (?, ?, ?)";

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, seguidor.getUsuario().getId());
            ps.setString(2, seguidor.getSeguido().getId());
            ps.setObject(3, seguidor.getDataSeguido());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar seguidor: " + e.getMessage(), e);
        }
    }

    public boolean existeSeguidor(String idUsuario, String idSeguido) {
        String sql = "SELECT 1 FROM SEGUIDOR WHERE ID_USUARIO = ? AND ID_SEGUIDO = ?";

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, idUsuario);
            ps.setString(2, idSeguido);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar seguidor: " + e.getMessage(), e);
        }
    }

    public List<Seguidor> listarSeguidos(String idUsuario) {
        String sql = "SELECT S.ID_USUARIO, S.ID_SEGUIDO, S.DATA_SEGUIDO, U.NOME_USUARIO " +
                "FROM SEGUIDOR S JOIN USUARIO_MOOVUP U ON U.ID_USUARIO = S.ID_SEGUIDO " +
                "WHERE S.ID_USUARIO = ? AND U.STATUS = 'A' " +
                "ORDER BY U.NOME_USUARIO";
        List<Seguidor> seguidos = new ArrayList<>();

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    seguidos.add(mapearSeguidor(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar quem o usuário segue: " + e.getMessage(), e);
        }

        return seguidos;
    }

    public void excluirSeguidor(String idUsuario, String idSeguido) {
        String sql = "DELETE FROM SEGUIDOR WHERE ID_USUARIO = ? AND ID_SEGUIDO = ?";

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, idUsuario);
            ps.setString(2, idSeguido);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deixar de seguir: " + e.getMessage(), e);
        }
    }

    private Seguidor mapearSeguidor(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getString("ID_USUARIO"));

        Usuario seguido = new Usuario();
        seguido.setId(rs.getString("ID_SEGUIDO"));
        seguido.setNome(rs.getString("NOME_USUARIO"));

        Seguidor seguidor = new Seguidor(usuario, seguido);
        seguidor.setDataSeguido(rs.getObject("DATA_SEGUIDO", LocalDateTime.class));
        return seguidor;
    }
}
