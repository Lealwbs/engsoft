package br.ufmg.dcc.controleincidentes.model;

import java.util.Objects;

public class ServicoEmergencia {
    private String nomeOrgao;
    private String telefoneEmergencia;
    private String canalIntegracao;

    public ServicoEmergencia(String nomeOrgao, String telefoneEmergencia, String canalIntegracao) {
        this.nomeOrgao = nomeOrgao;
        this.telefoneEmergencia = telefoneEmergencia;
        this.canalIntegracao = canalIntegracao;
    }

    public void emitirAlertaAutomatico(Incidente inc) {
        if (inc == null) {
            return;
        }
        System.out.printf("[Alerta para %s (%s)] Incidente %s no bairro %s.%n",
                nomeOrgao, telefoneEmergencia, inc.getId(),
                (inc.getLocal() != null ? inc.getLocal().getBairro() : "não informado"));
    }

    public String getNomeOrgao() {
        return nomeOrgao;
    }

    public void setNomeOrgao(String nomeOrgao) {
        this.nomeOrgao = nomeOrgao;
    }

    public String getTelefoneEmergencia() {
        return telefoneEmergencia;
    }

    public void setTelefoneEmergencia(String telefoneEmergencia) {
        this.telefoneEmergencia = telefoneEmergencia;
    }

    public String getCanalIntegracao() {
        return canalIntegracao;
    }

    public void setCanalIntegracao(String canalIntegracao) {
        this.canalIntegracao = canalIntegracao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServicoEmergencia)) return false;
        ServicoEmergencia that = (ServicoEmergencia) o;
        return Objects.equals(nomeOrgao, that.nomeOrgao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nomeOrgao);
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", nomeOrgao, telefoneEmergencia);
    }
}
