package com.anderson.ecommerce.model.resource;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "cliente")
public class ClienteModelResource {

    @Id
    private String id;

    @Column(length = 35, nullable = false)
    private String nome;
    // ser único
    @Column(length = 35, nullable = false)
    private String email;
    @Column(length = 20, nullable = false)
    private String senha;
    // ser char
    @Column(length = 11, nullable = false)
    private String telefone;

    //private List<CompraModelResource> compras;

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
}
