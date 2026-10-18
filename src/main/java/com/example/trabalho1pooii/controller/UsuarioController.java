package com.example.trabalho1pooii.controller;

import com.example.trabalho1pooii.dao.UsuarioDAO;
import com.example.trabalho1pooii.model.Usuario;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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

public class UsuarioController {

    @FXML private TextField txtNome;
    @FXML private TextField txtCpf;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelefone;

    @FXML private TableView<Usuario> tabelaUsuarios;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    private final ObservableList<Usuario> usuarios =
            FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        configurarTabela();
        carregarUsuarios();

        tabelaUsuarios.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, anterior, selecionado) -> {
                    if (selecionado != null) {
                        preencherFormulario(selecionado);
                    }
                });
    }

    private void configurarTabela() {
        TableColumn<Usuario, Integer> colId =
                new TableColumn<>("ID");
        colId.setCellValueFactory(
                new PropertyValueFactory<>("id"));

        TableColumn<Usuario, String> colNome =
                new TableColumn<>("Nome");
        colNome.setCellValueFactory(
                new PropertyValueFactory<>("nome"));

        TableColumn<Usuario, String> colCpf =
                new TableColumn<>("Cpf");
        colCpf.setCellValueFactory(
                new PropertyValueFactory<>("cpf"));

        TableColumn<Usuario, String> colEmail =
                new TableColumn<>("E-mail");
        colEmail.setCellValueFactory(
                new PropertyValueFactory<>("email"));

        TableColumn<Usuario, String> colTelefone =
                new TableColumn<>("Telefone");
        colTelefone.setCellValueFactory(
                new PropertyValueFactory<>("telefone"));

        tabelaUsuarios.getColumns().setAll(
                colId, colNome, colCpf, colEmail, colTelefone);

        tabelaUsuarios.setItems(usuarios);
    }

    private void carregarUsuarios() {
        try {
            usuarios.setAll(usuarioDAO.listar());
        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR,
                    "Erro",
                    "Não foi possível carregar os usuários: "
                            + e.getMessage());
        }
    }

    @FXML
    private void cadastrar() {
        try {
            Usuario usuario = lerFormulario();

            usuarioDAO.inserir(usuario);
            carregarUsuarios();
            limparCampos();

            mostrarAlerta(Alert.AlertType.INFORMATION,
                    "Sucesso", "Usuário cadastrado com sucesso!");

        } catch (IllegalArgumentException e) {
            mostrarAlerta(Alert.AlertType.WARNING,
                    "Dados inválidos", e.getMessage());

        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR,
                    "Erro ao cadastrar",
                    "Verifique os dados. CPF e e-mail devem ser únicos.\n"
                            + e.getMessage());
        }
    }

    @FXML
    private void atualizar() {
        Usuario selecionado =
                tabelaUsuarios.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING,
                    "Atenção", "Selecione um usuário na tabela.");
            return;
        }

        try {
            Usuario dados = lerFormulario();

            selecionado.setNome(dados.getNome());
            selecionado.setCpf(dados.getCpf());
            selecionado.setEmail(dados.getEmail());
            selecionado.setTelefone(dados.getTelefone());

            usuarioDAO.atualizar(selecionado);
            carregarUsuarios();
            limparCampos();
            tabelaUsuarios.getSelectionModel().clearSelection();

            mostrarAlerta(Alert.AlertType.INFORMATION,
                    "Sucesso", "Usuário atualizado com sucesso!");

        } catch (IllegalArgumentException e) {
            mostrarAlerta(Alert.AlertType.WARNING,
                    "Dados inválidos", e.getMessage());

        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR,
                    "Erro ao atualizar", e.getMessage());
        }
    }

    @FXML
    private void excluir() {
        Usuario selecionado =
                tabelaUsuarios.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING,
                    "Atenção", "Selecione um usuário na tabela.");
            return;
        }

        try {
            usuarioDAO.excluir(selecionado.getId());
            carregarUsuarios();
            limparCampos();

            mostrarAlerta(Alert.AlertType.INFORMATION,
                    "Sucesso", "Usuário excluído com sucesso!");

        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR,
                    "Erro ao excluir",
                    "Não foi possível excluir o usuário. "
                            + "Verifique se existem empréstimos associados.\n"
                            + e.getMessage());
        }
    }

    @FXML
    private void limpar() {
        limparCampos();
        tabelaUsuarios.getSelectionModel().clearSelection();
    }

    private Usuario lerFormulario() {
        String nome = txtNome.getText().trim();
        String cpf = txtCpf.getText().trim();
        String email = txtEmail.getText().trim();
        String telefone = txtTelefone.getText().trim();

        if (nome.isEmpty() || cpf.isEmpty() || email.isEmpty()) {
            throw new IllegalArgumentException(
                    "Preencha nome, CPF e e-mail.");
        }

        if (!cpf.matches("\\d{11}")) {
            throw new IllegalArgumentException(
                    "O CPF deve conter exatamente 11 números.");
        }

        if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalArgumentException(
                    "Informe um e-mail válido.");
        }

        return new Usuario(nome, cpf, email, telefone);
    }

    private void preencherFormulario(Usuario usuario) {
        txtNome.setText(usuario.getNome());
        txtCpf.setText(usuario.getCpf());
        txtEmail.setText(usuario.getEmail());
        txtTelefone.setText(usuario.getTelefone() == null
                ? "" : usuario.getTelefone());
    }

    private void limparCampos() {
        txtNome.clear();
        txtCpf.clear();
        txtEmail.clear();
        txtTelefone.clear();
    }

    private void mostrarAlerta(
            Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alerta = new Alert(tipo);
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
