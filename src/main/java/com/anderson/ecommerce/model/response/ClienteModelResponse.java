package com.anderson.ecommerce.model.response;

import com.anderson.ecommerce.model.resource.CompraModelResource;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

public class ClienteModelResponse {

    private String id;

    private String nome;
    private String email;
    private String senha;
    private String telefone;

    @JsonIgnoreProperties("cliente")
    private List<CompraModelResource> compras;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public List<CompraModelResource> getCompras() {
        return compras;
    }

    public void setCompras(List<CompraModelResource> compras) {
        this.compras = compras;
    }
}
