package com.example.trabalho1pooii.service;

import com.example.trabalho1pooii.UsuarioService;
import com.example.trabalho1pooii.model.Usuario;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioServiceTest {

    private final UsuarioService usuarioService = new UsuarioService();

    @Test
    void deveRejeitarUsuarioComCpfInvalido() {

        Usuario usuario = new Usuario(
                "Joao",
                "12345",
                "joao@gmail.com",
                "999999999"
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> usuarioService.cadastrar(usuario),
                "O sistema deveria rejeitar um CPF inválido."
        );
    }

    @Test
    void deveRejeitarUsuarioComEmailInvalido() {

        Usuario usuario = new Usuario(
                "Joao",
                "12345678901",
                "joaoemail.com",
                "999999999"
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> usuarioService.cadastrar(usuario),
                "O sistema deveria rejeitar um e-mail inválido."
        );
    }

    @Test
    void deveRejeitarExclusaoComIdInvalido() {

        int id = 0;

        assertThrows(
                IllegalArgumentException.class,
                () -> usuarioService.excluir(id),
                "O sistema deveria rejeitar a exclusão com ID inválido."
        );
    }
}