package br.ufmg.dcc.controleincidentes.test;

import br.ufmg.dcc.controleincidentes.model.*;
import br.ufmg.dcc.controleincidentes.service.ControleDespacho;

import java.util.ArrayList;
import java.util.List;

public class TesteSistemaControleIncidentes {
    private static int total = 0;
    private static int passou = 0;

    public static void main(String[] args) {
        System.out.println("Executando testes do sistema de controle de incidentes...");

        testeCalculoDistancia();
        testeAreaDeRisco();
        testeGpsOfflineEEstimativa();
        testeCicloDeVidaIncidente();
        testeEnvioSeguroAreaRisco();
        testeAlertaServicosEmergencia();
        testeRegistroPorRelator();
        testePrioridadeVeiculoEspecializado();

        System.out.printf("%nResultado: %d de %d testes passaram.%n", passou, total);
        if (passou != total) {
            System.err.println("Erro: algum teste falhou!");
            System.exit(1);
        }
    }

    private static void assertEqual(boolean condicao, String nome) {
        total++;
        if (condicao) {
            passou++;
            System.out.println(" [OK] " + nome);
        } else {
            System.err.println(" [FALHA] " + nome);
        }
    }

    private static void testeCalculoDistancia() {
        Localizacao p1 = new Localizacao(-19.9191, -43.9386, "Centro", "Centro");
        Localizacao p2 = new Localizacao(-19.9385, -43.9332, "Savassi", "Savassi");
        double d = p1.calcularDistancia(p2);
        assertEqual(d > 1.8 && d < 2.6, "Cálculo de distância geodésica");
    }

    private static void testeAreaDeRisco() {
        Localizacao centro = new Localizacao(-19.95, -43.91, "Serra", "Serra");
        AreaDeRisco area = new AreaDeRisco("Área de Teste", 3, true, 2, centro, 2.0);

        Localizacao dentro = new Localizacao(-19.951, -43.909, "Ponto interno", "Serra");
        Localizacao fora = new Localizacao(-19.85, -43.95, "Ponto externo", "Pampulha");

        assertEqual(area.contem(dentro), "Ponto dentro da área de risco");
        assertEqual(!area.contem(fora), "Ponto fora da área de risco");
    }

    private static void testeGpsOfflineEEstimativa() {
        Localizacao loc = new Localizacao(-19.92, -43.94, "Rua A", "Bairro");
        ViaturaPolicial vp = new ViaturaPolicial("V1", "VP-01", loc, true, false);

        assertEqual(vp.solicitarPosicao().equals(loc), "GPS online retorna posição exata");

        vp.setSinalGpsAtivo(false);
        Localizacao estimativa = vp.solicitarPosicao();
        assertEqual(estimativa != null && estimativa.getEndereco().contains("Estimada"),
                "GPS offline recorre à estimativa de posição");
    }

    private static void testeCicloDeVidaIncidente() {
        Localizacao loc = new Localizacao(-19.92, -43.94, "Rua B", "Bairro");
        Policial p = new Policial("P1", "Cabo Lima", "9999-0000", "123", "BPM");
        Incidente inc = p.relatarIncidente("INC-T1", TipoIncidente.ACIDENTE_TRANSITO, NivelGravidade.BAIXA, "Teste", loc);

        assertEqual(inc.getStatus() == StatusIncidente.REGISTRADO, "Status inicial é Registrado");

        Ambulancia amb = new Ambulancia("A1", "AMB-01", loc, false, false);
        inc.alocarVeiculo(amb);

        assertEqual(inc.getStatus() == StatusIncidente.EM_ATENDIMENTO, "Incidente passa para Em Atendimento");
        assertEqual(amb.getStatus() == StatusVeiculo.A_CAMINHO, "Veículo passa para A Caminho");
        assertEqual(inc.getVeiculosAlocados().contains(amb), "Veículo alocado na lista do incidente");
    }

    private static void testeEnvioSeguroAreaRisco() {
        ControleDespacho despacho = new ControleDespacho();
        Localizacao loc = new Localizacao(-19.95, -43.91, "Rua C", "Serra");
        AreaDeRisco area = new AreaDeRisco("Serra", 4, true, 2, loc, 1.0);

        Policial pol = new Policial("P1", "Ten. Rocha", "9888-1111", "456", "BPM");
        Incidente inc = pol.relatarIncidente("INC-R1", TipoIncidente.ASSALTO, NivelGravidade.ALTA, "Assalto", loc);
        inc.setAreaDeRisco(area);

        List<Veiculo> umaViatura = new ArrayList<>();
        umaViatura.add(new ViaturaPolicial("V1", "P-1", loc, false, false));
        assertEqual(!despacho.validarEnvioSeguro(inc, umaViatura), "Bloqueia viatura única em área de risco");

        List<Veiculo> duasViaturas = new ArrayList<>(umaViatura);
        duasViaturas.add(new ViaturaPolicial("V2", "P-2", loc, true, true));
        assertEqual(despacho.validarEnvioSeguro(inc, duasViaturas), "Permite envio com contingente mínimo");
    }

    private static void testeAlertaServicosEmergencia() {
        ControleDespacho despacho = new ControleDespacho();
        ServicoEmergencia orgao = new ServicoEmergencia("Defesa Civil", "199", "DEFESA-API");
        despacho.cadastrarOrgaoParceiro(orgao);

        Localizacao loc = new Localizacao(-19.92, -43.94, "Centro", "Centro");
        Bombeiro b = new Bombeiro("B1", "Ten. Mendes", "9777-2222", "987", "BBM");
        Incidente inc = b.relatarIncidente("INC-G1", TipoIncidente.INCENDIO, NivelGravidade.ALTA, "Incêndio", loc);

        despacho.acionarServicosEmergencia(inc);
        assertEqual(inc.getServicosAcionados().contains(orgao), "Alerta automático enviado para órgão parceiro");
    }

    private static void testeRegistroPorRelator() {
        Policial pol = new Policial("P1", "Sgt. Dias", "9666-3333", "3321", "22º BPM");
        Incidente inc = pol.relatarIncidente("INC-REL", TipoIncidente.ASSALTO, NivelGravidade.MEDIA, "Assalto",
                new Localizacao(-19.92, -43.93, "Av. Afonso Pena", "Centro"));

        assertEqual(inc.getRelator() != null && inc.getRelator().equals(pol), "Incidente associado ao relator policial");
    }

    private static void testePrioridadeVeiculoEspecializado() {
        ControleDespacho despacho = new ControleDespacho();
        Localizacao loc = new Localizacao(-19.92, -43.93, "Centro", "Centro");

        List<Veiculo> frota = new ArrayList<>();
        frota.add(new ViaturaPolicial("V1", "VP-1", loc, false, false));
        frota.add(new CaminhaoBombeiro("C1", "CB-1", loc, 4000, true));
        frota.add(new Ambulancia("A1", "AMB-1", loc, true, true));

        Bombeiro b = new Bombeiro("B1", "Capitão", "9555-4444", "111", "BBM");
        Incidente inc = b.relatarIncidente("INC-F1", TipoIncidente.INCENDIO, NivelGravidade.MEDIA, "Fogo", loc);

        List<Veiculo> selecionados = despacho.selecionarVeiculoApropriado(inc, frota);
        boolean temCaminhao = selecionados.stream().anyMatch(v -> v instanceof CaminhaoBombeiro);
        assertEqual(temCaminhao, "Incêndio prioriza caminhão de bombeiro");
    }
}
