package com.example.trabalho1pooii.observer;

import com.example.trabalho1pooii.factory.AlertaAvisoFactory;
import com.example.trabalho1pooii.factory.AlertaSucessoFactory;
import com.example.trabalho1pooii.model.Livro;

public class AlertaEstoqueObserver implements EstoqueObserver {

    @Override
    public void estoqueAlterado(Livro livro) {

        if (livro == null) {
            return;
        }

        String mensagem = "O estoque do livro '" + livro.getTitulo()
                + "' foi atualizado.\nQuantidade disponível: "
                + livro.getQuantidade();

        if (livro.getQuantidade() == 0) {

            AlertaAvisoFactory alerta = new AlertaAvisoFactory();

            alerta.exibir("Estoque zerado", mensagem);

        } else {

            AlertaSucessoFactory alerta = new AlertaSucessoFactory();

            alerta.exibir("Estoque atualizado", mensagem);
        }
    }
}