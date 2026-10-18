package com.example.trabalho1pooii.model;

public class Livro {

    private int id;
    private String titulo;
    private String autor;
    private int ano;
    private String genero;
    private int quantidade;

    public Livro() {
    }

    public Livro(int id, String titulo, String autor,
                 int ano, String genero, int quantidade) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.ano = ano;
        this.genero = genero;
        this.quantidade = quantidade;
    }

    public Livro(String titulo, String autor,
                 int ano, String genero, int quantidade) {
        this.titulo = titulo;
        this.autor = autor;
        this.ano = ano;
        this.genero = genero;
        this.quantidade = quantidade;
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public int getAno() {
        return ano;
    }

    public String getGenero() {
        return genero;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}