package br.ufmg.dcc.controleincidentes.model;

public class Bombeiro extends Relator {
    private String matricula;
    private String corporacao;

    public Bombeiro(String id, String nome, String telefone, String matricula, String corporacao) {
        super(id, nome, telefone);
        this.matricula = matricula;
        this.corporacao = corporacao;
    }

    public void atenderChamado() {
        System.out.println("Bombeiro " + nome + " (Matrícula: " + matricula + ", " + corporacao + ") atendeu ao chamado.");
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getCorporacao() {
        return corporacao;
    }

    public void setCorporacao(String corporacao) {
        this.corporacao = corporacao;
    }

    @Override
    public String toString() {
        return String.format("Bombeiro %s - Matrícula %s (%s)", nome, matricula, corporacao);
    }
}
