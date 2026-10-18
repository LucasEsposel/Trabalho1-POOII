package com.example.trabalho1pooii.dao;

import com.example.trabalho1pooii.database.Conexao;
import com.example.trabalho1pooii.model.Emprestimo;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmprestimoDAO {

    // CREATE: registrar empréstimo
    public void inserir(Emprestimo emprestimo) throws SQLException {

        String atualizarEstoque = """
                UPDATE livro
                SET quantidade = quantidade - 1
                WHERE id = ? AND quantidade > 0
                """;

        String inserirEmprestimo = """
                INSERT INTO emprestimo
                (usuario_id, livro_id, data_emprestimo, status)
                VALUES (?, ?, ?, 'ATIVO')
                """;

        try (Connection conexao = Conexao.conectar()) {

            conexao.setAutoCommit(false);

            try {
                // Verifica disponibilidade e reduz o estoque
                try (PreparedStatement stmt =
                             conexao.prepareStatement(atualizarEstoque)) {

                    stmt.setInt(1, emprestimo.getLivroId());

                    if (stmt.executeUpdate() == 0) {
                        throw new SQLException(
                                "Livro inexistente ou sem exemplares disponíveis."
                        );
                    }
                }

                // Registra o empréstimo
                try (PreparedStatement stmt =
                             conexao.prepareStatement(inserirEmprestimo)) {

                    stmt.setInt(1, emprestimo.getUsuarioId());
                    stmt.setInt(2, emprestimo.getLivroId());

                    stmt.setDate(
                            3,
                            Date.valueOf(emprestimo.getDataEmprestimo())
                    );

                    stmt.executeUpdate();
                }

                conexao.commit();

            } catch (SQLException e) {
                conexao.rollback();
                throw e;

            } finally {
                conexao.setAutoCommit(true);
            }
        }
    }

    // READ: listar empréstimos
    public List<Emprestimo> listar() throws SQLException {
        List<Emprestimo> lista = new ArrayList<>();

        String sql = """
        SELECT
            e.id,
            e.usuario_id,
            e.livro_id,
            e.data_emprestimo,
            e.data_devolucao,
            e.status,
            u.nome AS usuario_nome,
            l.titulo AS livro_titulo
        FROM emprestimo e
        JOIN usuario u ON e.usuario_id = u.id
        JOIN livro l ON e.livro_id = l.id
        ORDER BY e.id
        """;

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Emprestimo emprestimo = new Emprestimo(
                        rs.getInt("id"),
                        rs.getInt("usuario_id"),
                        rs.getInt("livro_id"),
                        rs.getDate("data_emprestimo").toLocalDate(),
                        rs.getDate("data_devolucao") != null
                                ? rs.getDate("data_devolucao").toLocalDate()
                                : null,
                        rs.getString("status")
                );

                emprestimo.setUsuarioNome(
                        rs.getString("usuario_nome")
                );

                emprestimo.setLivroTitulo(
                        rs.getString("livro_titulo")
                );

                lista.add(emprestimo);
            }
        }

        return lista;
    }

    // UPDATE: registrar devolução
    public void devolver(int emprestimoId) throws SQLException {

        try (Connection conexao = Conexao.conectar()) {

            conexao.setAutoCommit(false);

            try {
                int livroId;

                // Busca e bloqueia o empréstimo durante a operação
                String buscar = """
                        SELECT livro_id
                        FROM emprestimo
                        WHERE id = ? AND status = 'ATIVO'
                        FOR UPDATE
                        """;

                try (PreparedStatement stmt =
                             conexao.prepareStatement(buscar)) {

                    stmt.setInt(1, emprestimoId);

                    try (ResultSet rs = stmt.executeQuery()) {

                        if (!rs.next()) {
                            throw new SQLException(
                                    "Empréstimo inexistente ou já devolvido."
                            );
                        }

                        livroId = rs.getInt("livro_id");
                    }
                }

                // Marca o empréstimo como devolvido
                String atualizarEmprestimo = """
                        UPDATE emprestimo
                        SET status = 'DEVOLVIDO',
                            data_devolucao = CURRENT_DATE
                        WHERE id = ?
                        """;

                try (PreparedStatement stmt =
                             conexao.prepareStatement(atualizarEmprestimo)) {

                    stmt.setInt(1, emprestimoId);
                    stmt.executeUpdate();
                }

                // Devolve o exemplar ao estoque
                String atualizarEstoque = """
                        UPDATE livro
                        SET quantidade = quantidade + 1
                        WHERE id = ?
                        """;

                try (PreparedStatement stmt =
                             conexao.prepareStatement(atualizarEstoque)) {

                    stmt.setInt(1, livroId);
                    stmt.executeUpdate();
                }

                conexao.commit();

            } catch (SQLException e) {
                conexao.rollback();
                throw e;

            } finally {
                conexao.setAutoCommit(true);
            }
        }
    }
}