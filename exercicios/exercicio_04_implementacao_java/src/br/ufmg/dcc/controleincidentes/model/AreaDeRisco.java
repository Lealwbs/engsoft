package br.ufmg.dcc.controleincidentes.model;

import java.util.Objects;

public class AreaDeRisco {
    private String nome;
    private int nivelPerigo;
    private boolean exigeMultiplosVeiculos;
    private int qtdMinimaVeiculos;
    private Localizacao centro;
    private double raioKm;

    public AreaDeRisco(String nome, int nivelPerigo, boolean exigeMultiplosVeiculos, int qtdMinimaVeiculos, Localizacao centro, double raioKm) {
        this.nome = nome;
        this.nivelPerigo = nivelPerigo;
        this.exigeMultiplosVeiculos = exigeMultiplosVeiculos;
        this.qtdMinimaVeiculos = qtdMinimaVeiculos;
        this.centro = centro;
        this.raioKm = raioKm;
    }

    // Verifica se a localização informada está dentro do raio desta área
    public boolean contem(Localizacao loc) {
        if (loc == null || this.centro == null) {
            return false;
        }
        return this.centro.calcularDistancia(loc) <= this.raioKm;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getNivelPerigo() {
        return nivelPerigo;
    }

    public void setNivelPerigo(int nivelPerigo) {
        this.nivelPerigo = nivelPerigo;
    }

    public boolean isExigeMultiplosVeiculos() {
        return exigeMultiplosVeiculos;
    }

    public void setExigeMultiplosVeiculos(boolean exigeMultiplosVeiculos) {
        this.exigeMultiplosVeiculos = exigeMultiplosVeiculos;
    }

    public int getQtdMinimaVeiculos() {
        return qtdMinimaVeiculos;
    }

    public void setQtdMinimaVeiculos(int qtdMinimaVeiculos) {
        this.qtdMinimaVeiculos = qtdMinimaVeiculos;
    }

    public Localizacao getCentro() {
        return centro;
    }

    public void setCentro(Localizacao centro) {
        this.centro = centro;
    }

    public double getRaioKm() {
        return raioKm;
    }

    public void setRaioKm(double raioKm) {
        this.raioKm = raioKm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AreaDeRisco)) return false;
        AreaDeRisco that = (AreaDeRisco) o;
        return Objects.equals(nome, that.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome);
    }

    @Override
    public String toString() {
        return String.format("%s (Nível %d, Mínimo de viaturas: %d)", nome, nivelPerigo, qtdMinimaVeiculos);
    }
}
