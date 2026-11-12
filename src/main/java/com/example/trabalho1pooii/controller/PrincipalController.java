    package com.example.trabalho1pooii.controller;

    import javafx.event.ActionEvent;
    import javafx.fxml.FXMLLoader;
    import javafx.scene.Node;
    import javafx.scene.Parent;
    import javafx.scene.Scene;
    import javafx.stage.Stage;

    import java.io.IOException;
    import javafx.fxml.FXML;

    public class PrincipalController {

        @FXML
        private void abrirLivros(ActionEvent event) {
            abrirTela(event, "Livro.fxml", "Gerenciar Livros");
        }

        @FXML
        private void abrirUsuarios(ActionEvent event) {
            abrirTela(event, "Usuario.fxml", "Gerenciar Usuários");
        }

        @FXML
        private void abrirEmprestimos(ActionEvent event) {
            abrirTela(event, "Emprestimo.fxml", "Gerenciar Empréstimos");
        }

        private void abrirTela(
                ActionEvent event,
                String arquivo,
                String titulo) {

            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource(
                                "/com/example/trabalho1pooii/view/" + arquivo
                        )
                );

                Parent root = loader.load();
                Scene scene = new Scene(root);

                Stage stage = (Stage) ((Node) event.getSource())
                        .getScene().getWindow();

                stage.setTitle("Sistema de Biblioteca - " + titulo);
                stage.setScene(scene);

            } catch (IOException | RuntimeException e) {
                e.printStackTrace();
            }
        }
    }
