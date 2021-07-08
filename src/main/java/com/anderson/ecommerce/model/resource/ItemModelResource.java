package com.anderson.ecommerce.model.resource;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "item")
public class ItemModelResource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private int unidades;
    @Column(nullable = false)
    private BigDecimal preco_total;

    @ManyToOne
    private ProdutoModelResource produto;

    @ManyToOne
    private CompraModelResource compra;

    //Get e Set

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUnidades() {
        return unidades;
    }

    public void setUnidades(int unidades) {
        this.unidades = unidades;
    }

    public BigDecimal getPreco_total() {
        return preco_total;
    }

    public void setPreco_total(BigDecimal preco_total) {
        this.preco_total = preco_total;
    }

    public ProdutoModelResource getProduto() {
        return produto;
    }

    public void setProduto(ProdutoModelResource produto) {
        this.produto = produto;
    }

    public CompraModelResource getCompra() {
        return compra;
    }

    public void setCompra(CompraModelResource compra) {
        this.compra = compra;
    }
}
