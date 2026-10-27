package br.com.fiap.MoovUp.dao;

import br.com.fiap.MoovUp.models.Postagem;
import br.com.fiap.MoovUp.models.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PostagemDAO {
    private Connection conexao;

    public void cadastrarPostagem(Postagem postagem) {
        conexao = ConnectionFactory.obterConexao();
        PreparedStatement comandoSql = null;

        try {
            String sql = "INSERT INTO POSTAGEM (POSTAGEM_ID, TITULO, AUTOR, CONTEUDO, IMAGEM, VIDEO, TIPO_POSTAGEM, CURTIDAS, DATA_POSTAGEM) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            comandoSql = conexao.prepareStatement(sql);
            comandoSql.setInt(1, postagem.getId());
            comandoSql.setString(2, postagem.getTitulo());
            comandoSql.setString(3, postagem.getAutor());
            comandoSql.setString(4, postagem.getConteudo());
            comandoSql.setString(5, postagem.getImagem());
            comandoSql.setString(6, postagem.getVideo());
            comandoSql.setString(7, postagem.getTipoPostagem().name());
            comandoSql.setInt(8, postagem.getCurtidas());
            comandoSql.setObject(9, postagem.getDataPostagem());


            comandoSql.executeUpdate();
            System.out.println("Usuário cadastrado com sucesso!");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir usuário no banco: " + e.getMessage(), e);
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
