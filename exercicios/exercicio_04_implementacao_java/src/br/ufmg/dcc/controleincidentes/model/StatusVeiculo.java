package br.ufmg.dcc.controleincidentes.model;

public enum StatusVeiculo {
    DISPONIVEL("Disponível"),
    A_CAMINHO("A caminho"),
    NO_LOCAL("No local"),
    INDISPONIVEL("Indisponível");

    private final String descricao;

    StatusVeiculo(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
