package br.ufmg.dcc.controleincidentes.test;

import static org.junit.Assert.*;

import br.ufmg.dcc.controleincidentes.model.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Testes unitários para a hierarquia polimórfica de Veiculo utilizando JUnit 4.
 * Cobre telemetria GPS, estimativa de posição quando offline, ciclo de disponibilidade
 * e comportamentos especializados de ViaturaPolicial, CaminhaoBombeiro e Ambulancia.
 */
public class VeiculoTest {

    private Localizacao posBase;
    private ViaturaPolicial viatura;
    private CaminhaoBombeiro caminhao;
    private Ambulancia ambulancia;

    @Before
    public void setUp() {
        // 1) Eu tenho uma base operacional e três viaturas especializadas disponíveis
        posBase = new Localizacao(-19.9200, -43.9400, "Base Central", "Centro");
        viatura = new ViaturaPolicial("V-10", "VP-10", posBase, true, true);
        caminhao = new CaminhaoBombeiro("C-20", "CB-20", posBase, 5000.0, true);
        ambulancia = new Ambulancia("A-30", "SAMU-01", posBase, true, true);
    }

    @Test
    public void testGpsOnlineRetornaPosicaoExata() {
        // 1) Eu tenho uma viatura com sinal GPS ativo
        assertTrue("Sinal GPS deve iniciar ativo", viatura.isSinalGpsAtivo());

        // 2) E eu solicito a posição da viatura
        Localizacao pos = viatura.solicitarPosicao();

        // 3) Então eu espero receber exatamente a posição atual conhecida
        assertNotNull("Posição não pode ser nula", pos);
        assertEquals("Latitude deve coincidir", posBase.getLatitude(), pos.getLatitude(), 0.0001);
        assertEquals("Longitude deve coincidir", posBase.getLongitude(), pos.getLongitude(), 0.0001);
        assertEquals("Endereço deve coincidir", "Base Central", pos.getEndereco());
    }

    @Test
    public void testGpsOfflineRecorreAEstimativaDePosicao() {
        // 1) Eu tenho uma viatura que perdeu o sinal GPS
        viatura.setSinalGpsAtivo(false);
        assertFalse("Sinal GPS deve estar desativado", viatura.isSinalGpsAtivo());

        // 2) E eu solicito a posição da viatura
        Localizacao posEstimada = viatura.solicitarPosicao();

        // 3) Então eu espero que a estimativa seja gerada com base no último ponto conhecido
        assertNotNull("Estimativa de posição não deve ser nula", posEstimada);
        assertTrue("Endereço deve indicar que é uma posição estimada",
                posEstimada.getEndereco().contains("Estimada"));
        assertNotEquals("Posição estimada deve sofrer projeção matemática",
                posBase.getLatitude(), posEstimada.getLatitude(), 0.00001);
    }

    @Test
    public void testCalcularDistanciaAteAlvo() {
        // 1) Eu tenho uma viatura na Base Central e um alvo na Savassi (distância aprox. 1.6 km)
        Localizacao alvo = new Localizacao(-19.9333, -43.9378, "Praça da Liberdade", "Savassi");

        // 2) E calculo a distância da viatura até o alvo
        double dist = viatura.calcularDistanciaAte(alvo);

        // 3) Então espero distância calculada válida
        assertTrue("Distância deve ser positiva", dist > 1.0 && dist < 2.5);
    }

    @Test
    public void testCalcularDistanciaComAlvoNuloRetornaValorMaximo() {
        // 1) Eu tenho uma viatura posicionada
        // 2) E calculo a distância com alvo nulo
        double dist = viatura.calcularDistanciaAte(null);

        // 3) Então espero Double.MAX_VALUE como sentinela de erro
        assertEquals(Double.MAX_VALUE, dist, 0.0001);
    }

    @Test
    public void testCicloDeStatusDoVeiculo() {
        // 1) Eu tenho um veículo inicialmente disponível
        assertTrue("Status inicial deve ser DISPONIVEL", viatura.estaDisponivel());
        assertEquals(StatusVeiculo.DISPONIVEL, viatura.getStatus());

        // 2) E eu despacho a viatura para atender a ocorrência
        viatura.setStatus(StatusVeiculo.A_CAMINHO);
        assertFalse("Não deve estar mais disponível", viatura.estaDisponivel());
        assertEquals(StatusVeiculo.A_CAMINHO, viatura.getStatus());

        // E a viatura chega ao local
        viatura.setStatus(StatusVeiculo.NO_LOCAL);
        assertEquals(StatusVeiculo.NO_LOCAL, viatura.getStatus());

        // E a viatura entra em manutenção
        viatura.setStatus(StatusVeiculo.INDISPONIVEL);
        assertEquals(StatusVeiculo.INDISPONIVEL, viatura.getStatus());
        assertFalse(viatura.estaDisponivel());
    }

    @Test
    public void testEspecializacaoViaturaPolicial() {
        // 1) Eu tenho uma viatura policial configurada
        // 2) E consulto suas capacidades táticas
        // 3) Então espero conformidade de blindagem e armamento
        assertTrue("Deve possuir blindagem", viatura.isBlindagem());
        assertTrue("Deve possuir armamento pesado", viatura.isArmamentoPesado());
        assertEquals("VP-10", viatura.getPrefixo());

        viatura.patrulhar();
        viatura.interceptar();
    }

    @Test
    public void testEspecializacaoCaminhaoBombeiro() {
        // 1) Eu tenho um caminhão de bombeiro com tanque de 5000 litros e escada Magirus
        // 2) E consulto seus recursos operacionais
        // 3) Então espero capacidade de água e equipamentos adequados
        assertEquals(5000.0, caminhao.getCapacidadeAguaLitros(), 0.001);
        assertTrue("Deve possuir escada Magirus", caminhao.isEscadaMagirus());

        caminhao.combaterFogo();
        caminhao.resgatarFerragens();
    }

    @Test
    public void testEspecializacaoAmbulancia() {
        // 1) Eu tenho uma ambulância do SAMU tipo UTI móvel e SAV
        // 2) E consulto seus atributos médicos
        // 3) Então espero configuração de suporte avançado de vida
        assertTrue("Deve ser do tipo UTI móvel", ambulancia.isTipoUTI());
        assertTrue("Deve possuir Suporte Avançado de Vida (SAV)", ambulancia.isSuporteAvancadoVida());

        ambulancia.socorrerVitima();
        ambulancia.estabilizarPaciente();
    }

    @Test
    public void testEqualsEHashCodePorIdentificador() {
        // 1) Eu tenho dois veículos com o mesmo ID e um com ID diferente
        ViaturaPolicial v1 = new ViaturaPolicial("VP-1", "Prefixo 1", posBase, false, false);
        ViaturaPolicial v2 = new ViaturaPolicial("VP-1", "Outro Prefixo", posBase, true, true);
        ViaturaPolicial v3 = new ViaturaPolicial("VP-2", "Prefixo 1", posBase, false, false);

        // 2) E comparo as instâncias
        // 3) Então a identidade baseada no identificador do veículo deve ser preservada
        assertEquals("Veículos com mesmo ID devem ser iguais", v1, v2);
        assertNotEquals("Veículos com IDs distintos não devem ser iguais", v1, v3);
        assertEquals("Objetos iguais devem ter o mesmo hashCode", v1.hashCode(), v2.hashCode());
    }
}
