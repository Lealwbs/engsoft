package br.ufmg.dcc.controleincidentes.model;

public class CaminhaoBombeiro extends Veiculo {
    private double capacidadeAguaLitros;
    private boolean escadaMagirus;

    public CaminhaoBombeiro(String id, String prefixo, Localizacao posicaoInicial, double capacidadeAguaLitros, boolean escadaMagirus) {
        super(id, prefixo, posicaoInicial);
        this.capacidadeAguaLitros = capacidadeAguaLitros;
        this.escadaMagirus = escadaMagirus;
    }

    public void combaterFogo() {
        System.out.println("Caminhão " + prefixo + " combatendo o incêndio (capacidade: " + capacidadeAguaLitros + " L).");
    }

    public void resgatarFerragens() {
        System.out.println("Caminhão " + prefixo + " iniciando resgate em ferragens.");
    }

    public double getCapacidadeAguaLitros() {
        return capacidadeAguaLitros;
    }

    public void setCapacidadeAguaLitros(double capacidadeAguaLitros) {
        this.capacidadeAguaLitros = capacidadeAguaLitros;
    }

    public boolean isEscadaMagirus() {
        return escadaMagirus;
    }

    public void setEscadaMagirus(boolean escadaMagirus) {
        this.escadaMagirus = escadaMagirus;
    }

    @Override
    public String toString() {
        return String.format("Caminhão Bombeiro %s (Água: %.0f L, Escada Magirus: %s)",
                prefixo, capacidadeAguaLitros, escadaMagirus ? "Sim" : "Não");
    }
}
