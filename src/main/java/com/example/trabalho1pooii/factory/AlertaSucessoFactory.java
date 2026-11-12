package com.example.trabalho1pooii.factory;

import javafx.scene.control.Alert;

public class AlertaSucessoFactory extends AlertaFactory {

    @Override
    protected Alert criarAlerta() {
        return new Alert(Alert.AlertType.INFORMATION);
    }
}