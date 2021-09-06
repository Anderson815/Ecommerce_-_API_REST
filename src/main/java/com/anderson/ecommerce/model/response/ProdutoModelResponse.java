package com.anderson.ecommerce.model.response;

import java.math.BigDecimal;

public class ProdutoModelResponse {

    private String id;
    private String nome;
    private String marca;
    private String modelo;
    private BigDecimal preco;
    private int estoque;

    //Construtor

    public ProdutoModelResponse(String id, String nome, String marca, String modelo, BigDecimal preco, int estoque) {
        this.id = id;
        this.nome = nome;
        this.marca = marca;
        this.modelo = modelo;
        this.preco = preco;
        this.estoque = estoque;
    }

    //Get e Set

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public int getEstoque() {
        return estoque;
    }

    public void setEstoque(int estoque) {
        this.estoque = estoque;
    }

}
