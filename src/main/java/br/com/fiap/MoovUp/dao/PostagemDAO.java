package br.com.fiap.MoovUp.dao;

import br.com.fiap.MoovUp.enums.TipoPostagem;
import br.com.fiap.MoovUp.models.Postagem;
import br.com.fiap.MoovUp.models.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PostagemDAO {

    // JOIN com o usuário para montar o objeto Usuario (id e nome) do autor.
    // STATUS = 'A': postagens de usuários excluídos (inativos) não aparecem
    private static final String SELECT_BASE =
            "SELECT P.POSTAGEM_ID, P.TITULO, P.ID_USUARIO, U.NOME_USUARIO, P.CONTEUDO, P.IMAGEM, P.VIDEO, " +
            "P.TIPO_POSTAGEM, P.CURTIDAS, P.DATA_POSTAGEM " +
            "FROM POSTAGEM P JOIN USUARIO_MOOVUP U ON U.ID_USUARIO = P.ID_USUARIO " +
            "WHERE U.STATUS = 'A'";

    public void cadastrarPostagem(Postagem postagem) {
        String sql = "INSERT INTO POSTAGEM (POSTAGEM_ID, TITULO, ID_USUARIO, CONTEUDO, IMAGEM, VIDEO, TIPO_POSTAGEM, CURTIDAS, DATA_POSTAGEM) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, postagem.getId());
            ps.setString(2, postagem.getTitulo());
            ps.setString(3, postagem.getUsuario().getId());
            ps.setString(4, postagem.getConteudo());
            ps.setString(5, postagem.getImagem());
            ps.setString(6, postagem.getVideo());
            ps.setString(7, postagem.getTipoPostagem().name());
            ps.setInt(8, postagem.getCurtidas());
            ps.setObject(9, postagem.getDataPostagem());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir postagem no banco: " + e.getMessage(), e);
        }
    }

    public Postagem buscarPorId(String id) {
        String sql = SELECT_BASE + " AND P.POSTAGEM_ID = ?";

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearPostagem(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar postagem por ID: " + e.getMessage(), e);
        }

        return null;
    }

    public List<Postagem> listarTodos() {
        String sql = SELECT_BASE + " ORDER BY P.DATA_POSTAGEM DESC";
        List<Postagem> postagens = new ArrayList<>();

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                postagens.add(mapearPostagem(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar postagens: " + e.getMessage(), e);
        }

        return postagens;
    }

    // Só título e conteúdo podem mudar: o tipo define os pontos ganhos na criação
    public void alterarPostagem(Postagem postagem) {
        String sql = "UPDATE POSTAGEM SET TITULO = ?, CONTEUDO = ? WHERE POSTAGEM_ID = ?";

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, postagem.getTitulo());
            ps.setString(2, postagem.getConteudo());
            ps.setString(3, postagem.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao alterar postagem: " + e.getMessage(), e);
        }
    }

    // NVL: postagens antigas podem ter CURTIDAS nulo
    public void somarCurtida(String id) {
        String sql = "UPDATE POSTAGEM SET CURTIDAS = NVL(CURTIDAS, 0) + 1 WHERE POSTAGEM_ID = ?";

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao somar curtida: " + e.getMessage(), e);
        }
    }

    public void excluirPostagem(String id) {
        String sql = "DELETE FROM POSTAGEM WHERE POSTAGEM_ID = ?";

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir postagem: " + e.getMessage(), e);
        }
    }

    // Como no projeto da professora: o objeto relacionado vem só com o que o JOIN traz
    private Postagem mapearPostagem(ResultSet rs) throws SQLException {
        Usuario autor = new Usuario();
        autor.setId(rs.getString("ID_USUARIO"));
        autor.setNome(rs.getString("NOME_USUARIO"));

        return new Postagem(
                rs.getString("POSTAGEM_ID"),
                rs.getString("TITULO"),
                autor,
                rs.getString("CONTEUDO"),
                rs.getString("IMAGEM"),
                rs.getString("VIDEO"),
                TipoPostagem.valueOf(rs.getString("TIPO_POSTAGEM")),
                rs.getInt("CURTIDAS"),
                rs.getObject("DATA_POSTAGEM", LocalDateTime.class)
        );
    }
}
