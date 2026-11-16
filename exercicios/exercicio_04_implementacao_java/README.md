# Exercício 4: Implementação Java - Sistema para Controle de Veículos em Incidentes

**Disciplina:** Engenharia de Software 1 - DCC / ICEx / UFMG  
**Professor:** Eduardo Figueiredo  
**Aluno:** Ítalo Leal  

---

## Descrição

Implementação orientada a objetos em Java para o **Sistema de Controle de Veículos em Incidentes**, desenvolvida a partir do Diagrama de Classes elaborado no Exercício 3.

A implementação atende aos 5 fatores levantados na especificação do sistema:

1. **(i) Tipo e Urgência do Incidente:** Utilização dos enums `TipoIncidente` e `NivelGravidade` para categorizar as ocorrências e definir a urgência de resposta.
2. **(ii) Posição das Viaturas e Estimativa:** Cálculo geodésico de distância na classe `Localizacao` (fórmula de Haversine) e método `solicitarPosicao()` na classe `Veiculo`, com fallback para `estimarPosicao()` quando a viatura estiver com sinal GPS indisponível.
3. **(iii) Tipos de Veículos Disponíveis:** Hierarquia com classe abstrata `Veiculo` e especializações `ViaturaPolicial`, `CaminhaoBombeiro` e `Ambulancia`, permitindo alocação de veículos específicos e envio em comboio.
4. **(iv) Localização e Áreas de Risco:** Classe `AreaDeRisco` com verificação de perímetro (`contem()`). O método `validarEnvioSeguro()` no `ControleDespacho` impede o envio de viatura solitária em áreas perigosas.
5. **(v) Alerta a Serviços de Emergência:** Classe `ServicoEmergencia` com método para disparo de alertas aos órgãos parceiros (Polícia Militar, Bombeiros, SAMU).

Também foi implementada a classe abstrata `Relator` com as subclasses `Policial` e `Bombeiro`, representando os profissionais responsáveis por atender ao chamado e registrar a ocorrência.

---

## Estrutura do Projeto

```
exercicio_04_implementacao_java/
├── src/
│   └── br/
│       └── ufmg/
│           └── dcc/
│               └── controleincidentes/
│                   ├── Main.java
│                   ├── model/
│                   │   ├── TipoIncidente.java
│                   │   ├── NivelGravidade.java
│                   │   ├── StatusIncidente.java
│                   │   ├── StatusVeiculo.java
│                   │   ├── Localizacao.java
│                   │   ├── AreaDeRisco.java
│                   │   ├── Veiculo.java
│                   │   ├── ViaturaPolicial.java
│                   │   ├── CaminhaoBombeiro.java
│                   │   ├── Ambulancia.java
│                   │   ├── Relator.java
│                   │   ├── Policial.java
│                   │   ├── Bombeiro.java
│                   │   └── ServicoEmergencia.java
│                   ├── service/
│                   │   └── ControleDespacho.java
│                   └── test/
│                       └── TesteSistemaControleIncidentes.java
├── bin/
├── diagrama_classes.puml
├── diagrama_classes.png
├── [Java] Italo Leal.pdf
└── [Java] Italo Leal.zip
```

---

## Como Compilar e Executar

### 1. Compilação
```bash
# No diretório exercicio_04:
javac -encoding UTF-8 -d bin src/br/ufmg/dcc/controleincidentes/model/*.java src/br/ufmg/dcc/controleincidentes/service/*.java src/br/ufmg/dcc/controleincidentes/*.java src/br/ufmg/dcc/controleincidentes/test/*.java
```

### 2. Executar a demonstração
```bash
java -cp bin br.ufmg.dcc.controleincidentes.Main
```

### 3. Executar os testes
```bash
java -cp bin br.ufmg.dcc.controleincidentes.test.TesteSistemaControleIncidentes
```
