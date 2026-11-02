package br.com.fiap.MoovUp.dao;

import br.com.fiap.MoovUp.models.Comentario;
import br.com.fiap.MoovUp.models.Postagem;
import br.com.fiap.MoovUp.models.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ComentarioDAO {

    // JOIN com o usuário para montar o objeto Usuario (id e nome) do autor
    private static final String SELECT_BASE =
            "SELECT C.COMENTARIO_ID, C.POSTAGEM_ID, C.ID_USUARIO, U.NOME_USUARIO, C.CONTEUDO, C.CURTIDAS, C.DATA_COMENTARIO " +
            "FROM COMENTARIO C JOIN USUARIO_MOOVUP U ON U.ID_USUARIO = C.ID_USUARIO";

    public void cadastrarComentario(Comentario comentario) {
        String sql = "INSERT INTO COMENTARIO (COMENTARIO_ID, POSTAGEM_ID, ID_USUARIO, CONTEUDO, CURTIDAS, DATA_COMENTARIO) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, comentario.getId());
            ps.setString(2, comentario.getPostagem().getId());
            ps.setString(3, comentario.getUsuario().getId());
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

    // Objetos relacionados só com o que a consulta traz: a postagem só com o id
    private Comentario mapearComentario(ResultSet rs) throws SQLException {
        Postagem postagem = new Postagem();
        postagem.setId(rs.getString("POSTAGEM_ID"));

        Usuario autor = new Usuario();
        autor.setId(rs.getString("ID_USUARIO"));
        autor.setNome(rs.getString("NOME_USUARIO"));

        return new Comentario(
                rs.getString("COMENTARIO_ID"),
                postagem,
                autor,
                rs.getString("CONTEUDO"),
                rs.getInt("CURTIDAS"),
                rs.getObject("DATA_COMENTARIO", LocalDateTime.class)
        );
    }
}
