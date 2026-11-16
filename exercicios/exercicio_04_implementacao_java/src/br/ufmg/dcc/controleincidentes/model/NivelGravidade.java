package br.ufmg.dcc.controleincidentes.model;

public enum NivelGravidade {
    BAIXA(1, "Baixa"),
    MEDIA(2, "Média"),
    ALTA(3, "Alta");

    private final int prioridade;
    private final String descricao;

    NivelGravidade(int prioridade, String descricao) {
        this.prioridade = prioridade;
        this.descricao = descricao;
    }

    public int getPrioridade() {
        return prioridade;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
