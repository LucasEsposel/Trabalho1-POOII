package com.example.trabalho1pooii.observer;

import com.example.trabalho1pooii.model.Livro;

import java.util.ArrayList;
import java.util.List;

public class EstoqueNotificador {

    private final List<EstoqueObserver> observadores = new ArrayList<>();

    public void adicionarObservador(EstoqueObserver observador) {
        observadores.add(observador);
    }

    public void removerObservador(EstoqueObserver observador) {
        observadores.remove(observador);
    }

    public void notificar(Livro livro) {

        for (EstoqueObserver observador : observadores) {
            observador.estoqueAlterado(livro);
        }
    }
}