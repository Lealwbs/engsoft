package br.ufmg.dcc.controleincidentes.model;

public enum TipoIncidente {
    ACIDENTE_TRANSITO("Acidente de Trânsito"),
    INCENDIO("Incêndio"),
    ASSALTO("Assalto"),
    EMERGENCIA_MEDICA("Emergência Médica");

    private final String descricao;

    TipoIncidente(String descricao) {
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
