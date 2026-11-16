package br.ufmg.dcc.controleincidentes.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Suite unificada de testes automatizados JUnit 4 para o Sistema de Controle de Incidentes.
 * Agrupa todas as classes de testes unitários e de integração, validando 100% dos requisitos
 * funcionais, modelos de domínio e restrições de despacho do sistema.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        LocalizacaoTest.class,
        AreaDeRiscoTest.class,
        VeiculoTest.class,
        RelatorTest.class,
        IncidenteTest.class,
        ControleDespachoTest.class
})
public class SistemaControleIncidentesTestSuite {
    // Classe de ponto de entrada da suite de testes JUnit 4.
}
