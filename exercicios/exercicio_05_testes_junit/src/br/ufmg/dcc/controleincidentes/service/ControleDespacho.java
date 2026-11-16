package br.ufmg.dcc.controleincidentes.service;

import br.ufmg.dcc.controleincidentes.model.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ControleDespacho {
    private final List<ServicoEmergencia> orgaosParceiros;

    public ControleDespacho() {
        this.orgaosParceiros = new ArrayList<>();
    }

    public void cadastrarOrgaoParceiro(ServicoEmergencia orgao) {
        if (orgao != null && !orgaosParceiros.contains(orgao)) {
            orgaosParceiros.add(orgao);
        }
    }

    public List<Veiculo> selecionarVeiculoApropriado(Incidente inc, List<Veiculo> frota) {
        if (inc == null || frota == null || frota.isEmpty()) {
            return new ArrayList<>();
        }

        // Filtra os disponíveis e ordena pela menor distância até o local
        List<Veiculo> disponiveis = frota.stream()
                .filter(Veiculo::estaDisponivel)
                .sorted(Comparator.comparingDouble(v -> v.calcularDistanciaAte(inc.getLocal())))
                .collect(Collectors.toList());

        List<Veiculo> selecionados = new ArrayList<>();

        // Quantidade mínima exigida por área de risco ou por alta gravidade
        int qtdMinima = 1;
        if (inc.estaEmAreaDeRisco() && inc.getAreaDeRisco().isExigeMultiplosVeiculos()) {
            qtdMinima = Math.max(qtdMinima, inc.getAreaDeRisco().getQtdMinimaVeiculos());
        }
        if (inc.getGravidade() == NivelGravidade.ALTA) {
            qtdMinima = Math.max(qtdMinima, 2);
        }

        // Prioriza viaturas especializadas conforme o tipo do incidente
        switch (inc.getTipo()) {
            case INCENDIO:
                adicionarEspecializado(disponiveis, selecionados, CaminhaoBombeiro.class);
                adicionarEspecializado(disponiveis, selecionados, Ambulancia.class);
                break;
            case ASSALTO:
                adicionarEspecializado(disponiveis, selecionados, ViaturaPolicial.class);
                break;
            case EMERGENCIA_MEDICA:
                adicionarEspecializado(disponiveis, selecionados, Ambulancia.class);
                break;
            case ACIDENTE_TRANSITO:
                adicionarEspecializado(disponiveis, selecionados, Ambulancia.class);
                adicionarEspecializado(disponiveis, selecionados, ViaturaPolicial.class);
                break;
        }

        // Completa com os outros disponíveis mais próximos até atender a quantidade mínima
        for (Veiculo v : disponiveis) {
            if (selecionados.size() >= qtdMinima) {
                break;
            }
            if (!selecionados.contains(v)) {
                selecionados.add(v);
            }
        }

        // Validação de segurança em áreas de risco
        if (!validarEnvioSeguro(inc, selecionados)) {
            System.out.println("Aviso: Despacho não autorizado para o incidente " + inc.getId() + " - contingente insuficiente para área de risco.");
            return new ArrayList<>();
        }

        // Aloca os veículos selecionados
        for (Veiculo v : selecionados) {
            inc.alocarVeiculo(v);
        }

        // Alerta automático a serviços parceiros
        acionarServicosEmergencia(inc);

        return selecionados;
    }

    public boolean validarEnvioSeguro(Incidente inc, List<Veiculo> veiculos) {
        if (inc == null || veiculos == null || veiculos.isEmpty()) {
            return false;
        }

        if (inc.estaEmAreaDeRisco()) {
            AreaDeRisco area = inc.getAreaDeRisco();
            if (area.isExigeMultiplosVeiculos()) {
                // Em área perigosa, não permite envio de apenas um veículo
                if (veiculos.size() < 2 || veiculos.size() < area.getQtdMinimaVeiculos()) {
                    return false;
                }
            }
        }

        return true;
    }

    public void acionarServicosEmergencia(Incidente inc) {
        if (inc == null || orgaosParceiros.isEmpty()) {
            return;
        }

        boolean precisaAlertar = (inc.getGravidade() == NivelGravidade.ALTA)
                || (inc.getTipo() == TipoIncidente.INCENDIO)
                || (inc.estaEmAreaDeRisco());

        if (precisaAlertar) {
            for (ServicoEmergencia orgao : orgaosParceiros) {
                orgao.emitirAlertaAutomatico(inc);
                inc.adicionarServicoAcionado(orgao);
            }
        }
    }

    private <T extends Veiculo> void adicionarEspecializado(List<Veiculo> disponiveis, List<Veiculo> selecionados, Class<T> classeAlvo) {
        for (Veiculo v : disponiveis) {
            if (classeAlvo.isInstance(v) && !selecionados.contains(v)) {
                selecionados.add(v);
                break;
            }
        }
    }

    public List<ServicoEmergencia> getOrgaosParceiros() {
        return orgaosParceiros;
    }
}
