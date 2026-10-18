package com.example.trabalho1pooii.dao;

import com.example.trabalho1pooii.database.Conexao;
import com.example.trabalho1pooii.model.Livro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LivroDAO {

    // CREATE: cadastrar um livro
    public void inserir(Livro livro) throws SQLException {

        String sql = """
                INSERT INTO livro
                (titulo, autor, ano, genero, quantidade)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, livro.getTitulo());
            stmt.setString(2, livro.getAutor());
            stmt.setInt(3, livro.getAno());
            stmt.setString(4, livro.getGenero());
            stmt.setInt(5, livro.getQuantidade());

            stmt.executeUpdate();
        }
    }

    // READ: listar todos os livros
    public List<Livro> listar() throws SQLException {

        String sql = """
                SELECT id, titulo, autor, ano, genero, quantidade
                FROM livro
                ORDER BY id
                """;

        List<Livro> livros = new ArrayList<>();

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Livro livro = new Livro(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getInt("ano"),
                        rs.getString("genero"),
                        rs.getInt("quantidade")
                );

                livros.add(livro);
            }
        }

        return livros;
    }

    // UPDATE: atualizar um livro existente
    public void atualizar(Livro livro) throws SQLException {

        String sql = """
                UPDATE livro
                SET titulo = ?,
                    autor = ?,
                    ano = ?,
                    genero = ?,
                    quantidade = ?
                WHERE id = ?
                """;

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, livro.getTitulo());
            stmt.setString(2, livro.getAutor());
            stmt.setInt(3, livro.getAno());
            stmt.setString(4, livro.getGenero());
            stmt.setInt(5, livro.getQuantidade());
            stmt.setInt(6, livro.getId());

            stmt.executeUpdate();
        }
    }

    // DELETE: excluir um livro pelo ID
    public void excluir(int id) throws SQLException {

        String sql = "DELETE FROM livro WHERE id = ?";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);

            stmt.executeUpdate();
        }
    }
}