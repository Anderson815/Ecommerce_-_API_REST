package com.anderson.ecommerce.model.resource;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "produto")
public class ProdutoModelResource {

    @Id
    private String id;

    @Column(length = 20, nullable = false)
    private String nome;
    @Column(length = 20, nullable = false)
    private String marca;
    @Column(length = 25, nullable = false)
    private String modelo;
    @Column(nullable = false)
    private BigDecimal preco;
    @Column(nullable = false)
    private int estoque;

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
