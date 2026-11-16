# Exercício 5: Testes de Software JUnit - Sistema para Controle de Veículos em Incidentes

**Disciplina:** Engenharia de Software 1 - DCC / ICEx / UFMG  
**Professor:** Eduardo Figueiredo  
**Aluno:** Ítalo Leal  
**Prazo de Entrega:** 04/11/2026 às 23:59 h  
**Destino de Envio:** `disciplina.eduardo@gmail.com`  
**Título / Assunto da Mensagem:** `[JUnit] Italo Leal`  
**Arquivo Compactado:** `[JUnit] Italo Leal.zip`  

---

## 1. Visão Geral

Este projeto consiste na resolução do **Exercício 5 (Testes de Software JUnit)** da disciplina de Engenharia de Software 1 (UFMG), com base na especificação do **Sistema para Controle de Veículos em Incidentes** e na implementação Java desenvolvida no Exercício 4.

A suíte de testes automatizados foi construída sobre o framework **JUnit 4** e validada para importação direta na **IDE Eclipse para Desenvolvedores Java**, contemplando **48 testes automatizados**, todos executados com 100% de sucesso.

---

## 2. Estrutura do Projeto

O projeto está configurado como um **Eclipse Java Project** padrão, contendo as bibliotecas JUnit e Hamcrest embutidas na pasta `lib/` para garantir execução imediata tanto na IDE quanto por linha de comando.

```
exercicio_05/
├── .project                               # Configuração de projeto do Eclipse
├── .classpath                             # Configuração de classpath do Eclipse (JRE + JUnit 4)
├── README.md                              # Documentação completa de execução e arquitetura
├── [JUnit] Italo Leal.pdf                 # Relatório detalhado de entrega em PDF
├── [JUnit] Italo Leal.zip                 # Pacote final compactado para envio
├── diagrama_classes.puml                  # Diagrama de Classes UML em formato PlantUML
├── diagrama_classes.png                   # Diagrama de Classes UML exportado em imagem
├── generate_delivery.py                   # Script de automação: compilação, testes, PDF e ZIP
├── lib/                                   # Bibliotecas de teste empacotadas
│   ├── junit-4.13.2.jar
│   └── hamcrest-3.0.0.jar
├── src/                                   # Código-fonte Java
│   ├── tests/
│   │   └── PrimeiraoTests.java            # Testes no padrão canônico dos Slides 5 e 6 da aula
│   └── br/
│       └── ufmg/
│           └── dcc/
│               └── controleincidentes/
│                   ├── Main.java          # Demonstração executável dos cenários do sistema
│                   ├── model/             # Entidades do domínio orientado a objetos
│                   │   ├── TipoIncidente.java
│                   │   ├── NivelGravidade.java
│                   │   ├── StatusIncidente.java
│                   │   ├── StatusVeiculo.java
│                   │   ├── Localizacao.java
│                   │   ├── AreaDeRisco.java
│                   │   ├── Veiculo.java            (Classe Abstrata)
│                   │   ├── ViaturaPolicial.java    (Subclasse de Veiculo)
│                   │   ├── CaminhaoBombeiro.java   (Subclasse de Veiculo)
│                   │   ├── Ambulancia.java         (Subclasse de Veiculo)
│                   │   ├── Relator.java            (Classe Abstrata)
│                   │   ├── Policial.java           (Subclasse de Relator)
│                   │   ├── Bombeiro.java           (Subclasse de Relator)
│                   │   ├── ServicoEmergencia.java
│                   │   └── Incidente.java
│                   ├── service/           # Camada de serviços e regras de negócio
│                   │   └── ControleDespacho.java
│                   └── test/              # Suíte modular de testes JUnit 4
│                       ├── LocalizacaoTest.java
│                       ├── AreaDeRiscoTest.java
│                       ├── VeiculoTest.java
│                       ├── RelatorTest.java
│                       ├── IncidenteTest.java
│                       ├── ControleDespachoTest.java
│                       ├── SistemaControleIncidentesTestSuite.java
│                       └── TesteSistemaControleIncidentes.java
└── bin/                                   # Bytecodes compilados (.class)
```

---

## 3. Cobertura dos Fatores da Especificação

Os testes cobrem exaustivamente os 5 fatores descritos no enunciado do sistema, além dos conceitos fundamentais de Orientação a Objetos (herança, polimorfismo, encapsulamento e tratamento de exceções):

1. **Fator (i) - Tipo de Incidente e Urgência:**
   - Testado em `ControleDespachoTest` e `IncidenteTest`.
   - Valida que `NivelGravidade.ALTA` exige reforço mínimo de viaturas e que `TipoIncidente` orienta o direcionamento do atendimento.

2. **Fator (ii) - Posição das Viaturas e Estimativa de Posição:**
   - Testado em `LocalizacaoTest` e `VeiculoTest`.
   - Valida o cálculo geodésico pela fórmula de Haversine em `Localizacao`.
   - Valida que quando o GPS está ativo (`isSinalGpsAtivo() == true`), `solicitarPosicao()` retorna a posição real.
   - Valida que quando o GPS fica inativo (`setSinalGpsAtivo(false)`), o sistema aciona automaticamente `estimarPosicao()`, mantendo a operacionalidade com projeção matemática.

3. **Fator (iii) - Tipos de Veículos Disponíveis e Polimorfismo:**
   - Testado em `VeiculoTest` e `ControleDespachoTest`.
   - Valida o polimorfismo da hierarquia `Veiculo`: `ViaturaPolicial` (blindagem e armamento), `CaminhaoBombeiro` (capacidade de água e escada Magirus) e `Ambulancia` (UTI e Suporte Avançado de Vida).
   - Valida o despacho direcionado: incêndios priorizam caminhões de bombeiro; assaltos priorizam viaturas policiais; chamados médicos priorizam ambulâncias.

4. **Fator (iv) - Localização e Áreas de Risco:**
   - Testado em `AreaDeRiscoTest`, `IncidenteTest` e `ControleDespachoTest`.
   - Valida a verificação de perímetro espacial com `contem(Localizacao)`.
   - Valida a regra de segurança em `ControleDespacho.validarEnvioSeguro()`, que **bloqueia expressamente** o envio de uma viatura solitária para áreas conflagradas e exige contingente em comboio.

5. **Fator (v) - Alerta a Serviços de Emergência:**
   - Testado em `ControleDespachoTest` e `IncidenteTest`.
   - Valida o acionamento automatizado de órgãos parceiros (`ServicoEmergencia`: SAMU, Bombeiros, Polícia Militar) em incidentes graves, incêndios ou ocorrências em áreas de risco.

6. **Entidade Relator e Ciclo de Vida do Incidente:**
   - Testado em `RelatorTest` e `IncidenteTest`.
   - Valida que policiais e bombeiros atuam polimorficamente como relatores institucionais do chamado.
   - Valida a máquina de estados do incidente: `REGISTRADO` -> `EM_ATENDIMENTO` (ao alocar primeiro veículo) -> `FINALIZADO`.

---

## 4. Estrutura dos Testes (Padrão das Aulas)

Todos os testes seguem o template apresentado nas páginas 5 e 6 dos slides:

```java
// 1) Eu tenho... (Cenário / Arrange)
// 2) E eu faço... (Ação / Act)
// 3) Então eu espero... (Assertivas / Assert)
```

Assertivas do JUnit 4 utilizadas:
- `assertEquals(mensagem, esperado, obtido, delta)`
- `assertTrue(mensagem, expressao)`
- `assertFalse(mensagem, expressao)`
- `assertNull(mensagem, referencia)`
- `assertNotNull(mensagem, valor)`
- `@Test(expected = IllegalArgumentException.class)` para verificação de exceções esperadas

---

## 5. Como Importar e Rodar no Eclipse

1. Abra a **IDE Eclipse para Desenvolvedores Java**.
2. Vá no menu: **`File` -> `Import...`**
3. Selecione: **`General` -> `Existing Projects into Workspace`** e clique em **`Next`**.
4. Em **`Select root directory`**, clique em **`Browse...`** e selecione a pasta `exercicio_05`.
5. Certifique-se de que a caixa do projeto `exercicio_05` esteja marcada e clique em **`Finish`**.
6. Para rodar todos os testes:
   - Clique com o botão direito sobre o pacote `br.ufmg.dcc.controleincidentes.test` (ou sobre a classe `SistemaControleIncidentesTestSuite.java`).
   - Escolha **`Run As` -> `JUnit Test`**.
   - A barra verde do JUnit será exibida com **48/48 testes aprovados**.

---

## 6. Como Compilar e Rodar por Linha de Comando

### Compilação:
```bash
# Estando dentro do diretório exercicio_05:
javac -encoding UTF-8 -cp "lib/junit-4.13.2.jar;lib/hamcrest-3.0.0.jar" -d bin src/br/ufmg/dcc/controleincidentes/model/*.java src/br/ufmg/dcc/controleincidentes/service/*.java src/br/ufmg/dcc/controleincidentes/*.java src/br/ufmg/dcc/controleincidentes/test/*.java src/tests/*.java
```

### Executar a Suíte de Testes JUnit:
```bash
java -cp "bin;lib/junit-4.13.2.jar;lib/hamcrest-3.0.0.jar" br.ufmg.dcc.controleincidentes.test.TesteSistemaControleIncidentes
```

### Executar via JUnitCore padrão:
```bash
java -cp "bin;lib/junit-4.13.2.jar;lib/hamcrest-3.0.0.jar" org.junit.runner.JUnitCore br.ufmg.dcc.controleincidentes.test.SistemaControleIncidentesTestSuite tests.PrimeiraoTests
```

### Executar a Demonstração Principal:
```bash
java -cp "bin;lib/junit-4.13.2.jar;lib/hamcrest-3.0.0.jar" br.ufmg.dcc.controleincidentes.Main
```

### Gerar PDF e ZIP da Entrega Automaticamente:
```bash
python generate_delivery.py
```
