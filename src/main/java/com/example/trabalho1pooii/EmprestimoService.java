package com.example.trabalho1pooii;

import com.example.trabalho1pooii.dao.EmprestimoDAO;
import com.example.trabalho1pooii.dao.LivroDAO;
import com.example.trabalho1pooii.dao.UsuarioDAO;
import com.example.trabalho1pooii.model.Emprestimo;
import com.example.trabalho1pooii.model.Livro;
import com.example.trabalho1pooii.model.Usuario;
import com.example.trabalho1pooii.strategy.PoliticaPrazo;
import java.time.LocalDate;

import java.sql.SQLException;
import java.util.List;

public class EmprestimoService {

    private final EmprestimoDAO emprestimoDAO = new EmprestimoDAO();
    private final LivroDAO livroDAO = new LivroDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public List<Emprestimo> listarEmprestimos() throws SQLException {
        return emprestimoDAO.listar();
    }

    public List<Usuario> listarUsuarios() throws SQLException {
        return usuarioDAO.listar();
    }

    public List<Livro> listarLivros() throws SQLException {
        return livroDAO.listar();
    }

    public void emprestar(Emprestimo emprestimo, PoliticaPrazo politicaPrazo)
            throws SQLException {

        if (emprestimo == null) {
            throw new IllegalArgumentException("Empréstimo inválido.");
        }

        if (politicaPrazo == null) {
            throw new IllegalArgumentException("Selecione uma política de prazo.");
        }

        if (emprestimo.getUsuarioId() <= 0 || emprestimo.getLivroId() <= 0) {
            throw new IllegalArgumentException("Selecione um usuário e um livro.");
        }

        if (emprestimo.getDataEmprestimo() == null) {
            throw new IllegalArgumentException("Data do empréstimo inválida.");
        }

        LocalDate dataLimite = politicaPrazo.calcularDataLimite(
                emprestimo.getDataEmprestimo()
        );

        emprestimo.setDataLimite(dataLimite);

        emprestimoDAO.inserir(emprestimo);
    }

    public void devolver(int emprestimoId) throws SQLException {

        if (emprestimoId <= 0) {
            throw new IllegalArgumentException(
                    "Selecione um empréstimo válido."
            );
        }

        emprestimoDAO.devolver(emprestimoId);
    }
}