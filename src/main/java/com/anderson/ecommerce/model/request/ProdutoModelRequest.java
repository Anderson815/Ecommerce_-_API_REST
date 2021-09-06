package com.anderson.ecommerce.model.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

public class ProdutoModelRequest {

    @NotNull(message = "O nome não foi informado")
    @Size(min = 3, max = 20, message = "o nome tem menos que 3 ou mais do que 20 caracteres")
    private String nome;

    @NotNull(message = "A marca não foi informada")
    @Size(min = 2, max = 20, message = "o nome tem menos que 2 ou mais do que 20 caracteres")
    private String marca;

    @NotNull(message = "O modelo não foi informado")
    @Size(min = 2, max = 25, message = "o modelo tem meno de 2 ou mais de 25 caracteres")
    private String modelo;

    @NotNull(message = "O preço não foi informado")
    private BigDecimal preco;

    @NotNull(message = "O estoque não foi informado")
    private int estoque;

    //Construtores

    public ProdutoModelRequest(String nome, String marca, String modelo, BigDecimal preco, int estoque) {
        this.nome = nome;
        this.marca = marca;
        this.modelo = modelo;
        this.preco = preco;
        this.estoque = estoque;
    }


    //Get e Set

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
