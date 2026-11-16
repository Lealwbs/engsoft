package br.ufmg.dcc.controleincidentes.test;

import static org.junit.Assert.*;

import br.ufmg.dcc.controleincidentes.model.AreaDeRisco;
import br.ufmg.dcc.controleincidentes.model.Localizacao;
import org.junit.Before;
import org.junit.Test;

/**
 * Testes unitários para a classe AreaDeRisco utilizando JUnit 4.
 * Valida verificação de perímetro espacial, regras de contenção e requisitos de contingente mínimo.
 */
public class AreaDeRiscoTest {

    private AreaDeRisco areaSerra;
    private Localizacao centroSerra;

    @Before
    public void setUp() {
        // 1) Eu tenho um ponto central na Serra e uma área de risco de raio 2.0 km
        centroSerra = new Localizacao(-19.9500, -43.9100, "Serra Central", "Serra");
        areaSerra = new AreaDeRisco("Aglomorado da Serra", 4, true, 2, centroSerra, 2.0);
    }

    @Test
    public void testPontoInternoAoRaioDaAreaDeRisco() {
        // 1) Eu tenho uma localização próxima ao centro da área (a aprox. 150 metros)
        Localizacao pontoInterno = new Localizacao(-19.9510, -43.9105, "Rua Interna", "Serra");

        // 2) E eu verifico se a área contém o ponto
        boolean contido = areaSerra.contem(pontoInterno);

        // 3) Então eu espero que o retorno seja verdadeiro
        assertTrue("Ponto próximo ao centro deve estar dentro da área de risco", contido);
    }

    @Test
    public void testPontoExternoAoRaioDaAreaDeRisco() {
        // 1) Eu tenho uma localização distante (ex.: campus UFMG na Pampulha, aprox. 12 km)
        Localizacao pontoExterno = new Localizacao(-19.8690, -43.9630, "Campus UFMG", "Pampulha");

        // 2) E eu verifico se a área contém esse ponto distante
        boolean contido = areaSerra.contem(pontoExterno);

        // 3) Então eu espero que o retorno seja falso
        assertFalse("Ponto distante deve estar fora da área de risco", contido);
    }

    @Test
    public void testContemComPontoNuloRetornaFalsoSemExcecao() {
        // 1) Eu tenho uma área de risco válida
        // 2) E eu testo se contém uma localização nula
        boolean contido = areaSerra.contem(null);

        // 3) Então eu espero falso de maneira segura sem disparar NullPointerException
        assertFalse("Localização nula não deve ser considerada contida", contido);
    }

    @Test
    public void testAreaDeRiscoSemCentroRetornaFalso() {
        // 1) Eu tenho uma área de risco cujo centro não foi definido
        AreaDeRisco areaSemCentro = new AreaDeRisco("Área Sem Centro", 1, false, 1, null, 1.0);
        Localizacao ponto = new Localizacao(-19.95, -43.91, "Ponto Qualquer", "Bairro");

        // 2) E eu tento verificar se contém o ponto
        boolean contido = areaSemCentro.contem(ponto);

        // 3) Então eu espero falso
        assertFalse("Área sem centro não pode conter nenhum ponto", contido);
    }

    @Test
    public void testConfiguracaoDeSegurancaDaArea() {
        // 1) Eu tenho uma área de risco configurada com exigência de múltiplos veículos
        // 2) E consulto as diretrizes de segurança
        // 3) Então eu espero conformidade com as regras operacionais da especificação
        assertEquals("Nome da área incorreto", "Aglomorado da Serra", areaSerra.getNome());
        assertEquals("Nível de perigo deve ser 4", 4, areaSerra.getNivelPerigo());
        assertTrue("Deve exigir múltiplos veículos para atendimento seguro", areaSerra.isExigeMultiplosVeiculos());
        assertEquals("Contingente mínimo exigido deve ser 2", 2, areaSerra.getQtdMinimaVeiculos());
        assertEquals(2.0, areaSerra.getRaioKm(), 0.0001);
    }

    @Test
    public void testModificacaoDePropriedadesDaArea() {
        // 1) Eu tenho uma área de risco
        // 2) E eu atualizo seus atributos operacionais
        areaSerra.setNome("Serra - Zona Crítica");
        areaSerra.setNivelPerigo(5);
        areaSerra.setQtdMinimaVeiculos(3);
        areaSerra.setRaioKm(3.5);

        // 3) Então eu espero que as alterações sejam refletidas
        assertEquals("Serra - Zona Crítica", areaSerra.getNome());
        assertEquals(5, areaSerra.getNivelPerigo());
        assertEquals(3, areaSerra.getQtdMinimaVeiculos());
        assertEquals(3.5, areaSerra.getRaioKm(), 0.0001);
    }
}
