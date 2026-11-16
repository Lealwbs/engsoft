package br.ufmg.dcc.controleincidentes.test;

import static org.junit.Assert.*;

import br.ufmg.dcc.controleincidentes.model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Testes unitários para a classe Incidente utilizando JUnit 4.
 * Valida o ciclo de vida do incidente, alocação de viaturas, transições de estado,
 * verificação de necessidade de reforço em áreas de risco e associação de serviços acionados.
 */
public class IncidenteTest {

    private Incidente incidente;
    private Policial relator;
    private Localizacao localCentro;
    private ViaturaPolicial viatura1;
    private ViaturaPolicial viatura2;

    @Before
    public void setUp() {
        // 1) Eu tenho um incidente criado no centro e duas viaturas disponíveis
        localCentro = new Localizacao(-19.9200, -43.9400, "Rua dos Tupis, 400", "Centro");
        relator = new Policial("REL-01", "Cabo Martins", "31-9999-0000", "MAT-01", "1º BPM");
        incidente = new Incidente(
                "INC-001",
                TipoIncidente.ACIDENTE_TRANSITO,
                NivelGravidade.MEDIA,
                "Colisão com vítimas leves",
                localCentro,
                relator
        );
        viatura1 = new ViaturaPolicial("VP-1", "V-01", localCentro, false, false);
        viatura2 = new ViaturaPolicial("VP-2", "V-02", localCentro, true, true);
    }

    @Test
    public void testEstadoInicialDoIncidente() {
        // 1) Eu tenho um incidente recém-criado
        // 2) E verifico suas propriedades iniciais
        // 3) Então o status deve ser REGISTRADO e as listas de alocação vazias
        assertEquals("Status inicial deve ser REGISTRADO", StatusIncidente.REGISTRADO, incidente.getStatus());
        assertTrue("Lista de veículos alocados deve iniciar vazia", incidente.getVeiculosAlocados().isEmpty());
        assertTrue("Lista de serviços acionados deve iniciar vazia", incidente.getServicosAcionados().isEmpty());
        assertEquals("INC-001", incidente.getId());
        assertEquals(TipoIncidente.ACIDENTE_TRANSITO, incidente.getTipo());
        assertEquals(NivelGravidade.MEDIA, incidente.getGravidade());
        assertNotNull("Data/Hora deve ser preenchida automaticamente", incidente.getDataHora());
        assertEquals(relator, incidente.getRelator());
    }

    @Test
    public void testAlocarPrimeiroVeiculoMudaStatusParaEmAtendimento() {
        // 1) Eu tenho um incidente REGISTRADO e uma viatura DISPONIVEL
        assertEquals(StatusIncidente.REGISTRADO, incidente.getStatus());
        assertEquals(StatusVeiculo.DISPONIVEL, viatura1.getStatus());

        // 2) E eu aloco a viatura ao incidente
        incidente.alocarVeiculo(viatura1);

        // 3) Então o status do incidente muda para EM_ATENDIMENTO e da viatura para A_CAMINHO
        assertEquals(StatusIncidente.EM_ATENDIMENTO, incidente.getStatus());
        assertEquals(StatusVeiculo.A_CAMINHO, viatura1.getStatus());
        assertEquals(1, incidente.getVeiculosAlocados().size());
        assertTrue(incidente.getVeiculosAlocados().contains(viatura1));
    }

    @Test
    public void testAlocarMultiplosVeiculosNaoPermiteDuplicidade() {
        // 1) Eu tenho um incidente
        // 2) E tento alocar a mesma viatura duas vezes
        incidente.alocarVeiculo(viatura1);
        incidente.alocarVeiculo(viatura1);

        // 3) Então a lista deve conter apenas uma ocorrência do veículo
        assertEquals("Não deve permitir viatura duplicada", 1, incidente.getVeiculosAlocados().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAlocarVeiculoNuloLancaExcecao() {
        // 1) Eu tenho um incidente
        // 2) E tento alocar null como veículo
        // 3) Então deve lançar IllegalArgumentException
        incidente.alocarVeiculo(null);
    }

    @Test
    public void testPrecisaMaisVeiculosEmAreaDeRisco() {
        // 1) Eu tenho uma área de risco que exige pelo menos 2 viaturas e um incidente dentro dela
        AreaDeRisco area = new AreaDeRisco("Zona Sul - Risco", 3, true, 2, localCentro, 1.5);
        incidente.setAreaDeRisco(area);

        // 2) E eu avalio a necessidade de mais veículos com 0, 1 e 2 veículos alocados
        // Com 0 veículos:
        assertTrue("Deve precisar de mais veículos sem nenhum alocado", incidente.precisaMaisVeiculos());

        // Aloca 1 viatura:
        incidente.alocarVeiculo(viatura1);
        assertTrue("Ainda deve precisar de reforço pois exige no mínimo 2", incidente.precisaMaisVeiculos());

        // Aloca a 2ª viatura:
        incidente.alocarVeiculo(viatura2);
        // 3) Então com o contingente mínimo atingido, não precisa de mais veículos
        assertFalse("Com 2 viaturas o contingente de risco foi satisfeito", incidente.precisaMaisVeiculos());
    }

    @Test
    public void testPrecisaMaisVeiculosEmGravidadeAlta() {
        // 1) Eu tenho um incidente de alta gravidade
        incidente.setGravidade(NivelGravidade.ALTA);

        // 2) E aloco apenas uma viatura
        incidente.alocarVeiculo(viatura1);

        // 3) Então alta gravidade exige no mínimo 2 viaturas
        assertTrue("Alta gravidade deve exigir mais veículos com apenas 1 alocado", incidente.precisaMaisVeiculos());

        incidente.alocarVeiculo(viatura2);
        assertFalse("Com 2 viaturas deve satisfazer a exigência de gravidade alta", incidente.precisaMaisVeiculos());
    }

    @Test
    public void testCicloFinalizarIncidente() {
        // 1) Eu tenho um incidente em atendimento
        incidente.alocarVeiculo(viatura1);
        assertEquals(StatusIncidente.EM_ATENDIMENTO, incidente.getStatus());

        // 2) E a ocorrência é resolvida
        incidente.atualizarStatus(StatusIncidente.FINALIZADO);

        // 3) Então o status deve ser FINALIZADO
        assertEquals(StatusIncidente.FINALIZADO, incidente.getStatus());
    }

    @Test
    public void testAdicionarServicoAcionadoSemDuplicidade() {
        // 1) Eu tenho um serviço parceiro (SAMU)
        ServicoEmergencia samu = new ServicoEmergencia("SAMU 192", "192", "API-SAMU");

        // 2) E adiciono o serviço acionado ao incidente duas vezes
        incidente.adicionarServicoAcionado(samu);
        incidente.adicionarServicoAcionado(samu);

        // 3) Então a lista deve conter exatamente 1 registro
        List<ServicoEmergencia> acionados = incidente.getServicosAcionados();
        assertEquals(1, acionados.size());
        assertEquals("SAMU 192", acionados.get(0).getNomeOrgao());
    }

    @Test
    public void testListaVeiculosImutavel() {
        // 1) Eu tenho a lista de veículos alocados
        incidente.alocarVeiculo(viatura1);
        List<Veiculo> lista = incidente.getVeiculosAlocados();

        // 2) E tento adicionar elemento diretamente na lista externa retornada
        // 3) Então deve lançar UnsupportedOperationException protegendo o encapsulamento
        try {
            lista.add(viatura2);
            fail("Deveria ter lançado UnsupportedOperationException para proteger encapsulamento");
        } catch (UnsupportedOperationException e) {
            // Sucesso esperado
            assertTrue(true);
        }
    }
}
