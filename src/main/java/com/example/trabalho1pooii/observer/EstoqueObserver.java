package com.example.trabalho1pooii.observer;

import com.example.trabalho1pooii.model.Livro;

public interface EstoqueObserver {

    void estoqueAlterado(Livro livro);
}