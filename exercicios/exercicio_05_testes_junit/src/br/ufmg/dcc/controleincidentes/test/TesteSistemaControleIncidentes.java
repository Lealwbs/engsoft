package br.ufmg.dcc.controleincidentes.test;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * Ponto de execução por linha de comando para a suite completa de testes JUnit 4.
 * Executa a suite SistemaControleIncidentesTestSuite e a classe PrimeiraoTests,
 * emitindo um relatório detalhado com tempo de execução e status de aprovação.
 */
public class TesteSistemaControleIncidentes {

    public static void main(String[] args) {
        System.out.println("=====================================================================");
        System.out.println("  UFMG / ICEx / DCC - Engenharia de Software 1 - Prof. Eduardo Figueiredo");
        System.out.println("  Execução Automatizada da Suite de Testes de Software JUnit 4");
        System.out.println("  Aluno: Ítalo Leal");
        System.out.println("=====================================================================\n");

        long inicio = System.currentTimeMillis();
        Result resultado = JUnitCore.runClasses(
                SistemaControleIncidentesTestSuite.class,
                tests.PrimeiraoTests.class
        );
        long fim = System.currentTimeMillis();

        System.out.println("Resultados dos Testes JUnit:");
        System.out.printf(" - Total de testes executados: %d%n", resultado.getRunCount());
        System.out.printf(" - Testes com sucesso:        %d%n", resultado.getRunCount() - resultado.getFailureCount());
        System.out.printf(" - Falhas detectadas:         %d%n", resultado.getFailureCount());
        System.out.printf(" - Testes ignorados:          %d%n", resultado.getIgnoreCount());
        System.out.printf(" - Tempo de execução:         %d ms%n%n", (fim - inicio));

        if (resultado.wasSuccessful()) {
            System.out.println(">>> SUCESSO: Todos os testes automatizados passaram com êxito! <<<");
        } else {
            System.err.println(">>> FALHA: Foram detectados erros durante a execução dos testes: <<<");
            for (Failure f : resultado.getFailures()) {
                System.err.println(" - " + f.getTestHeader() + ": " + f.getMessage());
            }
            System.exit(1);
        }
    }
}
