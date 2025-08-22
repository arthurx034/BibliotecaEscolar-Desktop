package com.arthur.biblioteca.biblioteca.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class Emprestimo {
    private int indiceLivro;          // índice do livro na lista (ou pode ser o ISBN, se preferir)
    private String aluno;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucao;  // data prevista para devolução
    private LocalDate dataEntregaReal; // data que o livro foi devolvido (null se não devolvido)

    public Emprestimo(int indiceLivro, String aluno, int prazoDias) {
        this.indiceLivro = indiceLivro;
        this.aluno = aluno;
        this.dataEmprestimo = LocalDate.now();
        this.dataDevolucao = dataEmprestimo.plusDays(prazoDias);
        this.dataEntregaReal = null; // ainda não devolvido
    }

    public int getIndiceLivro() {
        return indiceLivro;
    }

    public void setIndiceLivro(int indiceLivro) {
        this.indiceLivro = indiceLivro;
    }

    public String getAluno() {
        return aluno;
    }

    public void setAluno(String aluno) {
        this.aluno = aluno;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public LocalDate getDataEntregaReal() {
        return dataEntregaReal;
    }

    public void setDataEntregaReal(LocalDate dataEntregaReal) {
        this.dataEntregaReal = dataEntregaReal;
    }

    public boolean isDevolvido() {
        return dataEntregaReal != null;
    }

    public boolean isAtrasado() {
        if (isDevolvido()) {
            return dataEntregaReal.isAfter(dataDevolucao);
        } else {
            return LocalDate.now().isAfter(dataDevolucao);
        }
    }

    public long diasAtraso() {
        if (!isAtrasado()) return 0;

        LocalDate dataReferencia = isDevolvido() ? dataEntregaReal : LocalDate.now();
        return ChronoUnit.DAYS.between(dataDevolucao, dataReferencia);
    }

    @Override
    public String toString() {
        String status = isDevolvido() ? "Devolvido em: " + dataEntregaReal : "Pendente";
        String atraso = isAtrasado() ? " (Atrasado " + diasAtraso() + " dias)" : "";
        return "Aluno: " + aluno +
                " | Empréstimo: " + dataEmprestimo +
                " | Devolução prevista: " + dataDevolucao +
                " | Status: " + status + atraso;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Emprestimo)) return false;
        Emprestimo that = (Emprestimo) o;
        return indiceLivro == that.indiceLivro && Objects.equals(aluno, that.aluno);
    }

    @Override
    public int hashCode() {
        return Objects.hash(indiceLivro, aluno);
    }
}
