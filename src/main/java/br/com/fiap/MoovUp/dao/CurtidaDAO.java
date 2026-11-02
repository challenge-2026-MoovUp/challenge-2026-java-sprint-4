package br.com.fiap.MoovUp.dao;

import br.com.fiap.MoovUp.models.Curtida;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CurtidaDAO {

    public void cadastrarCurtida(Curtida curtida) {
        String sql = "INSERT INTO CURTIDA (ID_USUARIO, POSTAGEM_ID, DATA_CURTIDA) VALUES (?, ?, ?)";

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, curtida.getUsuario().getId());
            ps.setString(2, curtida.getPostagem().getId());
            ps.setObject(3, curtida.getDataCurtida());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir curtida no banco: " + e.getMessage(), e);
        }
    }

    public boolean existeCurtida(String idUsuario, String idPostagem) {
        String sql = "SELECT 1 FROM CURTIDA WHERE ID_USUARIO = ? AND POSTAGEM_ID = ?";

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, idUsuario);
            ps.setString(2, idPostagem);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar curtida: " + e.getMessage(), e);
        }
    }
}
