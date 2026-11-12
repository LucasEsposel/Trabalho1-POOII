package com.example.trabalho1pooii.factory;

import javafx.scene.control.Alert;

public abstract class AlertaFactory {

    protected abstract Alert criarAlerta();

    public void exibir(String titulo, String mensagem) {

        Alert alerta = criarAlerta();

        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);

        alerta.showAndWait();
    }
}