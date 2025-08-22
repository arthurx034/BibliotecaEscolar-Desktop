package com.arthur.biblioteca.biblioteca.model;

import java.util.Objects;

public class Usuario {
    private String nome;
    private TipoUsuario tipo;

    public Usuario(String nome, TipoUsuario tipo) {
        this.nome = nome;
        this.tipo = tipo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public TipoUsuario getTipo() {
        return tipo;
    }

    public void setTipo(TipoUsuario tipo) {
        this.tipo = tipo;
    }

    public int getPrazoEmprestimo() {
        switch (tipo) {
            case PROFESSOR:
                return 15;
            case ALUNO:
            default:
                return 7;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(nome, usuario.nome) && tipo == usuario.tipo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, tipo);
    }

    @Override
    public String toString() {
        return nome + " (" + tipo + ")";
    }
}
