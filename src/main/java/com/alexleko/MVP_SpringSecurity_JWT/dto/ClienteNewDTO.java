package com.alexleko.MVP_SpringSecurity_JWT.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;


public class ClienteNewDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    // PROPERTIES

    @NotEmpty(message = "Preenchimento obrigatório")
    @Length(min = 5, max = 120, message = "O Nome deve ter entre 5 e 120 caracteres")
    private String nome;

    @NotEmpty(message = "Preenchimento obrigatório")
    @Email(message = "E-mail inválido")
    private String email;

    @NotEmpty(message = "Preenchimento obrigatório")
    private String senha;


    // CONSTRUCTOR

    public ClienteNewDTO() {
    }


    // GETTERS AND SETTERS

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



}

