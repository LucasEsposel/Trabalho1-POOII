package com.example.trabalho1pooii;

import com.example.trabalho1pooii.dao.LivroDAO;
import com.example.trabalho1pooii.model.Livro;
import com.example.trabalho1pooii.dao.EmprestimoDAO;

import java.sql.SQLException;
import java.util.List;

public class LivroService {

    private final LivroDAO livroDAO = new LivroDAO();
    private final EmprestimoDAO emprestimoDAO = new EmprestimoDAO();

    public void cadastrar(Livro livro) throws SQLException {
        validarLivro(livro);
        livroDAO.inserir(livro);
    }

    public List<Livro> listar() throws SQLException {
        return livroDAO.listar();
    }

    public void atualizar(Livro livro) throws SQLException {
        if (livro.getId() <= 0) {
            throw new IllegalArgumentException(
                    "Selecione um livro válido."
            );
        }

        validarLivro(livro);
        livroDAO.atualizar(livro);
    }

    // DELETE, com a nova verificação
    public void excluir(int id) throws SQLException {

        if (id <= 0) {
            throw new IllegalArgumentException("Selecione um livro válido.");
        }

        if (emprestimoDAO.existeEmprestimoParaLivro(id)) {
            throw new IllegalArgumentException(
                    "Não é possível excluir este livro, pois ele possui histórico de empréstimos."
            );
        }

        livroDAO.excluir(id);
    }

    private void validarLivro(Livro livro) {

        if (livro == null) {
            throw new IllegalArgumentException(
                    "Os dados do livro não foram informados."
            );
        }

        String titulo = livro.getTitulo();
        String autor = livro.getAutor();
        String genero = livro.getGenero();

        if (titulo == null || titulo.trim().isEmpty()
                || autor == null || autor.trim().isEmpty()
                || genero == null || genero.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Preencha todos os campos."
            );
        }

        if (livro.getAno() <= 0) {
            throw new IllegalArgumentException(
                    "O ano deve ser maior que zero."
            );
        }

        if (livro.getQuantidade() < 0) {
            throw new IllegalArgumentException(
                    "A quantidade não pode ser negativa."
            );
        }
    }
}