package com.arthur.biblioteca.biblioteca.view;

import com.arthur.biblioteca.biblioteca.controller.BibliotecaController;
import com.arthur.biblioteca.biblioteca.model.Livro;
import com.arthur.biblioteca.biblioteca.model.Usuario;
import com.arthur.biblioteca.biblioteca.model.TipoUsuario;
import com.arthur.biblioteca.biblioteca.model.Emprestimo;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;

public class BibliotecaControllerFX {

    @FXML
    private Button btnCadastrarLivro;

    @FXML
    private Button btnListarLivros;

    @FXML
    private Button btnEmprestarLivro;

    @FXML
    private Button btnListarEmprestimos;

    @FXML
    private Button btnDevolverLivro;

    @FXML
    private Button btnSair;

    @FXML
    private TextArea textAreaOutput;

    @FXML
    public void initialize() {
        btnCadastrarLivro.setOnAction(e -> cadastrarLivro());
        btnListarLivros.setOnAction(e -> listarLivros());
        btnEmprestarLivro.setOnAction(e -> emprestarLivro());
        btnListarEmprestimos.setOnAction(e -> listarEmprestimos());
        btnDevolverLivro.setOnAction(e -> devolverLivro());
        btnSair.setOnAction(e -> sair());
    }

    private void cadastrarLivro() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Cadastrar Livro");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        TextField isbnField = new TextField();
        isbnField.setPromptText("ISBN (10 ou 13 dígitos)");

        TextField tituloField = new TextField();
        tituloField.setPromptText("Título");

        TextField autorField = new TextField();
        autorField.setPromptText("Autor");

        TextField edicaoField = new TextField();
        edicaoField.setPromptText("Edição");

        grid.add(new Label("ISBN:"), 0, 0);     grid.add(isbnField, 1, 0);
        grid.add(new Label("Título:"), 0, 1);
        grid.add(tituloField, 1, 1);
        grid.add(new Label("Autor:"), 0, 2);
        grid.add(autorField, 1, 2);
        grid.add(new Label("Edição:"), 0, 3);
        grid.add(edicaoField, 1, 3);

        dialog.getDialogPane().setContent(grid);

        ButtonType cadastrarButtonType = new ButtonType("Cadastrar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(cadastrarButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == cadastrarButtonType) {
                BibliotecaController.cadastrarLivro(
                        isbnField.getText(),
                        tituloField.getText(),
                        autorField.getText(),
                        edicaoField.getText()
                );
                atualizarSaida("Livro cadastrado (ou erro) - ver console para detalhes.");
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void listarLivros() {
        List<Livro> livros = BibliotecaController.getLivros();
        if (livros.isEmpty()) {
            atualizarSaida("Nenhum livro cadastrado.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Lista de livros:\n");
        for (int i = 0; i < livros.size(); i++) {
            Livro l = livros.get(i);
            sb.append((i + 1))
                    .append(" - ")
                    .append(l.getTitulo())
                    .append(" | Autor: ").append(l.getAutor())
                    .append(" | Edição: ").append(l.getEdicao())
                    .append(" | ISBN: ").append(l.getIsbn())
                    .append("\n");
        }
        atualizarSaida(sb.toString());
    }

    private void emprestarLivro() {
        List<Livro> livros = BibliotecaController.getLivros();
        if (livros.isEmpty()) {
            atualizarSaida("Nenhum livro disponível para empréstimo.");
            return;
        }

        ChoiceDialog<Integer> escolhaLivroDialog = new ChoiceDialog<>();
        for (int i = 0; i < livros.size(); i++) {
            escolhaLivroDialog.getItems().add(i + 1);
        }
        escolhaLivroDialog.setTitle("Emprestar Livro");
        escolhaLivroDialog.setHeaderText("Escolha o número do livro para empréstimo:");
        escolhaLivroDialog.setContentText("Número do livro:");

        Optional<Integer> resultLivro = escolhaLivroDialog.showAndWait();
        if (!resultLivro.isPresent()) return;

        TextInputDialog alunoDialog = new TextInputDialog();
        alunoDialog.setTitle("Nome do Aluno");
        alunoDialog.setHeaderText("Digite o nome do aluno:");
        Optional<String> alunoResult = alunoDialog.showAndWait();
        if (!alunoResult.isPresent() || alunoResult.get().trim().isEmpty()) {
            showAlert(AlertType.ERROR, "Erro", "Nome do aluno inválido.");
            return;
        }

        Usuario usuario = new Usuario(alunoResult.get().trim(), TipoUsuario.ALUNO);
        BibliotecaController.emprestarLivro(resultLivro.get(), usuario);
        atualizarSaida("Livro emprestado para " + usuario.getNome());
    }

    private void listarEmprestimos() {
        List<Emprestimo> emprestimos = BibliotecaController.getEmprestimos();
        if (emprestimos.isEmpty()) {
            atualizarSaida("Nenhum empréstimo registrado.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Empréstimos:\n");
        for (int i = 0; i < emprestimos.size(); i++) {
            Emprestimo e = emprestimos.get(i);
            Livro livro = BibliotecaController.getLivros().get(e.getIndiceLivro());
            sb.append((i + 1))
                    .append(" - Aluno: ").append(e.getAluno())
                    .append(" | Livro: ").append(livro.getTitulo())
                    .append("\n");
        }
        atualizarSaida(sb.toString());
    }

    private void devolverLivro() {
        List<Emprestimo> emprestimos = BibliotecaController.getEmprestimos();
        if (emprestimos.isEmpty()) {
            atualizarSaida("Nenhum empréstimo registrado.");
            return;
        }

        ChoiceDialog<Integer> escolhaEmprestimoDialog = new ChoiceDialog<>();
        for (int i = 0; i < emprestimos.size(); i++) {
            escolhaEmprestimoDialog.getItems().add(i + 1);
        }
        escolhaEmprestimoDialog.setTitle("Devolver Livro");
        escolhaEmprestimoDialog.setHeaderText("Escolha o número do empréstimo para devolução:");
        escolhaEmprestimoDialog.setContentText("Número do empréstimo:");

        Optional<Integer> resultEmprestimo = escolhaEmprestimoDialog.showAndWait();
        if (!resultEmprestimo.isPresent()) return;

        Emprestimo emprestimo = emprestimos.get(resultEmprestimo.get() - 1);

        TextInputDialog alunoDialog = new TextInputDialog();
        alunoDialog.setTitle("Nome do Aluno");
        alunoDialog.setHeaderText("Digite o nome do aluno para confirmar a devolução:");
        Optional<String> alunoResult = alunoDialog.showAndWait();
        if (!alunoResult.isPresent() || alunoResult.get().trim().isEmpty()) {
            showAlert(AlertType.ERROR, "Erro", "Nome do aluno inválido.");
            return;
        }

        if (!emprestimo.getAluno().equalsIgnoreCase(alunoResult.get().trim())) {
            showAlert(AlertType.ERROR, "Erro", "Nome do aluno não corresponde ao empréstimo selecionado.");
            return;
        }

        BibliotecaController.devolverLivro(emprestimo.getIndiceLivro() + 1, alunoResult.get().trim());
        atualizarSaida("Livro devolvido com sucesso.");
    }

    private void sair() {
        // Fecha a janela principal
        Stage stage = (Stage) btnSair.getScene().getWindow();
        stage.close();
    }

    private void atualizarSaida(String mensagem) {
        textAreaOutput.setText(mensagem);
    }

    private void showAlert(AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
