package com.example.trabalho1pooii;

import com.example.trabalho1pooii.dao.UsuarioDAO;
import com.example.trabalho1pooii.model.Usuario;

import java.sql.SQLException;
import java.util.List;

public class UsuarioService {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public void cadastrar(Usuario usuario) throws SQLException {
        validarUsuario(usuario);
        usuarioDAO.inserir(usuario);
    }

    public List<Usuario> listar() throws SQLException {
        return usuarioDAO.listar();
    }

    public void atualizar(Usuario usuario) throws SQLException {
        if (usuario.getId() <= 0) {
            throw new IllegalArgumentException(
                    "Selecione um usuário válido."
            );
        }

        validarUsuario(usuario);
        usuarioDAO.atualizar(usuario);
    }


    public void excluir(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Selecione um usuário válido."
            );
        }

        usuarioDAO.excluir(id);
    }

    // Validação das regras de negócio
    private void validarUsuario(Usuario usuario) {

        if (usuario == null) {
            throw new IllegalArgumentException(
                    "Os dados do usuário não foram informados."
            );
        }

        String nome = usuario.getNome();
        String cpf = usuario.getCpf();
        String email = usuario.getEmail();

        if (nome == null || nome.trim().isEmpty()
                || cpf == null || cpf.trim().isEmpty()
                || email == null || email.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Preencha nome, CPF e e-mail."
            );
        }

        if (!cpf.matches("\\d{11}")) {
            throw new IllegalArgumentException(
                    "O CPF deve conter exatamente 11 números."
            );
        }

        if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalArgumentException(
                    "Informe um e-mail válido."
            );
        }
    }
}