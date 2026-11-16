package br.ufmg.dcc.controleincidentes.test;

import static org.junit.Assert.*;

import br.ufmg.dcc.controleincidentes.model.*;
import br.ufmg.dcc.controleincidentes.service.ControleDespacho;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Testes unitários para a classe de serviço ControleDespacho utilizando JUnit 4.
 * Valida os 5 fatores essenciais da especificação do sistema de controle de incidentes:
 *   (i)   Tipo e urgência do incidente;
 *   (ii)  Posicionamento e proximidade dos veículos disponíveis;
 *   (iii) Alocação de viaturas especializadas conforme a tipologia da ocorrência;
 *   (iv)  Diretrizes de segurança para áreas de risco (proibição de viatura solitária);
 *   (v)   Disparo automatizado de alertas para serviços de emergência parceiros.
 */
public class ControleDespachoTest {

    private ControleDespacho despacho;
    private Localizacao localCentro;
    private Localizacao localSavassi;
    private Localizacao localPampulha;
    private Policial policialRelator;
    private Bombeiro bombeiroRelator;

    private ViaturaPolicial viaturaPerto;
    private ViaturaPolicial viaturaLonge;
    private CaminhaoBombeiro caminhao;
    private Ambulancia ambulancia;

    private List<Veiculo> frotaCompleta;
    private ServicoEmergencia samu;
    private ServicoEmergencia bombeiros;
    private ServicoEmergencia policiaMilitar;

    @Before
    public void setUp() {
        // 1) Eu tenho o serviço de despacho, localizações e frota de viaturas inicializadas
        despacho = new ControleDespacho();

        localCentro = new Localizacao(-19.9200, -43.9400, "Centro", "Centro");
        localSavassi = new Localizacao(-19.9333, -43.9378, "Savassi", "Savassi");
        localPampulha = new Localizacao(-19.8550, -43.9600, "Pampulha", "Pampulha");

        policialRelator = new Policial("POL-01", "Tenente Barros", "31-98888-1111", "MAT-111", "5º BPM");
        bombeiroRelator = new Bombeiro("BOM-02", "Capitão Faria", "31-97777-2222", "MAT-222", "1º BBM");

        // Viaturas posicionadas estrategicamente
        viaturaPerto = new ViaturaPolicial("VP-CENTRO", "VP-01", localCentro, false, false);
        viaturaLonge = new ViaturaPolicial("VP-PAMPULHA", "VP-02", localPampulha, true, true);
        caminhao = new CaminhaoBombeiro("CB-CENTRO", "CB-01", localCentro, 6000.0, true);
        ambulancia = new Ambulancia("AMB-SAVASSI", "SAMU-01", localSavassi, true, true);

        frotaCompleta = new ArrayList<>();
        frotaCompleta.add(viaturaPerto);
        frotaCompleta.add(viaturaLonge);
        frotaCompleta.add(caminhao);
        frotaCompleta.add(ambulancia);

        // Órgãos parceiros de emergência
        samu = new ServicoEmergencia("SAMU - Serviço de Atendimento Móvel de Urgência", "192", "REST-API-SAMU");
        bombeiros = new ServicoEmergencia("Corpo de Bombeiros Militar de MG", "193", "REST-API-CBMMG");
        policiaMilitar = new ServicoEmergencia("Polícia Militar de Minas Gerais", "190", "REST-API-PMMG");

        despacho.cadastrarOrgaoParceiro(samu);
        despacho.cadastrarOrgaoParceiro(bombeiros);
        despacho.cadastrarOrgaoParceiro(policiaMilitar);
    }

    @Test
    public void testFator3_DespachoPriorizaCaminhaoBombeiroParaIncendio() {
        // 1) Eu tenho um incidente de incêndio registrado
        Incidente incendio = bombeiroRelator.relatarIncidente(
                "INC-FOGO-10",
                TipoIncidente.INCENDIO,
                NivelGravidade.MEDIA,
                "Incêndio em imóvel comercial",
                localCentro
        );

        // 2) E eu solicito o despacho dos veículos apropriados
        List<Veiculo> despachados = despacho.selecionarVeiculoApropriado(incendio, frotaCompleta);

        // 3) Então o despacho deve obrigatoriamente alocar o CaminhaoBombeiro especializado
        assertFalse("Deve retornar veículos despachados", despachados.isEmpty());
        boolean temCaminhao = despachados.stream().anyMatch(v -> v instanceof CaminhaoBombeiro);
        assertTrue("Incêndio deve ter caminhão de bombeiro alocado", temCaminhao);
        assertEquals("Status do caminhão deve mudar para A_CAMINHO", StatusVeiculo.A_CAMINHO, caminhao.getStatus());
        assertEquals("Incidente deve mudar para EM_ATENDIMENTO", StatusIncidente.EM_ATENDIMENTO, incendio.getStatus());
    }

    @Test
    public void testFator3_DespachoPriorizaAmbulanciaParaEmergenciaMedica() {
        // 1) Eu tenho um chamado de emergência médica
        Incidente med = policialRelator.relatarIncidente(
                "INC-MED-20",
                TipoIncidente.EMERGENCIA_MEDICA,
                NivelGravidade.MEDIA,
                "Indivíduo com suspeita de infarto em praça pública",
                localSavassi
        );

        // 2) E processo a seleção de viaturas
        List<Veiculo> despachados = despacho.selecionarVeiculoApropriado(med, frotaCompleta);

        // 3) Então uma ambulância deve ser imediatamente alocada
        assertFalse("Deve encontrar viatura disponível", despachados.isEmpty());
        boolean temAmbulancia = despachados.stream().anyMatch(v -> v instanceof Ambulancia);
        assertTrue("Emergência médica deve alocar uma ambulância", temAmbulancia);
    }

    @Test
    public void testFator3_DespachoPriorizaViaturaPolicialParaAssalto() {
        // 1) Eu tenho uma ocorrência de assalto à mão armada
        Incidente assalto = policialRelator.relatarIncidente(
                "INC-ASS-30",
                TipoIncidente.ASSALTO,
                NivelGravidade.MEDIA,
                "Roubo em agência bancária",
                localCentro
        );

        // 2) E realizo a seleção de viaturas
        List<Veiculo> despachados = despacho.selecionarVeiculoApropriado(assalto, frotaCompleta);

        // 3) Então uma viatura policial deve ser priorizada
        assertFalse(despachados.isEmpty());
        boolean temPolicial = despachados.stream().anyMatch(v -> v instanceof ViaturaPolicial);
        assertTrue("Assalto deve priorizar viatura policial", temPolicial);
    }

    @Test
    public void testFator2_DespachoSelecionaVeiculoMaisProximoEntreIguais() {
        // 1) Eu tenho duas viaturas policiais, uma no Centro (0 km do alvo) e outra na Pampulha (> 8 km)
        Incidente assaltoNoCentro = policialRelator.relatarIncidente(
                "INC-CENTRO-01",
                TipoIncidente.ASSALTO,
                NivelGravidade.BAIXA,
                "Furto simples no Centro",
                localCentro
        );

        // Lista contendo apenas viaturas policiais
        List<Veiculo> policiaisDisponiveis = new ArrayList<>();
        policiaisDisponiveis.add(viaturaLonge);
        policiaisDisponiveis.add(viaturaPerto);

        // 2) E eu despacho uma viatura para o incidente
        List<Veiculo> despachados = despacho.selecionarVeiculoApropriado(assaltoNoCentro, policiaisDisponiveis);

        // 3) Então a viatura mais próxima (Centro) deve ser escolhida em detrimento da distante (Pampulha)
        assertEquals("Deve alocar 1 viatura para gravidade baixa fora de área de risco", 1, despachados.size());
        assertEquals("Viatura selecionada deve ser a mais próxima do local", viaturaPerto, despachados.get(0));
    }

    @Test
    public void testFator4_BloqueioDespachoViaturaUnicaEmAreaDeRisco() {
        // 1) Eu tenho um incidente localizado em área de risco que exige no mínimo 2 veículos
        AreaDeRisco favelaSerra = new AreaDeRisco("Serra Perigosa", 4, true, 2, localCentro, 2.0);
        Incidente incRisco = policialRelator.relatarIncidente(
                "INC-RISCO-01",
                TipoIncidente.ASSALTO,
                NivelGravidade.BAIXA,
                "Denúncia em área conflagrada",
                localCentro
        );
        incRisco.setAreaDeRisco(favelaSerra);

        // Apenas UMA viatura disponível na frota
        List<Veiculo> frotaSolitaria = new ArrayList<>();
        frotaSolitaria.add(viaturaPerto);

        // 2) E tento despachar viatura solitária para a área perigosa
        boolean seguro = despacho.validarEnvioSeguro(incRisco, frotaSolitaria);
        List<Veiculo> despachados = despacho.selecionarVeiculoApropriado(incRisco, frotaSolitaria);

        // 3) Então por norma de segurança o envio unitário deve ser BLOQUEADO
        assertFalse("Envio de apenas 1 viatura em área de risco deve ser considerado inseguro", seguro);
        assertTrue("Despacho inseguro deve retornar lista vazia de envio", despachados.isEmpty());
        assertEquals("Status do incidente deve permanecer REGISTRADO", StatusIncidente.REGISTRADO, incRisco.getStatus());
    }

    @Test
    public void testFator4_AutorizacaoDespachoEmComboioParaAreaDeRisco() {
        // 1) Eu tenho incidente em área de risco e frota com múltiplos veículos
        AreaDeRisco favelaSerra = new AreaDeRisco("Serra Perigosa", 4, true, 2, localCentro, 2.0);
        Incidente incRisco = policialRelator.relatarIncidente(
                "INC-RISCO-02",
                TipoIncidente.ASSALTO,
                NivelGravidade.MEDIA,
                "Operação em área de risco",
                localCentro
        );
        incRisco.setAreaDeRisco(favelaSerra);

        // 2) E executo o despacho com a frota completa
        List<Veiculo> despachados = despacho.selecionarVeiculoApropriado(incRisco, frotaCompleta);

        // 3) Então o comboio de pelo menos 2 viaturas deve ser despachado com sucesso
        assertTrue("Envio deve atender contingente mínimo da área de risco", despachados.size() >= 2);
        assertTrue("Despacho seguro aprovado", despacho.validarEnvioSeguro(incRisco, despachados));
        assertEquals(StatusIncidente.EM_ATENDIMENTO, incRisco.getStatus());
    }

    @Test
    public void testFator5_AlertaAutomaticoServicosEmergenciaEmGravidadeAlta() {
        // 1) Eu tenho um incidente de gravidade ALTA
        Incidente grave = policialRelator.relatarIncidente(
                "INC-GRAVE-99",
                TipoIncidente.ACIDENTE_TRANSITO,
                NivelGravidade.ALTA,
                "Engavetamento grave com múltiplas vítimas presas em ferragens",
                localCentro
        );

        // 2) E realizo o despacho
        despacho.selecionarVeiculoApropriado(grave, frotaCompleta);

        // 3) Então os órgãos parceiros (SAMU, Bombeiros, PM) devem ser acionados automaticamente
        List<ServicoEmergencia> acionados = grave.getServicosAcionados();
        assertEquals("Todos os órgãos cadastrados devem ser acionados para gravidade ALTA", 3, acionados.size());
        assertTrue(acionados.contains(samu));
        assertTrue(acionados.contains(bombeiros));
        assertTrue(acionados.contains(policiaMilitar));
    }

    @Test
    public void testFator5_AlertaAutomaticoDisparadoParaQualquerIncendio() {
        // 1) Eu tenho um chamado de incêndio mesmo com gravidade MÉDIA
        Incidente fogoMedio = bombeiroRelator.relatarIncidente(
                "INC-FOGO-33",
                TipoIncidente.INCENDIO,
                NivelGravidade.MEDIA,
                "Princípio de incêndio em residência",
                localCentro
        );

        // 2) E disparo os alertas
        despacho.acionarServicosEmergencia(fogoMedio);

        // 3) Então deve emitir alerta a serviços de emergência parceiros
        assertFalse("Incêndio deve acionar serviços parceiros compulsoriamente",
                fogoMedio.getServicosAcionados().isEmpty());
    }

    @Test
    public void testFator5_NaoAlertaEmIncidenteBaixaGravidadeForaDeAreaDeRisco() {
        // 1) Eu tenho uma ocorrência rotineira de baixa gravidade fora de área de risco
        Incidente rotina = policialRelator.relatarIncidente(
                "INC-ROTINA-01",
                TipoIncidente.ASSALTO,
                NivelGravidade.BAIXA,
                "Perda de documentos em via pública",
                localCentro
        );

        // 2) E submeto à rotina de alerta
        despacho.acionarServicosEmergencia(rotina);

        // 3) Então nenhum parceiro externo deve ser acionado para evitar sobrecarga de alertas
        assertTrue("Incidente de baixa gravidade não deve disparar alertas automáticos",
                rotina.getServicosAcionados().isEmpty());
    }

    @Test
    public void testCadastroDeOrgaosParceirosNaoDuplica() {
        // 1) Eu tenho um novo órgão parceiro (Defesa Civil)
        ServicoEmergencia defesaCivil = new ServicoEmergencia("Defesa Civil Estadual", "199", "REST-API-DEFESA");

        // 2) E cadastro esse órgão duas vezes no serviço de despacho
        despacho.cadastrarOrgaoParceiro(defesaCivil);
        despacho.cadastrarOrgaoParceiro(defesaCivil);

        // 3) Então o cadastro deve ser idempotente
        assertTrue(despacho.getOrgaosParceiros().contains(defesaCivil));
        long count = despacho.getOrgaosParceiros().stream().filter(o -> o.equals(defesaCivil)).count();
        assertEquals("Não deve duplicar órgão parceiro", 1, count);
    }
}
