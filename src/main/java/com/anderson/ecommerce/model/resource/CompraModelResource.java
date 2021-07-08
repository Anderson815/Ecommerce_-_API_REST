package com.anderson.ecommerce.model.resource;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "compra")
public class CompraModelResource {

    @Id
    private String id;

    @Column(nullable = false)
    private Date data;
    @Column(nullable = false)
    private BigDecimal valor;
    @Column(nullable = false)
    private boolean finalizado;

    @ManyToOne
    private ClienteModelResource cliente;

    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL)
    private List<ItemModelResource> itens;

    //Get e Set

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public boolean isFinalizado() {
        return finalizado;
    }

    public void setFinalizado(boolean finalizado) {
        this.finalizado = finalizado;
    }

    public ClienteModelResource getCliente() {
        return cliente;
    }

    public void setCliente(ClienteModelResource cliente) {
        this.cliente = cliente;
    }

    public List<ItemModelResource> getItens() {
        return itens;
    }

    public void setItens(List<ItemModelResource> itens) {
        this.itens = itens;
    }
}
