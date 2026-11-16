package br.ufmg.dcc.controleincidentes;

import br.ufmg.dcc.controleincidentes.model.*;
import br.ufmg.dcc.controleincidentes.service.ControleDespacho;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Sistema para Controle de Veículos em Incidentes - DCC/UFMG\n");

        // 1. Configuração dos órgãos parceiros (Polícia, Bombeiros, SAMU)
        ControleDespacho despacho = new ControleDespacho();
        despacho.cadastrarOrgaoParceiro(new ServicoEmergencia("Polícia Militar", "190", "API-COPOM"));
        despacho.cadastrarOrgaoParceiro(new ServicoEmergencia("Corpo de Bombeiros", "193", "API-COBOM"));
        despacho.cadastrarOrgaoParceiro(new ServicoEmergencia("SAMU", "192", "API-SAMU"));

        // 2. Bases e Frota de Veículos
        Localizacao baseSavassi = new Localizacao(-19.9385, -43.9332, "Praça Diogo de Vasconcelos", "Savassi");
        Localizacao baseCentro = new Localizacao(-19.9191, -43.9386, "Praça Sete", "Centro");
        Localizacao basePampulha = new Localizacao(-19.8580, -43.9720, "Av. Antônio Carlos", "Pampulha");
        Localizacao baseBarreiro = new Localizacao(-19.9750, -44.0200, "Av. Sinfrônio Brochado", "Barreiro");

        List<Veiculo> frota = new ArrayList<>();

        ViaturaPolicial vp1 = new ViaturaPolicial("VP-01", "ROTAM-101", baseCentro, true, true);
        ViaturaPolicial vp2 = new ViaturaPolicial("VP-02", "BPTRAN-202", baseSavassi, false, false);
        ViaturaPolicial vp3 = new ViaturaPolicial("VP-03", "TATICO-303", baseBarreiro, true, true);
        vp3.setSinalGpsAtivo(false); // Simulação de sinal GPS offline

        CaminhaoBombeiro cb1 = new CaminhaoBombeiro("CB-01", "AB-01", baseCentro, 5000.0, true);
        CaminhaoBombeiro cb2 = new CaminhaoBombeiro("CB-02", "AR-02", basePampulha, 3000.0, false);

        Ambulancia amb1 = new Ambulancia("AMB-01", "USA-01", baseSavassi, true, true);
        Ambulancia amb2 = new Ambulancia("AMB-02", "USB-02", basePampulha, false, false);

        frota.add(vp1);
        frota.add(vp2);
        frota.add(vp3);
        frota.add(cb1);
        frota.add(cb2);
        frota.add(amb1);
        frota.add(amb2);

        System.out.println("Frota cadastrada:");
        for (Veiculo v : frota) {
            System.out.printf(" - %s em %s (GPS: %s)%n", v, v.getPosicaoAtual().getBairro(), v.isSinalGpsAtivo() ? "OK" : "Offline");
        }
        System.out.println();

        // 3. Relatores e Áreas de Risco
        Policial relatorPolicial = new Policial("REL-P1", "Sgt. Silva", "(31) 98765-4321", "12345", "1º BPM");
        Bombeiro relatorBombeiro = new Bombeiro("REL-B1", "Cap. Oliveira", "(31) 99123-9876", "67890", "1º BBM");

        AreaDeRisco areaCritica = new AreaDeRisco("Serra Alta", 4, true, 3,
                new Localizacao(-19.9500, -43.9100, "Rua Alto da Serra", "Serra Alta"), 2.5);

        // --- Cenário 1: Acidente simples na Savassi ---
        System.out.println("--- Cenário 1: Acidente de trânsito simples ---");
        relatorPolicial.atenderChamado();
        Localizacao locAcidente = new Localizacao(-19.9350, -43.9350, "Av. Getúlio Vargas", "Savassi");
        Incidente inc1 = relatorPolicial.relatarIncidente("INC-01", TipoIncidente.ACIDENTE_TRANSITO,
                NivelGravidade.BAIXA, "Colisão traseira sem vítimas graves", locAcidente);

        List<Veiculo> despachados1 = despacho.selecionarVeiculoApropriado(inc1, frota);
        System.out.println("Veículos alocados para o incidente 01: " + despachados1);
        System.out.println("Status da ocorrência: " + inc1.getStatus() + "\n");

        // --- Cenário 2: Incêndio no Centro (Alta gravidade) ---
        System.out.println("--- Cenário 2: Incêndio de grandes proporções ---");
        relatorBombeiro.atenderChamado();
        Localizacao locIncendio = new Localizacao(-19.9180, -43.9390, "Rua da Bahia", "Centro");
        Incidente inc2 = relatorBombeiro.relatarIncidente("INC-02", TipoIncidente.INCENDIO,
                NivelGravidade.ALTA, "Incêndio em imóvel comercial", locIncendio);

        List<Veiculo> despachados2 = despacho.selecionarVeiculoApropriado(inc2, frota);
        System.out.println("Veículos alocados para o incêndio: " + despachados2);
        for (Veiculo v : despachados2) {
            if (v instanceof CaminhaoBombeiro) {
                ((CaminhaoBombeiro) v).combaterFogo();
            } else if (v instanceof Ambulancia) {
                ((Ambulancia) v).estabilizarPaciente();
            }
        }
        System.out.println("Serviços acionados: " + inc2.getServicosAcionados() + "\n");

        // --- Cenário 3: Ocorrência em Área de Risco ---
        System.out.println("--- Cenário 3: Assalto em área de risco ---");
        Localizacao locRisco = new Localizacao(-19.9510, -43.9090, "Beco da Paz", "Serra Alta");
        Incidente inc3 = relatorPolicial.relatarIncidente("INC-03", TipoIncidente.ASSALTO,
                NivelGravidade.ALTA, "Assalto com indivíduos armados", locRisco);
        inc3.setAreaDeRisco(areaCritica);

        System.out.println("Tentando despachar apenas 1 viatura para a área de risco:");
        List<Veiculo> apenasUm = new ArrayList<>();
        apenasUm.add(vp2);
        boolean seguro = despacho.validarEnvioSeguro(inc3, apenasUm);
        System.out.println("Envio seguro? " + (seguro ? "Sim" : "Não, proibido viatura isolada"));

        System.out.println("\nDespachando contingente completo via sistema:");
        List<Veiculo> despachados3 = despacho.selecionarVeiculoApropriado(inc3, frota);
        System.out.println("Veículos autorizados para incursão: " + despachados3);
        System.out.println("Incidente ainda precisa de veículos? " + (inc3.precisaMaisVeiculos() ? "Sim" : "Não\n"));

        // --- Cenário 4: Estimativa de posição (GPS offline) ---
        System.out.println("--- Cenário 4: Teste de estimativa de posição ---");
        System.out.println("Viatura " + vp3.getPrefixo() + " com sinal GPS offline.");
        System.out.println("Posição solicitada: " + vp3.solicitarPosicao());
        System.out.printf("Distância calculada até o Centro: %.2f km%n", vp3.calcularDistanciaAte(baseCentro));
    }
}
