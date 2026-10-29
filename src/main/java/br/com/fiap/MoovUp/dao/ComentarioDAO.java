package br.com.fiap.MoovUp.dao;

import br.com.fiap.MoovUp.models.Comentario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ComentarioDAO {

    // JOIN com o usuário para trazer o nomeUsuario (não é coluna do COMENTARIO)
    private static final String SELECT_BASE =
            "SELECT C.COMENTARIO_ID, C.POSTAGEM_ID, C.ID_USUARIO, U.NOME_USUARIO, C.CONTEUDO, C.CURTIDAS, C.DATA_COMENTARIO " +
            "FROM COMENTARIO C JOIN USUARIO_MOOVUP U ON U.ID_USUARIO = C.ID_USUARIO";

    public void cadastrarComentario(Comentario comentario) {
        String sql = "INSERT INTO COMENTARIO (COMENTARIO_ID, POSTAGEM_ID, ID_USUARIO, CONTEUDO, CURTIDAS, DATA_COMENTARIO) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, comentario.getId());
            ps.setString(2, comentario.getIdPostagem());
            ps.setString(3, comentario.getIdUsuario());
            ps.setString(4, comentario.getConteudo());
            ps.setInt(5, comentario.getCurtidas());
            ps.setObject(6, comentario.getDataComentario());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir comentário no banco: " + e.getMessage(), e);
        }
    }

    public Comentario buscarPorId(String id) {
        String sql = SELECT_BASE + " WHERE C.COMENTARIO_ID = ?";

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearComentario(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar comentário por ID: " + e.getMessage(), e);
        }

        return null;
    }

    public List<Comentario> buscarPorPostagem(String idPostagem) {
        String sql = SELECT_BASE + " WHERE C.POSTAGEM_ID = ? ORDER BY C.DATA_COMENTARIO";
        List<Comentario> comentarios = new ArrayList<>();

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, idPostagem);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    comentarios.add(mapearComentario(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar comentários da postagem: " + e.getMessage(), e);
        }

        return comentarios;
    }

    public void excluirComentario(String id) {
        String sql = "DELETE FROM COMENTARIO WHERE COMENTARIO_ID = ?";

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir comentário: " + e.getMessage(), e);
        }
    }

    private Comentario mapearComentario(ResultSet rs) throws SQLException {
        Comentario comentario = new Comentario(
                rs.getString("COMENTARIO_ID"),
                rs.getString("POSTAGEM_ID"),
                rs.getString("ID_USUARIO"),
                rs.getString("CONTEUDO"),
                rs.getInt("CURTIDAS"),
                rs.getObject("DATA_COMENTARIO", LocalDateTime.class)
        );
        comentario.setNomeUsuario(rs.getString("NOME_USUARIO"));
        return comentario;
    }
}
