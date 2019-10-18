package com.alexleko.MVP_SpringSecurity_JWT.domain;

public enum Perfil {

    ADMIN(1, "ROLE_ADMIN"),
    CLIENTE(2, "ROLE_CLIENTE");

    private int codigo;
    private String descricao;


    // ===================
    //     CONSTRUCTORS
    // ===================

    Perfil(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }


    // ===================
    //      GETTERS
    // ===================

    public Integer getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }


    // ===================
    //     COMPLEMENTS
    // ===================

    public static Perfil toEnum(Integer cod){

        if (cod == null) return null;

        for (Perfil cli : Perfil.values()){
            if (cod.equals(cli.getCodigo())){
                return cli;
            }
        }

        throw new IllegalArgumentException("Id inválido: " + cod);
    }

}

