package br.ufmg.dcc.controleincidentes.test;

import static org.junit.Assert.*;

import br.ufmg.dcc.controleincidentes.model.Localizacao;
import org.junit.Before;
import org.junit.Test;

/**
 * Testes unitários para a classe Localizacao utilizando JUnit 4.
 * Cobre cálculo de distância geodésica (Haversine), manipulação de coordenadas e validações.
 */
public class LocalizacaoTest {

    private Localizacao pracaSete;
    private Localizacao pracaLiberdade;

    @Before
    public void setUp() {
        // 1) Eu tenho coordenadas conhecidas no centro de Belo Horizonte
        pracaSete = new Localizacao(-19.9191, -43.9386, "Praça Sete de Setembro", "Centro");
        pracaLiberdade = new Localizacao(-19.9333, -43.9378, "Praça da Liberdade", "Savassi / Funcionários");
    }

    @Test
    public void testCalcularDistanciaHaversineComPrecisao() {
        // 1) Eu tenho duas posições geográficas conhecidas em BH (distância real aprox. 1.6 a 1.8 km)
        // 2) E eu faço o cálculo da distância geodésica entre elas
        double distanciaKm = pracaSete.calcularDistancia(pracaLiberdade);

        // 3) Então eu espero que a distância calculada esteja no intervalo geodésico esperado
        assertTrue("A distância deve ser maior que 1.4 km", distanciaKm > 1.4);
        assertTrue("A distância deve ser menor que 2.0 km", distanciaKm < 2.0);
        assertEquals("Distância de ida deve ser igual à distância de volta",
                distanciaKm, pracaLiberdade.calcularDistancia(pracaSete), 0.0001);
    }

    @Test
    public void testCalcularDistanciaMesmoPontoRetornaZero() {
        // 1) Eu tenho um ponto geográfico
        // 2) E eu calculo a distância deste ponto para si mesmo
        double distancia = pracaSete.calcularDistancia(pracaSete);

        // 3) Então eu espero que a distância seja exatamente zero
        assertEquals("Distância para o mesmo ponto deve ser zero", 0.0, distancia, 0.00001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCalcularDistanciaDestinoNuloLancaExcecao() {
        // 1) Eu tenho uma localização válida
        // 2) E eu tento calcular a distância passando destino nulo
        // 3) Então eu espero uma IllegalArgumentException
        pracaSete.calcularDistancia(null);
    }

    @Test
    public void testGettersESettersLocalizacao() {
        // 1) Eu tenho uma localização inicial
        Localizacao loc = new Localizacao(-19.90, -43.90, "Rua Inicial", "Bairro Inicial");

        // 2) E eu atualizo seus atributos via setters
        loc.setLatitude(-19.85);
        loc.setLongitude(-43.95);
        loc.setEndereco("Av. Pres. Antônio Carlos, 6627");
        loc.setBairro("Pampulha");

        // 3) Então eu espero que os getters reflitam os novos valores
        assertEquals(-19.85, loc.getLatitude(), 0.0001);
        assertEquals(-43.95, loc.getLongitude(), 0.0001);
        assertEquals("Av. Pres. Antônio Carlos, 6627", loc.getEndereco());
        assertEquals("Pampulha", loc.getBairro());
        assertNotNull("toString não deve ser nulo", loc.toString());
    }

    @Test
    public void testEqualsEHashCodeLocalizacao() {
        // 1) Eu tenho duas instâncias com os mesmos dados e uma diferente
        Localizacao loc1 = new Localizacao(-19.92, -43.94, "Rua A", "Centro");
        Localizacao loc2 = new Localizacao(-19.92, -43.94, "Rua A", "Centro");
        Localizacao loc3 = new Localizacao(-19.80, -43.90, "Rua B", "Venda Nova");

        // 2) E comparo as instâncias
        // 3) Então eu espero reflexividade, simetria e consistência de hashCode
        assertEquals("Instâncias com mesmos valores devem ser iguais", loc1, loc2);
        assertNotEquals("Instâncias com valores distintos não devem ser iguais", loc1, loc3);
        assertEquals("Instâncias iguais devem possuir o mesmo hashCode", loc1.hashCode(), loc2.hashCode());
    }
}
