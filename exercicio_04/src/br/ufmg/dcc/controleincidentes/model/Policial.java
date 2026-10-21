package br.ufmg.dcc.controleincidentes.model;

public class Policial extends Relator {
    private String matricula;
    private String batalhao;

    public Policial(String id, String nome, String telefone, String matricula, String batalhao) {
        super(id, nome, telefone);
        this.matricula = matricula;
        this.batalhao = batalhao;
    }

    public void atenderChamado() {
        System.out.println("Policial " + nome + " (Matrícula: " + matricula + ", " + batalhao + ") atendeu ao chamado.");
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getBatalhao() {
        return batalhao;
    }

    public void setBatalhao(String batalhao) {
        this.batalhao = batalhao;
    }

    @Override
    public String toString() {
        return String.format("Policial %s - Matrícula %s (%s)", nome, matricula, batalhao);
    }
}
