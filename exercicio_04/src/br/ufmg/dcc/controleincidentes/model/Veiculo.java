package br.ufmg.dcc.controleincidentes.model;

import java.util.Objects;

public abstract class Veiculo {
    protected String id;
    protected String prefixo;
    protected StatusVeiculo status;
    protected Localizacao posicaoAtual;
    protected Localizacao ultimaPosicaoConhecida;
    protected boolean sinalGpsAtivo;

    public Veiculo(String id, String prefixo, Localizacao posicaoInicial) {
        this.id = id;
        this.prefixo = prefixo;
        this.posicaoAtual = posicaoInicial;
        this.ultimaPosicaoConhecida = posicaoInicial;
        this.status = StatusVeiculo.DISPONIVEL;
        this.sinalGpsAtivo = true;
    }

    public Localizacao solicitarPosicao() {
        if (sinalGpsAtivo && posicaoAtual != null) {
            this.ultimaPosicaoConhecida = posicaoAtual;
            return posicaoAtual;
        }
        // Se estiver sem sinal de GPS, recorre à posição estimada
        return estimarPosicao();
    }

    public Localizacao estimarPosicao() {
        if (ultimaPosicaoConhecida != null) {
            // Projeção aproximada baseada no último ponto conhecido
            return new Localizacao(
                    ultimaPosicaoConhecida.getLatitude() + 0.001,
                    ultimaPosicaoConhecida.getLongitude() + 0.001,
                    ultimaPosicaoConhecida.getEndereco() + " (Estimada)",
                    ultimaPosicaoConhecida.getBairro()
            );
        }
        return posicaoAtual;
    }

    public double calcularDistanciaAte(Localizacao alvo) {
        Localizacao pos = solicitarPosicao();
        if (pos == null || alvo == null) {
            return Double.MAX_VALUE;
        }
        return pos.calcularDistancia(alvo);
    }

    public boolean estaDisponivel() {
        return this.status == StatusVeiculo.DISPONIVEL;
    }

    public String getId() {
        return id;
    }

    public String getPrefixo() {
        return prefixo;
    }

    public StatusVeiculo getStatus() {
        return status;
    }

    public void setStatus(StatusVeiculo status) {
        this.status = status;
    }

    public Localizacao getPosicaoAtual() {
        return posicaoAtual;
    }

    public void setPosicaoAtual(Localizacao posicaoAtual) {
        this.posicaoAtual = posicaoAtual;
        if (posicaoAtual != null) {
            this.ultimaPosicaoConhecida = posicaoAtual;
        }
    }

    public boolean isSinalGpsAtivo() {
        return sinalGpsAtivo;
    }

    public void setSinalGpsAtivo(boolean sinalGpsAtivo) {
        this.sinalGpsAtivo = sinalGpsAtivo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Veiculo)) return false;
        Veiculo veiculo = (Veiculo) o;
        return Objects.equals(id, veiculo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("%s [%s] - %s", prefixo, getClass().getSimpleName(), status);
    }
}
