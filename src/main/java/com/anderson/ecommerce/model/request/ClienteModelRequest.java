package com.anderson.ecommerce.model.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ClienteModelRequest {

    @NotBlank(message = "O nome não foi informado")
    @Size(min = 3, max = 35, message = "O nome deve possuir de 3 a 35 caracteres")
    private String nome;

    @NotBlank(message = "O e-mail não foi informado")
    @Email(message = "O campo e-mail não está no padrão de e-mail")
    @Size(max = 35, message = "O e-mail deve possuir no máximo 35 caracteres")
    private String email;

    @NotBlank(message = "A senha não foi informada")
    @Size(min = 6, max = 20, message = "A senha deve possuir de 6 a 20 caracteres")
    private String senha;

    @NotBlank(message = "O telefone não foi informado")
    @Size(min = 11, max = 11, message = "O telefone deve possuir 11 números (ddd mais o número)")
    private String telefone;

    //Get e Set

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
