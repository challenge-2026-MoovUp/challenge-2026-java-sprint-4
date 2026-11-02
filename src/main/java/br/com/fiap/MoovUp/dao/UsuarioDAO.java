package br.com.fiap.MoovUp.dao;

import br.com.fiap.MoovUp.enums.StatusUsuario;
import br.com.fiap.MoovUp.models.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    private static final String SELECT_BASE =
            "SELECT ID_USUARIO, NOME_USUARIO, EMAIL, SENHA, SALDO_PONTOS, STATUS, DATA_CADASTRO FROM USUARIO_MOOVUP";

    public void cadastrarUsuario(Usuario usuario) {
        String sql = "INSERT INTO USUARIO_MOOVUP (ID_USUARIO, NOME_USUARIO, EMAIL, SENHA, SALDO_PONTOS, STATUS, DATA_CADASTRO) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, usuario.getId());
            ps.setString(2, usuario.getNome());
            ps.setString(3, usuario.getEmail());
            ps.setString(4, usuario.getSenha());
            ps.setInt(5, usuario.getSaldoPontos());
            ps.setString(6, usuario.getStatus().name());
            ps.setObject(7, usuario.getDataCadastro());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir usuário no banco: " + e.getMessage(), e);
        }
    }

    public Usuario buscarPorId(String id) {
        return buscarUm(SELECT_BASE + " WHERE ID_USUARIO = ?", id);
    }

    public Usuario buscarPorEmail(String email) {
        return buscarUm(SELECT_BASE + " WHERE UPPER(EMAIL) = UPPER(?)", email);
    }

    public Usuario buscarPorNome(String nome) {
        return buscarUm(SELECT_BASE + " WHERE UPPER(NOME_USUARIO) = UPPER(?)", nome);
    }

    public List<Usuario> listarTodos() {
        String sql = SELECT_BASE + " ORDER BY NOME_USUARIO";
        List<Usuario> usuarios = new ArrayList<>();

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                usuarios.add(mapearUsuario(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar usuários: " + e.getMessage(), e);
        }

        return usuarios;
    }

    public List<Usuario> buscarUsuariosPorStatus(StatusUsuario status) {
        return listarPorStatus(SELECT_BASE + " WHERE STATUS = ? ORDER BY NOME_USUARIO", status);
    }

    public List<Usuario> listarRanking() {
        return listarPorStatus(SELECT_BASE + " WHERE STATUS = ? ORDER BY SALDO_PONTOS DESC, NOME_USUARIO", StatusUsuario.A);
    }

    public void alterarUsuario(Usuario usuario) {
        String sql = "UPDATE USUARIO_MOOVUP SET NOME_USUARIO = ?, EMAIL = ?, SALDO_PONTOS = ?, STATUS = ? WHERE ID_USUARIO = ?";

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getEmail());
            ps.setInt(3, usuario.getSaldoPontos());
            ps.setString(4, usuario.getStatus().name());
            ps.setString(5, usuario.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao alterar usuário no banco: " + e.getMessage(), e);
        }
    }

    public void excluirUsuario(String id) {
        String sql = "DELETE FROM USUARIO_MOOVUP WHERE ID_USUARIO = ?";

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir usuário no banco: " + e.getMessage(), e);
        }
    }

    private Usuario buscarUm(String sql, String valor) {
        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, valor);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário: " + e.getMessage(), e);
        }

        return null;
    }

    private List<Usuario> listarPorStatus(String sql, StatusUsuario status) {
        List<Usuario> usuarios = new ArrayList<>();

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, status.name());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    usuarios.add(mapearUsuario(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar usuários por status: " + e.getMessage(), e);
        }

        return usuarios;
    }

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario(
                rs.getString("ID_USUARIO"),
                rs.getString("NOME_USUARIO"),
                rs.getString("EMAIL"),
                rs.getString("SENHA"),
                rs.getInt("SALDO_PONTOS")
        );
        usuario.setStatus(StatusUsuario.valueOf(rs.getString("STATUS")));
        usuario.setDataCadastro(rs.getObject("DATA_CADASTRO", LocalDate.class));
        return usuario;
    }
}
