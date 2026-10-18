package com.example.trabalho1pooii.dao;

import com.example.trabalho1pooii.database.Conexao;
import com.example.trabalho1pooii.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    // CREATE
    public void inserir(Usuario usuario) throws SQLException {

        String sql = """
                INSERT INTO usuario (nome, cpf, email, telefone)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getCpf());
            stmt.setString(3, usuario.getEmail());
            stmt.setString(4, usuario.getTelefone());

            stmt.executeUpdate();
        }
    }

    // READ
    public List<Usuario> listar() throws SQLException {

        String sql = """
                SELECT id, nome, cpf, email, telefone
                FROM usuario
                ORDER BY id
                """;

        List<Usuario> usuarios = new ArrayList<>();

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Usuario usuario = new Usuario(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("email"),
                        rs.getString("telefone")
                );

                usuarios.add(usuario);
            }
        }

        return usuarios;
    }

    // UPDATE
    public void atualizar(Usuario usuario) throws SQLException {

        String sql = """
                UPDATE usuario
                SET nome = ?, cpf = ?, email = ?, telefone = ?
                WHERE id = ?
                """;

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getCpf());
            stmt.setString(3, usuario.getEmail());
            stmt.setString(4, usuario.getTelefone());
            stmt.setInt(5, usuario.getId());

            stmt.executeUpdate();
        }
    }

    // DELETE
    public void excluir(int id) throws SQLException {

        String sql = "DELETE FROM usuario WHERE id = ?";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);

            stmt.executeUpdate();
        }
    }
}
