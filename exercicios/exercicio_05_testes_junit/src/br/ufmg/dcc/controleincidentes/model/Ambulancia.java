package br.ufmg.dcc.controleincidentes.model;

public class Ambulancia extends Veiculo {
    private boolean tipoUTI;
    private boolean suporteAvancadoVida;

    public Ambulancia(String id, String prefixo, Localizacao posicaoInicial, boolean tipoUTI, boolean suporteAvancadoVida) {
        super(id, prefixo, posicaoInicial);
        this.tipoUTI = tipoUTI;
        this.suporteAvancadoVida = suporteAvancadoVida;
    }

    public void socorrerVitima() {
        System.out.println("Ambulância " + prefixo + " socorrendo vítima no local.");
    }

    public void estabilizarPaciente() {
        System.out.println("Ambulância " + prefixo + " estabilizando paciente (UTI: " + (tipoUTI ? "Sim" : "Não") + ").");
    }

    public boolean isTipoUTI() {
        return tipoUTI;
    }

    public void setTipoUTI(boolean tipoUTI) {
        this.tipoUTI = tipoUTI;
    }

    public boolean isSuporteAvancadoVida() {
        return suporteAvancadoVida;
    }

    public void setSuporteAvancadoVida(boolean suporteAvancadoVida) {
        this.suporteAvancadoVida = suporteAvancadoVida;
    }

    @Override
    public String toString() {
        return String.format("Ambulância %s (UTI: %s, SAV: %s)",
                prefixo, tipoUTI ? "Sim" : "Não", suporteAvancadoVida ? "Sim" : "Não");
    }
}
