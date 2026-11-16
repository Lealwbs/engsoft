package br.ufmg.dcc.controleincidentes.test;

import static org.junit.Assert.*;

import br.ufmg.dcc.controleincidentes.model.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Testes unitários para a hierarquia polimórfica de Relator utilizando JUnit 4.
 * Cobre registro de ocorrências por policiais e bombeiros, métodos sobrecarregados
 * e validação de vínculo entre o relator e o incidente gerado.
 */
public class RelatorTest {

    private Policial policial;
    private Bombeiro bombeiro;
    private Localizacao localOcorrencia;

    @Before
    public void setUp() {
        // 1) Eu tenho dois relatores institucionais e uma localização de ocorrência
        policial = new Policial("REL-P01", "Sargento Silva", "(31) 98765-4321", "PM-12345", "1º BPM");
        bombeiro = new Bombeiro("REL-B02", "Tenente Oliveira", "(31) 99876-5432", "CB-67890", "3º BBM");
        localOcorrencia = new Localizacao(-19.9200, -43.9400, "Av. Amazonas, 100", "Centro");
    }

    @Test
    public void testPolicialRelataIncidenteComSucesso() {
        // 1) Eu tenho um policial relator em serviço
        // 2) E o policial registra um incidente de assalto
        Incidente inc = policial.relatarIncidente(
                "INC-ASSALTO-01",
                TipoIncidente.ASSALTO,
                NivelGravidade.ALTA,
                "Assalto a estabelecimento comercial em andamento",
                localOcorrencia
        );

        // 3) Então eu espero que o incidente seja criado e vinculado corretamente ao relator
        assertNotNull("O incidente gerado não deve ser nulo", inc);
        assertEquals("INC-ASSALTO-01", inc.getId());
        assertEquals(TipoIncidente.ASSALTO, inc.getTipo());
        assertEquals(NivelGravidade.ALTA, inc.getGravidade());
        assertEquals(StatusIncidente.REGISTRADO, inc.getStatus());
        assertEquals("Relator deve ser o policial responsável", policial, inc.getRelator());
        assertTrue("Relator deve ser uma instância de Policial", inc.getRelator() instanceof Policial);
    }

    @Test
    public void testBombeiroRelataIncidenteComSucesso() {
        // 1) Eu tenho um bombeiro relator em prontidão
        // 2) E o bombeiro relata um chamado de incêndio
        Incidente inc = bombeiro.relatarIncidente(
                "INC-FOGO-02",
                TipoIncidente.INCENDIO,
                NivelGravidade.ALTA,
                "Fogo em galpão de reciclagem com risco de propagação",
                localOcorrencia
        );

        // 3) Então eu espero que o incidente reflita a tipologia e o relator bombeiro
        assertNotNull(inc);
        assertEquals(TipoIncidente.INCENDIO, inc.getTipo());
        assertEquals("Tenente Oliveira", inc.getRelator().getNome());
        assertTrue("Relator deve ser uma instância de Bombeiro", inc.getRelator() instanceof Bombeiro);
    }

    @Test
    public void testSobrecargaRelatarIncidenteComDadosResumidos() {
        // 1) Eu tenho um relator policial
        // 2) E ele relata ocorrência pela assinatura simplificada de texto
        Incidente inc = policial.relatarIncidente("Colisão frontal entre dois carros na Av. Contorno");

        // 3) Então eu espero que um incidente com defaults válidos seja estruturado
        assertNotNull(inc);
        assertNotNull(inc.getId());
        assertEquals("Colisão frontal entre dois carros na Av. Contorno", inc.getDescricao());
        assertEquals(policial, inc.getRelator());
        assertEquals(StatusIncidente.REGISTRADO, inc.getStatus());
    }

    @Test
    public void testAtendimentoDeChamadoPolimorfico() {
        // 1) Eu tenho instâncias polimórficas de Relator
        Relator r1 = policial;
        Relator r2 = bombeiro;

        // 2) E executo suas rotinas de atendimento
        // 3) Então espero execução normal e integridade dos atributos específicos
        assertEquals("PM-12345", policial.getMatricula());
        assertEquals("1º BPM", policial.getBatalhao());
        assertEquals("CB-67890", bombeiro.getMatricula());
        assertEquals("3º BBM", bombeiro.getCorporacao());

        policial.atenderChamado();
        bombeiro.atenderChamado();
    }

    @Test
    public void testEqualsEHashCodeRelator() {
        // 1) Eu tenho dois policiais com mesmo id e outro com id distinto
        Policial p1 = new Policial("REL-01", "Cabo Santos", "111", "M1", "BPM");
        Policial p2 = new Policial("REL-01", "Cabo Outro", "222", "M2", "BPM");
        Policial p3 = new Policial("REL-02", "Cabo Santos", "111", "M1", "BPM");

        // 2) E comparo as instâncias
        // 3) Então a igualdade baseada no ID deve ser mantida
        assertEquals(p1, p2);
        assertNotEquals(p1, p3);
        assertEquals(p1.hashCode(), p2.hashCode());
    }
}
