package com.example.trabalho1pooii.controller;

import com.example.trabalho1pooii.EmprestimoService;
import com.example.trabalho1pooii.model.Emprestimo;
import com.example.trabalho1pooii.model.Livro;
import com.example.trabalho1pooii.model.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import java.sql.SQLException;
import java.time.LocalDate;
import com.example.trabalho1pooii.strategy.PoliticaPrazo;
import com.example.trabalho1pooii.strategy.PrazoPadrao;
import com.example.trabalho1pooii.strategy.PrazoEstendido;
import com.example.trabalho1pooii.factory.AlertaFactory;
import com.example.trabalho1pooii.factory.AlertaSucessoFactory;
import com.example.trabalho1pooii.factory.AlertaErroFactory;
import com.example.trabalho1pooii.factory.AlertaAvisoFactory;
import com.example.trabalho1pooii.observer.EstoqueNotificador;
import com.example.trabalho1pooii.observer.EstoqueObserver;
import com.example.trabalho1pooii.observer.AlertaEstoqueObserver;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class EmprestimoController {

    @FXML private ComboBox<Usuario> comboUsuario;
    @FXML private ComboBox<Livro> comboLivro;
    @FXML private TableView<Emprestimo> tabelaEmprestimos;
    @FXML private ComboBox<PoliticaPrazo> comboPrazo;


    private final EmprestimoService emprestimoService = new EmprestimoService();

    private final EstoqueNotificador estoqueNotificador = new EstoqueNotificador();
    private final EstoqueObserver alertaEstoqueObserver = new AlertaEstoqueObserver();

    private final ObservableList<Emprestimo> emprestimos =
            FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        comboPrazo.setItems(FXCollections.observableArrayList(
                new PrazoPadrao(),
                new PrazoEstendido()
        ));

        configurarComboBoxes();

        comboPrazo.getSelectionModel().selectFirst();

        configurarTabela();
        carregarDados();

        estoqueNotificador.adicionarObservador(alertaEstoqueObserver);
    }

    private void configurarComboBoxes() {
        comboUsuario.setConverter(new StringConverter<>() {
            @Override
            public String toString(Usuario usuario) {
                return usuario == null ? "" :
                        usuario.getNome() + " (CPF: " + usuario.getCpf() + ")";
            }

            @Override
            public Usuario fromString(String texto) {
                return null;
            }
        });

        comboLivro.setConverter(new StringConverter<>() {
            @Override
            public String toString(Livro livro) {
                return livro == null ? "" :
                        livro.getTitulo() + " - " + livro.getAutor()
                                + " (disponíveis: " + livro.getQuantidade() + ")";
            }

            @Override
            public Livro fromString(String texto) {
                return null;
            }
        });

        comboPrazo.setConverter(new StringConverter<>() {
            @Override
            public String toString(PoliticaPrazo politica) {
                if (politica instanceof PrazoPadrao) {
                    return "Prazo padrão (7 dias)";
                }

                if (politica instanceof PrazoEstendido) {
                    return "Prazo estendido (15 dias)";
                }

                return "";
            }

            @Override
            public PoliticaPrazo fromString(String string) {
                return null;
            }
        });

    }

    private void configurarTabela() {
        TableColumn<Emprestimo, Integer> colId =
                new TableColumn<>("ID");
        colId.setCellValueFactory(
                new PropertyValueFactory<>("id"));

        TableColumn<Emprestimo, String> colUsuario =
                new TableColumn<>("Usuário");

        colUsuario.setCellValueFactory(
                new PropertyValueFactory<>("usuarioNome"));

        TableColumn<Emprestimo, String> colLivro =
                new TableColumn<>("Livro");

        colLivro.setCellValueFactory(
                new PropertyValueFactory<>("livroTitulo"));

        TableColumn<Emprestimo, LocalDate> colDataEmprestimo =
                new TableColumn<>("Data do Empréstimo");
        colDataEmprestimo.setCellValueFactory(
                new PropertyValueFactory<>("dataEmprestimo"));

        TableColumn<Emprestimo, LocalDate> colDataLimite =
                new TableColumn<>("Data Limite");

        colDataLimite.setCellValueFactory(
                new PropertyValueFactory<>("dataLimite"));

        TableColumn<Emprestimo, LocalDate> colDataDevolucao =
                new TableColumn<>("Data da Devolução");
        colDataDevolucao.setCellValueFactory(
                new PropertyValueFactory<>("dataDevolucao"));

        TableColumn<Emprestimo, String> colStatus =
                new TableColumn<>("Status");
        colStatus.setCellValueFactory(
                new PropertyValueFactory<>("status"));

        tabelaEmprestimos.getColumns().setAll(
                colId,
                colUsuario,
                colLivro,
                colDataEmprestimo,
                colDataLimite,
                colDataDevolucao,
                colStatus
        );

        tabelaEmprestimos.setItems(emprestimos);
    }

    private void carregarDados() {
        try {
            comboUsuario.setItems(
                    FXCollections.observableArrayList(
                            emprestimoService.listarUsuarios()
                    )
            );

            comboLivro.setItems(
                    FXCollections.observableArrayList(
                            emprestimoService.listarLivros()
                    )
            );

            emprestimos.setAll(
                    emprestimoService.listarEmprestimos()
            );

        } catch (SQLException e) {
            mostrarAlerta(
                    Alert.AlertType.ERROR,
                    "Erro ao carregar dados",
                    e.getMessage()
            );
        }
    }

    @FXML
    private void emprestar() {
        Usuario usuario = comboUsuario.getValue();
        Livro livro = comboLivro.getValue();

        if (usuario == null || livro == null) {
            mostrarAlerta(Alert.AlertType.WARNING,
                    "Atenção",
                    "Selecione um usuário e um livro.");
            return;
        }

        try {
            Emprestimo emprestimo = new Emprestimo(
                    usuario.getId(),
                    livro.getId(),
                    LocalDate.now()
            );

            PoliticaPrazo politica = comboPrazo.getValue();

            if (politica == null) {
                mostrarAlerta(Alert.AlertType.WARNING,
                        "Atenção",
                        "Selecione um prazo de devolução.");
                return;
            }

            int livroId = livro.getId();

            emprestimoService.emprestar(emprestimo, politica);

            carregarDados();

            notificarEstoqueAlterado(livroId);

            comboUsuario.getSelectionModel().clearSelection();
            comboLivro.getSelectionModel().clearSelection();

            mostrarAlerta(Alert.AlertType.INFORMATION,
                    "Sucesso",
                    "Empréstimo registrado com sucesso!");

        } catch (IllegalArgumentException e) {
        mostrarAlerta(
                Alert.AlertType.WARNING,
                "Dados inválidos",
                e.getMessage()
            );
        } catch (SQLException e) {
            mostrarAlerta(
                    Alert.AlertType.ERROR,
                    "Erro ao registrar empréstimo",
                    e.getMessage()
            );
        }
    }

    @FXML
    private void devolver() {
        Emprestimo selecionado =
                tabelaEmprestimos.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING,
                    "Atenção",
                    "Selecione um empréstimo na tabela.");
            return;
        }

        if (!"ATIVO".equals(selecionado.getStatus())) {
            mostrarAlerta(Alert.AlertType.WARNING,
                    "Atenção",
                    "Este empréstimo já foi devolvido.");
            return;
        }

        try {

            int livroId = selecionado.getLivroId();

            emprestimoService.devolver(selecionado.getId());

            carregarDados();

            notificarEstoqueAlterado(livroId);

            mostrarAlerta(Alert.AlertType.INFORMATION,
                    "Sucesso",
                    "Devolução registrada com sucesso!");

        } catch (IllegalArgumentException e) {
        mostrarAlerta(
                Alert.AlertType.WARNING,
                "Dados inválidos",
                e.getMessage()
        );

        } catch (SQLException e) {
            mostrarAlerta(
                    Alert.AlertType.ERROR,
                    "Erro ao registrar devolução",
                    e.getMessage()
            );
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensagem) {

        AlertaFactory fabrica;

        switch (tipo) {
            case INFORMATION:
                fabrica = new AlertaSucessoFactory();
                break;

            case ERROR:
                fabrica = new AlertaErroFactory();
                break;

            case WARNING:
                fabrica = new AlertaAvisoFactory();
                break;

            default:
                throw new IllegalArgumentException(
                        "Tipo de alerta não suportado."
                );
        }

        fabrica.exibir(titulo, mensagem);
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

    private void notificarEstoqueAlterado(int livroId) {

        Livro livroAtualizado = comboLivro.getItems()
                .stream()
                .filter(livro -> livro.getId() == livroId)
                .findFirst()
                .orElse(null);

        if (livroAtualizado != null) {
            estoqueNotificador.notificar(livroAtualizado);
        }
    }
}
