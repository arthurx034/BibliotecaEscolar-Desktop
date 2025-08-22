package com.arthur.biblioteca.biblioteca.model;

import java.util.Objects;

public class Livro {
    private String isbn;
    private String titulo;
    private String autor;
    private String edicao;

    public Livro(String isbn, String titulo, String autor, String edicao) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.edicao = edicao;
    }

    // Getters e setters

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getEdicao() {
        return edicao;
    }

    public void setEdicao(String edicao) {
        this.edicao = edicao;
    }

    // equals e hashCode para evitar duplicidade de livros no sistema

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Livro)) return false;
        Livro livro = (Livro) o;
        return Objects.equals(isbn, livro.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }

    @Override
    public String toString() {
        return titulo + " | Autor: " + autor + " | Edição: " + edicao + " | ISBN: " + isbn;
    }
}