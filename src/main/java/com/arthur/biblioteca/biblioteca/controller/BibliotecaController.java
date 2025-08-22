package com.arthur.biblioteca.biblioteca.controller;

import com.arthur.biblioteca.biblioteca.model.Emprestimo;
import com.arthur.biblioteca.biblioteca.model.Livro;
import com.arthur.biblioteca.biblioteca.model.Usuario;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BibliotecaController {

    private static final List<Livro> livros = new ArrayList<>();
    private static final List<Usuario> usuarios = new ArrayList<>();
    private static final List<Emprestimo> emprestimos = new ArrayList<>();

    // --- Alert helper ---
    private static void mostrarAlerta(String titulo, String mensagem, AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    // --- Usuários ---
    public static void cadastrarUsuario(Usuario usuario) {
        if (usuario == null || usuario.getNome() == null || usuario.getNome().trim().isEmpty()) {
            mostrarAlerta("Erro", "Nome do usuário inválido.", AlertType.ERROR);
            return;
        }
        if (usuarios.contains(usuario)) {
            mostrarAlerta("Erro", "Usuário já cadastrado.", AlertType.WARNING);
            return;
        }
        usuarios.add(usuario);
        mostrarAlerta("Sucesso", "Usuário cadastrado com sucesso: " + usuario.getNome(), AlertType.INFORMATION);
    }

    public static List<Usuario> listarUsuarios() {
        return new ArrayList<>(usuarios);
    }

    // --- Livros ---
    public static void cadastrarLivro(String isbn, String titulo, String autor, String edicao) {
        try {
            if (isbn == null || isbn.trim().isEmpty()
                    || titulo == null || titulo.trim().isEmpty()
                    || autor == null || autor.trim().isEmpty()
                    || edicao == null || edicao.trim().isEmpty()) {
                mostrarAlerta("Erro", "Preencha todos os campos corretamente.", AlertType.ERROR);
                return;
            }

            if (!isbn.matches("\\d+")) {
                mostrarAlerta("Erro", "ISBN deve conter apenas números!", AlertType.ERROR);
                return;
            }
            if (!edicao.matches("\\d+")) {
                mostrarAlerta("Erro", "Edição deve conter apenas números.", AlertType.ERROR);
                return;
            }

            isbn = isbn.trim();  // Remover espaços no início e fim do ISBN

            if (isbn.isEmpty() || !(isbn.length() == 10 || isbn.length() == 13) || !isbn.matches("\\d+")) {
                mostrarAlerta("Erro", "ISBN deve ter 10 ou 13 dígitos!", AlertType.ERROR);
                return;
            }

            Livro novoLivro = new Livro(isbn.trim(), titulo.trim(), autor.trim(), edicao.trim());

            for (Livro livro : livros) {
                if (livro.getIsbn().equalsIgnoreCase(novoLivro.getIsbn())) {
                    mostrarAlerta("Erro", "Já existe um livro com este ISBN.", AlertType.WARNING);
                    return;
                }
                if (livro.getTitulo().equalsIgnoreCase(novoLivro.getTitulo())
                        && livro.getAutor().equalsIgnoreCase(novoLivro.getAutor())
                        && livro.getEdicao().equalsIgnoreCase(novoLivro.getEdicao())) {
                    mostrarAlerta("Erro", "Já existe um livro com o mesmo título, autor e edição.", AlertType.WARNING);
                    return;
                }
            }

            livros.add(novoLivro);
            mostrarAlerta("Sucesso", "Livro cadastrado com sucesso!", AlertType.INFORMATION);

        } catch (Exception e) {
            mostrarAlerta("Erro", "Erro inesperado ao cadastrar livro: " + e.getMessage(), AlertType.ERROR);
        }
    }

    public static List<Livro> listarLivros() {
        return new ArrayList<>(livros);
    }

    // --- Empréstimos ---
    public static void emprestarLivro(int indiceLivro, Usuario usuario) {
        int index = indiceLivro - 1;

        if (index < 0 || index >= livros.size()) {
            mostrarAlerta("Erro", "Índice de livro inválido.", AlertType.ERROR);
            return;
        }
        if (usuario == null || usuario.getNome().trim().isEmpty()) {
            mostrarAlerta("Erro", "Usuário inválido para empréstimo.", AlertType.ERROR);
            return;
        }

        Livro livro = livros.get(index);

        for (Emprestimo e : emprestimos) {
            if (e.getIndiceLivro() == index && !e.isDevolvido()) {
                mostrarAlerta("Aviso", "Este livro já está emprestado.", AlertType.WARNING);
                return;
            }
        }

        Emprestimo novoEmprestimo = new Emprestimo(index, usuario.getNome(), usuario.getPrazoEmprestimo());
        emprestimos.add(novoEmprestimo);
        mostrarAlerta("Sucesso", "Livro \"" + livro.getTitulo() + "\" emprestado para " +
                usuario.getNome() + " até " + novoEmprestimo.getDataDevolucao(), AlertType.INFORMATION);
    }

    public static void devolverLivro(int indiceLivro, String nomeUsuario) {
        int index = indiceLivro - 1;

        if (index < 0 || index >= livros.size()) {
            mostrarAlerta("Erro", "Índice de livro inválido.", AlertType.ERROR);
            return;
        }
        if (nomeUsuario == null || nomeUsuario.trim().isEmpty()) {
            mostrarAlerta("Erro", "Nome do usuário inválido.", AlertType.ERROR);
            return;
        }

        Emprestimo emprestimoEncontrado = null;
        for (Emprestimo e : emprestimos) {
            if (e.getIndiceLivro() == index && e.getAluno().equalsIgnoreCase(nomeUsuario) && !e.isDevolvido()) {
                emprestimoEncontrado = e;
                break;
            }
        }

        if (emprestimoEncontrado != null) {
            emprestimoEncontrado.setDataEntregaReal(LocalDate.now());
            String msg = "Livro \"" + livros.get(index).getTitulo() + "\" devolvido por " + nomeUsuario;
            if (emprestimoEncontrado.isAtrasado()) {
                msg += "\nDevolução atrasada em " + emprestimoEncontrado.diasAtraso() + " dias!";
            }
            mostrarAlerta("Devolução registrada", msg, AlertType.INFORMATION);
        } else {
            mostrarAlerta("Erro", "Nenhum empréstimo encontrado para este usuário e livro.", AlertType.WARNING);
        }
    }

    public static List<Emprestimo> listarEmprestimos() {
        return new ArrayList<>(emprestimos);
    }

    public static List<Livro> getLivros() {
        return Collections.unmodifiableList(livros);
    }

    public static List<Emprestimo> getEmprestimos() {
        return Collections.unmodifiableList(emprestimos);
    }
}