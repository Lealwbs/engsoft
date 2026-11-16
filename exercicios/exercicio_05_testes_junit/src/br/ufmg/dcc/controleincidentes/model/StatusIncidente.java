package br.ufmg.dcc.controleincidentes.model;

public enum StatusIncidente {
    REGISTRADO("Registrado"),
    EM_ATENDIMENTO("Em atendimento"),
    FINALIZADO("Finalizado");

    private final String descricao;

    StatusIncidente(String descricao) {
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
