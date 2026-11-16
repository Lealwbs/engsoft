package tests;

import static org.junit.Assert.*;

import br.ufmg.dcc.controleincidentes.model.*;
import br.ufmg.dcc.controleincidentes.service.ControleDespacho;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe de testes estruturada estritamente de acordo com os Slides 5 e 6
 * da aula prática de Testes de Software JUnit (Prof. Eduardo Figueiredo).
 * Demonstra o padrão canônico do template de teste:
 *   1) Eu tenho... (Cenário / Arrange)
 *   2) E eu faço... (Ação / Act)
 *   3) Então eu espero... (Assertivas / Assert)
 */
public class PrimeiraoTests {

    @Test
    public void testTemplate() {
        // 1) Eu tenho uma localização no centro de Belo Horizonte
        Localizacao centro = new Localizacao(-19.9200, -43.9400, "Centro", "Centro");

        // 2) E eu calculo a distância para o mesmo ponto
        double distancia = centro.calcularDistancia(centro);

        // 3) Então eu espero distância igual a zero
        assertEquals("Distância para o mesmo ponto deve ser zero", 0.0, distancia, 0.0001);
    }

    @Test
    public void test1() {
        // 1) Eu tenho um relator policial e um chamado de emergência médica
        Policial relator = new Policial("P-01", "Cabo Lima", "31-9999-0000", "MAT-123", "BPM");
        Localizacao loc = new Localizacao(-19.93, -43.93, "Av. Afonso Pena", "Centro");

        // 2) E eu registro o incidente no sistema
        Incidente inc = relator.relatarIncidente("INC-101", TipoIncidente.EMERGENCIA_MEDICA, NivelGravidade.ALTA, "Chamado SAMU", loc);

        // 3) Então eu espero que o incidente seja registrado com sucesso e associado ao relator
        assertNotNull("Incidente gerado não deve ser nulo", inc);
        assertEquals(StatusIncidente.REGISTRADO, inc.getStatus());
        assertEquals(relator, inc.getRelator());
        assertTrue("Incidente deve precisar de veículos inicialmente", inc.precisaMaisVeiculos());
    }

    @Test
    public void test2() {
        // 1) Eu tenho uma viatura com sinal GPS ativo
        Localizacao base = new Localizacao(-19.91, -43.92, "Base Operacional", "Leste");
        ViaturaPolicial vp = new ViaturaPolicial("V-01", "VP-01", base, true, false);

        // 2) E eu desativo o sinal GPS simulando perda de telemetria
        vp.setSinalGpsAtivo(false);
        Localizacao posEstimada = vp.solicitarPosicao();

        // 3) Então eu espero que o sistema estime a posição automaticamente
        assertNotNull("Posição estimada não pode ser nula", posEstimada);
        assertTrue("Endereço deve conter a indicação de estimativa", posEstimada.getEndereco().contains("Estimada"));
    }

    @Test
    public void test3() {
        // 1) Eu tenho uma área de risco que exige contingente mínimo de 2 viaturas
        Localizacao centroRisco = new Localizacao(-19.95, -43.91, "Serra", "Serra");
        AreaDeRisco area = new AreaDeRisco("Área Crítica", 4, true, 2, centroRisco, 1.5);
        ControleDespacho despacho = new ControleDespacho();

        Incidente inc = new Incidente("INC-CRIT", TipoIncidente.ASSALTO, NivelGravidade.ALTA, "Ocorrência", centroRisco, null);
        inc.setAreaDeRisco(area);

        // 2) E eu tento validar o envio com apenas 1 viatura
        List<Veiculo> contingenteIncompleto = new ArrayList<>();
        contingenteIncompleto.add(new ViaturaPolicial("VP-1", "V-1", centroRisco, false, false));
        boolean seguroUnitario = despacho.validarEnvioSeguro(inc, contingenteIncompleto);

        // E valido com 2 viaturas (contingente seguro)
        contingenteIncompleto.add(new ViaturaPolicial("VP-2", "V-2", centroRisco, true, true));
        boolean seguroComboio = despacho.validarEnvioSeguro(inc, contingenteIncompleto);

        // 3) Então eu espero que 1 viatura seja bloqueada e 2 sejam liberadas
        assertFalse("Envio solitário em área de risco deve ser rejeitado", seguroUnitario);
        assertTrue("Envio com contingente mínimo deve ser aprovado", seguroComboio);
    }
}
