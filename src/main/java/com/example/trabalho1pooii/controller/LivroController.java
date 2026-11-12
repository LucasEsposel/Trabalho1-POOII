package com.example.trabalho1pooii.controller;

import com.example.trabalho1pooii.service.LivroService;
import com.example.trabalho1pooii.model.Livro;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;

public class LivroController {

    @FXML
    private TextField txtTitulo;

    @FXML
    private TextField txtAutor;

    @FXML
    private TextField txtAno;

    @FXML
    private TextField txtGenero;

    @FXML
    private TextField txtQuantidade;

    @FXML
    private Button btnCadastrar;

    @FXML
    private Button btnAtualizar;

    @FXML
    private Button btnExcluir;

    @FXML
    private Button btnLimpar;

    @FXML
    private TableView<Livro> tabelaLivros;

    private final LivroService livroService = new LivroService();

    private final ObservableList<Livro> livros =
            FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        configurarTabela();
        carregarLivros();
    }

    private void configurarTabela() {
        TableColumn<Livro, Integer> colId =
                new TableColumn<>("ID");
        colId.setCellValueFactory(
                new PropertyValueFactory<>("id"));

        TableColumn<Livro, String> colTitulo =
                new TableColumn<>("Título");
        colTitulo.setCellValueFactory(
                new PropertyValueFactory<>("titulo"));

        TableColumn<Livro, String> colAutor =
                new TableColumn<>("Autor");
        colAutor.setCellValueFactory(
                new PropertyValueFactory<>("autor"));

        TableColumn<Livro, Integer> colAno =
                new TableColumn<>("Ano");
        colAno.setCellValueFactory(
                new PropertyValueFactory<>("ano"));

        TableColumn<Livro, String> colGenero =
                new TableColumn<>("Gênero");
        colGenero.setCellValueFactory(
                new PropertyValueFactory<>("genero"));

        TableColumn<Livro, Integer> colQuantidade =
                new TableColumn<>("Quantidade");
        colQuantidade.setCellValueFactory(
                new PropertyValueFactory<>("quantidade"));

        tabelaLivros.getColumns().setAll(
                colId, colTitulo, colAutor,
                colAno, colGenero, colQuantidade);

        tabelaLivros.setItems(livros);
    }

    private void carregarLivros() {
        try {
            livros.setAll(livroService.listar());
        } catch (SQLException e) {
            mostrarAlerta("Erro",
                    "Não foi possível carregar os livros: "
                            + e.getMessage());
        }
    }

    @FXML
    private void cadastrar() {
        try {
            Livro livro = lerFormulario();
            livroService.cadastrar(livro);

            carregarLivros();
            limparCampos();

            mostrarAlerta("Sucesso",
                    "Livro cadastrado com sucesso!");
        } catch (IllegalArgumentException e) {
            mostrarAlerta("Dados inválidos", e.getMessage());
        } catch (SQLException e) {
            mostrarAlerta("Erro ao cadastrar", e.getMessage());
        }
    }

    @FXML
    private void atualizar() {
        Livro selecionado =
                tabelaLivros.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            mostrarAlerta("Atenção",
                    "Selecione um livro na tabela.");
            return;
        }

        try {
            Livro dados = lerFormulario();

            Livro livro = new Livro(
                    selecionado.getId(),
                    dados.getTitulo(),
                    dados.getAutor(),
                    dados.getAno(),
                    dados.getGenero(),
                    dados.getQuantidade()
            );

            livroService.atualizar(livro);

            carregarLivros();
            limparCampos();

            mostrarAlerta("Sucesso",
                    "Livro atualizado com sucesso!");
        } catch (IllegalArgumentException e) {
            mostrarAlerta("Dados inválidos", e.getMessage());
        } catch (SQLException e) {
            mostrarAlerta("Erro ao atualizar", e.getMessage());
        }
    }

    @FXML
    private void excluir() {
        Livro selecionado =
                tabelaLivros.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            mostrarAlerta("Atenção",
                    "Selecione um livro na tabela.");
            return;
        }

        try {
            livroService.excluir(selecionado.getId());

            carregarLivros();
            limparCampos();

            mostrarAlerta("Sucesso",
                    "Livro excluído com sucesso!");
        } catch (IllegalArgumentException e) {
        mostrarAlerta(
                "Exclusão não permitida",
                e.getMessage()
        );

    } catch (SQLException e) {
        mostrarAlerta(
                "Erro ao excluir livro",
                e.getMessage()
        );
    }
    }

    @FXML
    private void limpar() {
        limparCampos();
        tabelaLivros.getSelectionModel().clearSelection();
    }

    private Livro lerFormulario() {

        String titulo = txtTitulo.getText().trim();
        String autor = txtAutor.getText().trim();
        String genero = txtGenero.getText().trim();

        try {
            int ano = Integer.parseInt(txtAno.getText().trim());
            int quantidade = Integer.parseInt(
                    txtQuantidade.getText().trim()
            );

            return new Livro(
                    titulo, autor, ano, genero, quantidade
            );

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Ano e quantidade devem ser números inteiros."
            );
        }
    }

    private void limparCampos() {
        txtTitulo.clear();
        txtAutor.clear();
        txtAno.clear();
        txtGenero.clear();
        txtQuantidade.clear();
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    @FXML
    private void voltarMenu(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/com/example/trabalho1pooii/view/Principal.fxml"
                    )
            );

            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

            stage.setScene(scene);
            stage.setTitle("Sistema de Biblioteca");
            stage.centerOnScreen();

        } catch (Exception e) {
            e.printStackTrace();

            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Erro de navegação");
            alerta.setHeaderText("Não foi possível voltar ao menu.");
            alerta.setContentText(e.getMessage());
            alerta.showAndWait();
        }
    }
}
