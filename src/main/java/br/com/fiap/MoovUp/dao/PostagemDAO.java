package br.com.fiap.MoovUp.dao;

import br.com.fiap.MoovUp.models.Postagem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PostagemDAO {
    private Connection conexao;

    public void cadastrarPostagem(Postagem postagem) {
        conexao = ConnectionFactory.obterConexao();
        PreparedStatement comandoSql = null;

        try {
            String sql = "INSERT INTO POSTAGEM (POSTAGEM_ID, TITULO, ID_USUARIO, CONTEUDO, IMAGEM, VIDEO, TIPO_POSTAGEM, CURTIDAS, DATA_POSTAGEM) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            comandoSql = conexao.prepareStatement(sql);
            comandoSql.setString(1, postagem.getId());
            comandoSql.setString(2, postagem.getTitulo());
            comandoSql.setString(3, postagem.getIdUsuario());
            comandoSql.setString(4, postagem.getConteudo());
            comandoSql.setString(5, postagem.getImagem());
            comandoSql.setString(6, postagem.getVideo());
            comandoSql.setString(7, postagem.getTipoPostagem().name());
            comandoSql.setInt(8, postagem.getCurtidas());
            comandoSql.setObject(9, postagem.getDataPostagem());

            comandoSql.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir postagem no banco: " + e.getMessage(), e);
        } finally {
            try {
                if (comandoSql != null) {
                    comandoSql.close();
                }
                if (conexao != null && !conexao.isClosed()) {
                    conexao.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao fechar conexão: " + e.getMessage(), e);
            }
        }
    }
}
