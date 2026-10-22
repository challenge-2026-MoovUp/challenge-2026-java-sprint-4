package br.com.fiap.MoovUp.dao;

import br.com.fiap.MoovUp.enums.StatusUsuario;
import br.com.fiap.MoovUp.models.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    private Connection conexao;

    public void cadastrarUsuario(Usuario usuario) {
        validarUsuario(usuario);

        conexao = ConnectionFactory.obterConexao();
        PreparedStatement comandoSql = null;

        try {
            String sql = "INSERT INTO USUARIO_MOOVUP (ID_USUARIO, NOME_USUARIO, EMAIL, SALDO_PONTOS, STATUS, DATA_CADASTRO) VALUES (?, ?, ?, ?, ?, ?)";

            comandoSql = conexao.prepareStatement(sql);
            comandoSql.setString(1, usuario.getId());
            comandoSql.setString(2, usuario.getNome());
            comandoSql.setString(3, usuario.getEmail());
            comandoSql.setInt(4, usuario.getSaldoPontos());
            comandoSql.setString(5, usuario.getStatus().name());
            comandoSql.setDate(6, new java.sql.Date(usuario.getDataCadastro().getTime()));

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

    public Usuario buscarPorNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome informado é inválido.");
        }

        String sql = "SELECT ID_USUARIO, NOME_USUARIO, EMAIL, SALDO_PONTOS, STATUS, DATA_CADASTRO FROM USUARIO_MOOVUP WHERE UPPER(NOME_USUARIO) = UPPER(?)";
        Usuario usuario = null;

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, nome.trim());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario(
                            rs.getString("ID_USUARIO"),
                            rs.getString("NOME_USUARIO"),
                            rs.getString("EMAIL"),
                            rs.getInt("SALDO_PONTOS")
                    );

                    usuario.setStatus(StatusUsuario.valueOf(rs.getString("STATUS")));
                    usuario.setDataCadastro(rs.getDate("DATA_CADASTRO"));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário por nome: " + e.getMessage(), e);
        }

        return usuario;
    }

    public Usuario buscarPorEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("O email informado é inválido.");
        }

        String sql = "SELECT ID_USUARIO, NOME_USUARIO, EMAIL, SALDO_PONTOS, STATUS, DATA_CADASTRO FROM USUARIO_MOOVUP WHERE UPPER(EMAIL) = UPPER(?)";
        Usuario usuario = null;

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, email.trim());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario(
                            rs.getString("ID_USUARIO"),
                            rs.getString("NOME_USUARIO"),
                            rs.getString("EMAIL"),
                            rs.getInt("SALDO_PONTOS")
                    );

                    usuario.setStatus(StatusUsuario.valueOf(rs.getString("STATUS")));
                    usuario.setDataCadastro(rs.getDate("DATA_CADASTRO"));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário por email: " + e.getMessage(), e);
        }

        return usuario;
    }

    public List<Usuario> listarTodos() {
        String sql = "SELECT ID_USUARIO, NOME_USUARIO, EMAIL, SALDO_PONTOS, STATUS, DATA_CADASTRO FROM USUARIO_MOOVUP ORDER BY NOME_USUARIO";
        List<Usuario> usuarios = new ArrayList<>();

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Usuario usuario = new Usuario(
                        rs.getString("ID_USUARIO"),
                        rs.getString("NOME_USUARIO"),
                        rs.getString("EMAIL"),
                        rs.getInt("SALDO_PONTOS")
                );
                usuario.setStatus(StatusUsuario.valueOf(rs.getString("STATUS")));
                usuario.setDataCadastro(rs.getDate("DATA_CADASTRO"));
                usuarios.add(usuario);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar usuários: " + e.getMessage(), e);
        }

        return usuarios;
    }

    public List<Usuario> buscarUsuariosPorStatus(StatusUsuario status) {
        if (status == null) {
            throw new IllegalArgumentException("O status informado é inválido.");
        }

        String sql = "SELECT ID_USUARIO, NOME_USUARIO, EMAIL, SALDO_PONTOS, STATUS, DATA_CADASTRO FROM USUARIO_MOOVUP WHERE STATUS = ?";
        List<Usuario> usuarios = new ArrayList<>();

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement pr = conexao.prepareStatement(sql)) {

            pr.setString(1, status.name());

            try (ResultSet rs = pr.executeQuery()) {
                while (rs.next()) {
                    Usuario usuario = new Usuario(
                            rs.getString("ID_USUARIO"),
                            rs.getString("NOME_USUARIO"),
                            rs.getString("EMAIL"),
                            rs.getInt("SALDO_PONTOS")
                    );
                    usuario.setStatus(StatusUsuario.valueOf(rs.getString("STATUS")));
                    usuario.setDataCadastro(rs.getDate("DATA_CADASTRO"));
                    usuarios.add(usuario);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuários por status: " + e.getMessage(), e);
        }

        return usuarios;
    }

    private void validarUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não pode ser nulo.");
        }

        if (usuario.getId() == null || usuario.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("O ID do usuário é obrigatório.");
        }

        if (usuario.getNome() == null || usuario.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do usuário é obrigatório.");
        }

        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("O email do usuário é obrigatório.");
        }

        if (!usuario.getEmail().contains("@") || !usuario.getEmail().contains(".")) {
            throw new IllegalArgumentException("Email inválido. Informe um email no formato correto.");
        }

        if (usuario.getSaldoPontos() < 0) {
            throw new IllegalArgumentException("O saldo de pontos não pode ser negativo.");
        }
    }

    public Usuario buscarPorId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("O ID informado é inválido.");
        }

        String sql = "SELECT ID_USUARIO, NOME_USUARIO, EMAIL, SALDO_PONTOS, STATUS, DATA_CADASTRO FROM USUARIO_MOOVUP WHERE ID_USUARIO = ?";
        Usuario usuario = null;

        try (Connection conexao = ConnectionFactory.obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, id.trim());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario(
                            rs.getString("ID_USUARIO"),
                            rs.getString("NOME_USUARIO"),
                            rs.getString("EMAIL"),
                            rs.getInt("SALDO_PONTOS")
                    );
                    usuario.setStatus(StatusUsuario.valueOf(rs.getString("STATUS")));
                    usuario.setDataCadastro(rs.getDate("DATA_CADASTRO"));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário por ID: " + e.getMessage(), e);
        }

        return usuario;
    }

    public void alterarUsuario(Usuario usuario) {
        validarUsuario(usuario);

        conexao = ConnectionFactory.obterConexao();
        PreparedStatement comandoSql = null;

        try {
            String sql = "UPDATE USUARIO_MOOVUP SET NOME_USUARIO = ?, EMAIL = ?, SALDO_PONTOS = ?, STATUS = ? WHERE ID_USUARIO = ?";

            comandoSql = conexao.prepareStatement(sql);
            comandoSql.setString(1, usuario.getNome());
            comandoSql.setString(2, usuario.getEmail());
            comandoSql.setInt(3, usuario.getSaldoPontos());
            comandoSql.setString(4, usuario.getStatus().name());
            comandoSql.setString(5, usuario.getId());

            int linhasAfetadas = comandoSql.executeUpdate();
            if (linhasAfetadas == 0) {
                throw new RuntimeException("Nenhum usuário encontrado com o ID informado.");
            }

            System.out.println("Usuário alterado com sucesso!");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao alterar usuário no banco: " + e.getMessage(), e);
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

    public void excluirUsuario(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("O ID informado é inválido.");
        }

        conexao = ConnectionFactory.obterConexao();
        PreparedStatement comandoSql = null;

        try {
            String sql = "DELETE FROM USUARIO_MOOVUP WHERE ID_USUARIO = ?";
            comandoSql = conexao.prepareStatement(sql);
            comandoSql.setString(1, id.trim());

            int linhasAfetadas = comandoSql.executeUpdate();
            if (linhasAfetadas == 0) {
                throw new RuntimeException("Nenhum usuário encontrado com o ID informado.");
            }

            System.out.println("Usuário excluído com sucesso!");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir usuário no banco: " + e.getMessage(), e);
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
