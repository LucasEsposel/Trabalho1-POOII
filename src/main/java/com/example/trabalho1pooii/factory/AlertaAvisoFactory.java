package com.example.trabalho1pooii.factory;

import javafx.scene.control.Alert;

public class AlertaAvisoFactory extends AlertaFactory {

    @Override
    protected Alert criarAlerta() {
        return new Alert(Alert.AlertType.WARNING);
    }
}