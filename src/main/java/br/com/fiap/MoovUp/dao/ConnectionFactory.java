package br.com.fiap.MoovUp.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    public static Connection obterConexao(){
        Connection conexao = null;
        try {
            conexao = DriverManager.getConnection(
                    "jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL",
                    "RM571985", "130907");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return conexao;
    }
}
