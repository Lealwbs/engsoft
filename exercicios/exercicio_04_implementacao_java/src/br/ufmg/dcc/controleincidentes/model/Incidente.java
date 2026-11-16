package br.ufmg.dcc.controleincidentes.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Incidente {
    private String id;
    private LocalDateTime dataHora;
    private TipoIncidente tipo;
    private NivelGravidade gravidade;
    private StatusIncidente status;
    private String descricao;
    private Localizacao local;
    private AreaDeRisco areaDeRisco;
    private Relator relator;
    private final List<Veiculo> veiculosAlocados;
    private final List<ServicoEmergencia> servicosAcionados;

    public Incidente(String id, LocalDateTime dataHora, TipoIncidente tipo, NivelGravidade gravidade,
                     String descricao, Localizacao local, Relator relator) {
        this.id = id;
        this.dataHora = (dataHora != null) ? dataHora : LocalDateTime.now();
        this.tipo = tipo;
        this.gravidade = gravidade;
        this.status = StatusIncidente.REGISTRADO;
        this.descricao = descricao;
        this.local = local;
        this.relator = relator;
        this.veiculosAlocados = new ArrayList<>();
        this.servicosAcionados = new ArrayList<>();
    }

    public Incidente(String id, TipoIncidente tipo, NivelGravidade gravidade,
                     String descricao, Localizacao local, Relator relator) {
        this(id, LocalDateTime.now(), tipo, gravidade, descricao, local, relator);
    }

    public void registrar() {
        this.status = StatusIncidente.REGISTRADO;
    }

    public void atualizarStatus(StatusIncidente novo) {
        this.status = novo;
    }

    public void alocarVeiculo(Veiculo v) {
        if (v == null) {
            throw new IllegalArgumentException("Veículo não pode ser nulo");
        }
        if (!veiculosAlocados.contains(v)) {
            veiculosAlocados.add(v);
            v.setStatus(StatusVeiculo.A_CAMINHO);
            if (this.status == StatusIncidente.REGISTRADO) {
                atualizarStatus(StatusIncidente.EM_ATENDIMENTO);
            }
        }
    }

    public boolean precisaMaisVeiculos() {
        if (estaEmAreaDeRisco() && areaDeRisco.isExigeMultiplosVeiculos()) {
            if (veiculosAlocados.size() < areaDeRisco.getQtdMinimaVeiculos()) {
                return true;
            }
        }
        if (gravidade == NivelGravidade.ALTA && veiculosAlocados.size() < 2) {
            return true;
        }
        return veiculosAlocados.isEmpty();
    }

    public boolean estaEmAreaDeRisco() {
        return areaDeRisco != null && areaDeRisco.contem(local);
    }

    public void adicionarServicoAcionado(ServicoEmergencia servico) {
        if (servico != null && !servicosAcionados.contains(servico)) {
            servicosAcionados.add(servico);
        }
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public TipoIncidente getTipo() {
        return tipo;
    }

    public void setTipo(TipoIncidente tipo) {
        this.tipo = tipo;
    }

    public NivelGravidade getGravidade() {
        return gravidade;
    }

    public void setGravidade(NivelGravidade gravidade) {
        this.gravidade = gravidade;
    }

    public StatusIncidente getStatus() {
        return status;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Localizacao getLocal() {
        return local;
    }

    public void setLocal(Localizacao local) {
        this.local = local;
    }

    public AreaDeRisco getAreaDeRisco() {
        return areaDeRisco;
    }

    public void setAreaDeRisco(AreaDeRisco areaDeRisco) {
        this.areaDeRisco = areaDeRisco;
    }

    public Relator getRelator() {
        return relator;
    }

    public void setRelator(Relator relator) {
        this.relator = relator;
    }

    public List<Veiculo> getVeiculosAlocados() {
        return Collections.unmodifiableList(veiculosAlocados);
    }

    public List<ServicoEmergencia> getServicosAcionados() {
        return Collections.unmodifiableList(servicosAcionados);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Incidente)) return false;
        Incidente incidente = (Incidente) o;
        return Objects.equals(id, incidente.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Incidente %s [%s] - Gravidade: %s, Status: %s",
                id, tipo, gravidade, status);
    }
}
