package br.ufmg.dcc.controleincidentes.model;

public class ViaturaPolicial extends Veiculo {
    private boolean blindagem;
    private boolean armamentoPesado;

    public ViaturaPolicial(String id, String prefixo, Localizacao posicaoInicial, boolean blindagem, boolean armamentoPesado) {
        super(id, prefixo, posicaoInicial);
        this.blindagem = blindagem;
        this.armamentoPesado = armamentoPesado;
    }

    public void patrulhar() {
        System.out.println("Viatura policial " + prefixo + " em patrulhamento na região.");
    }

    public void interceptar() {
        System.out.println("Viatura policial " + prefixo + " em procedimento de interceptação.");
    }

    public boolean isBlindagem() {
        return blindagem;
    }

    public void setBlindagem(boolean blindagem) {
        this.blindagem = blindagem;
    }

    public boolean isArmamentoPesado() {
        return armamentoPesado;
    }

    public void setArmamentoPesado(boolean armamentoPesado) {
        this.armamentoPesado = armamentoPesado;
    }

    @Override
    public String toString() {
        return String.format("Viatura %s (Blindagem: %s, Armamento pesado: %s)",
                prefixo, blindagem ? "Sim" : "Não", armamentoPesado ? "Sim" : "Não");
    }
}
