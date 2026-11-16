# Engenharia de Software I (2026/2) - DCC/UFMG

Repositório completo e organizado da disciplina **Engenharia de Software I** (Prof. Eduardo Figueiredo — DCC/UFMG), contendo todos os slides de aulas, enunciados e resoluções de exercícios, provas, instruções do Trabalho Prático (TP), notas e espelho da página oficial da disciplina.

- **Página oficial da disciplina:** https://homepages.dcc.ufmg.br/~figueiredo/disciplinas/2026b/es1.htm
- **Página de exercícios e conquistas:** https://homepages.dcc.ufmg.br/~figueiredo/disciplinas/2026b/es1exercises.htm

---

## Estrutura de Pastas

```text
engsoft/
├── aulas/                                      # Slides de todas as aulas organizados por módulo/aula
│   ├── aula_01_apresentacao/                   # Apresentação da disciplina e instruções do TP
│   ├── aula_02_conceitos_fundamentais/         # Motivação, visão em camadas, concepção e desenvolvimento
│   ├── aula_03_processos_software/             # Processos, Cascata, Incremental, Integração e Mudanças
│   ├── aula_04_metodos_ageis/                  # Métodos Ágeis, Manifesto Ágil, XP e Scrum
│   ├── aula_07_engenharia_requisitos/          # Requisitos de Usuário/Sistema, RF/RNF, Processos e Técnicas
│   ├── aula_10_uml_casos_de_uso/               # Introdução à UML, Diagramas e Casos de Uso/Cenários
│   ├── aula_12_arquitetura_software/           # Arquitetura de Software e Padrões Arquiteturais
│   ├── aula_13_modelagem_oo_diagrama_classes/  # Desenvolvimento/Modelagem OO e Diagrama de Classes
│   ├── aula_15_diagramas_comportamentais/      # Diagramas de Sequência, Comunicação e Atividades
│   ├── aula_19_programacao_oo_java/            # POO em Java (Classes, Membros e Idiomas de Programação)
│   ├── aula_21_verificacao_validacao_testes/   # V&V, Inspeção, Testes de Unidade, Integração e Sistema
│   └── aula_22_evolucao_manutencao_refatoracao/# Evolução, Manutenção, Reengenharia, Refatoração e Bad Smells
├── exercicios/                                 # Enunciados, materiais base e resoluções dos exercícios
│   ├── exercicio_01_processos_software/        # Aula 05 (17/08) - Processos de Software
│   ├── exercicio_02_casos_de_uso/              # Aula 11 (09/09) - Diagrama de Casos de Uso e Cenários [RESOLVIDO]
│   ├── exercicio_03_diagrama_classes/          # Aula 14 (21/09) - Diagrama de Classes UML [RESOLVIDO]
│   ├── exercicio_04_implementacao_java/        # Aula 20 (19/10) - Implementação OO em Java [RESOLVIDO]
│   └── exercicio_05_testes_junit/              # Aula 23 (04/11) - Testes de Software JUnit
├── provas/                                     # Provas e correções aplicadas no semestre
├── trabalho_pratico/                           # Instruções, slides, temas e alocação do Trabalho Prático
├── notas/                                      # Planilhas/PDFs de notas (Provas, Exercícios, TP e Geral)
└── pagina_disciplina/                          # Cópia offline das páginas HTML, estilo CSS e medalhas (badges)
```

---

## Cronograma e Materiais das Aulas (`aulas/`)

| Aula | Data | Pasta / Tópico | Arquivos |
| :---: | :---: | :--- | :--- |
| **01** | 03/08 | `aulas/aula_01_apresentacao/` | `aula01.pdf`, `tp-slides.pdf` |
| **02** | 05/08 | `aulas/aula_02_conceitos_fundamentais/` | `01_motivacao-es_v03.pdf`, `02_estrutura-es_v01.pdf`, `03_especificacao-projeto-software_v03.pdf`, `04_desenvolvimento-software_v03.pdf` |
| **03** | 10/08 | `aulas/aula_03_processos_software/` | `01_processos-software_v03.pdf`, `02_modelo-cascata_v03.pdf`, `03_desenvolvimento-incremental_v03.pdf`, `04_integracao-configuracao_v03.pdf`, `05_processos-mudancas_v03.pdf` |
| **04** | 12/08 | `aulas/aula_04_metodos_ageis/` | `01_metodos-ageis_v03.pdf`, `02_metodos-ageis-manifesto_v03.pdf`, `03_metodo-agil-xp_v03.pdf`, `04_metodo-agil-scrum-fases_v03.pdf`, `05_metodo-agil-scrum-termos_v03.pdf` |
| **07** | 24/08 | `aulas/aula_07_engenharia_requisitos/` | `01_req-usuario-sistema_v03.pdf`, `02_req-funcional-rnf_v03.pdf`, `03_eng-req-processos_v03.pdf`, `04_eng-req-tecnicas_v03.pdf` |
| **10** | 02/09 | `aulas/aula_10_uml_casos_de_uso/` | `01_uml-intro_v02.pdf`, `02_uml-diagramas-estrutura_v01.pdf`, `03_uml-diagramas-comportamento_v01.pdf`, `04_uml-casos-de-uso_v03.pdf`, `05_uml-casos-de-uso-relacionamentos_v01.pdf`, `06_uml-casos-de-uso-cenarios_v01.pdf` |
| **12** | 14/09 | `aulas/aula_12_arquitetura_software/` | `01_arquitetura-sw_v03.pdf`, `02_padroes-arquiteturais_intro_v03.pdf`, `03_padroes-arquiteturais_estrutura_v03.pdf` |
| **13** | 16/09 | `aulas/aula_13_modelagem_oo_diagrama_classes/` | `01_desenvolvimento-oo-intro_v01.pdf`, `02_modelagem-oo_v03.pdf`, `03_uml-diagrama-classes_v02-1.pdf`, `04_uml-diagrama-classes-relacionamentos_v01.pdf` |
| **15** | 23/09 | `aulas/aula_15_diagramas_comportamentais/` | `01_uml-diagrama-sequencia-intro_v01.pdf`, `02_uml-diagrama-sequencia-mensagens_v01.pdf`, `03_uml-caso-de-uso-sequencia_v03.pdf`, `04_uml-diagrama-comunicacao_v01.pdf`, `05_uml-diagrama-atividades_v01.pdf` |
| **19** | 07/10 | `aulas/aula_19_programacao_oo_java/` | `01_poo-java_v01-2.pdf`, `02_poo-java-classes_v01.pdf`, `03_poo-java-membros_v01.pdf`, `04_idiomas-java_v02-2.pdf` |
| **21** | 21/10 | `aulas/aula_21_verificacao_validacao_testes/` | `01_verificacao-validacao_v03.pdf`, `02_inspecao-software_v03.pdf`, `03_testes-desenvolvimento_v03.pdf`, `04_testes-de-unidade_v03.pdf`, `05_testes-de-sistema_v03.pdf` |
| **22** | 26/10 | `aulas/aula_22_evolucao_manutencao_refatoracao/` | `01_evolucao-software_v03.pdf`, `02_manutencao-software_v03.pdf`, `03_bad-smell-refactoring_v03.pdf` |

---

## Exercícios Práticos (`exercicios/`)

1. **`exercicios/exercicio_01_processos_software/`** *(Aula 05 — 17/08)*
   - Enunciados: `aula05.pdf`, `exercicio-descricao.pdf`
2. **`exercicios/exercicio_02_casos_de_uso/`** *(Aula 11 — 09/09 — **Resolvido**)*
   - Enunciados: `aula11.pdf`, `exercicio-descricao.pdf`
   - Resolução: `exercicio_casos_de_uso.pdf`, `sistema_controle_incidentes.puml`, `sistema_controle_incidentes.png`, `generate_report.py`
3. **`exercicios/exercicio_03_diagrama_classes/`** *(Aula 14 — 21/09 — **Resolvido**)*
   - Enunciados: `exercicio-diagrama-classes_v02.pdf`, `exercicio-descricao.pdf`
   - Resolução: `[Classes] Italo Leal.pdf`, `exercicio-respostas.pdf`, `diagrama_classes.puml`, `diagrama_classes.png`, `generate_report.py`
4. **`exercicios/exercicio_04_implementacao_java/`** *(Aula 20 — 19/10 — **Resolvido**)*
   - Enunciados e pacote base: `exerc-implementacao_v03.pdf`, `exercicio-4-implementacao.pdf`, `exercicio-descricao.pdf`, `exercicio-4-descricao.pdf`, `bet.zip`
   - Resolução: `[Java] Italo Leal.pdf`, `[Java] Italo Leal.zip`, `src/` (código-fonte completo e suíte de testes), `diagrama_classes.puml`, `diagrama_classes.png`, `generate_delivery.py`, `README.md`
5. **`exercicios/exercicio_05_testes_junit/`** *(Aula 23 — 04/11)*
   - Enunciados: `exerc-testes_v03.pdf`, `exercicio-descricao.pdf`

---

## Provas, Trabalho Prático e Notas

- **`provas/`**:
  - `es1-p1.pdf`: Prova 1 / Correção (Aula 08/09)
  - `es1-p2.pdf`: Prova 2 / Correção (Aula 17/18)
- **`trabalho_pratico/`**:
  - `tp-slides.pdf`: Slides de apresentação do TP
  - `tp-instrucao.pdf`: Instruções detalhadas do TP
  - `tp-temas.pdf`: Lista de temas propostos
  - `tp-alocacao.pdf`: Alocação de grupos/temas e ordem de apresentação
- **`notas/`**:
  - `notas.pdf` (Notas Gerais), `notas-p1.pdf`, `notas-p2.pdf`, `notas-p3.pdf`, `notas-exercicios.pdf`, `notas-tp.pdf`, `notas-tp-apresentacao.pdf`
- **`pagina_disciplina/`**:
  - Cópia local de `es1.htm`, `es1exercises.htm`, `default.css`, `at.jpg` e pasta `figs/` com todas as medalhas/conquistas.

---

## Questionários Online de Revisão (Google Forms)

- **Revisão P1 (Aula 06 / Aula 07):**
  - [Questionário 01 — Motivação e Conceitos Fundamentais](https://forms.gle/rjmGkVUaGFbrHFPcA)
  - [Questionário 02 — Processos de Software](https://forms.gle/oqRsY6riTB2DJx4z6)
  - [Questionário 03 — Métodos Ágeis](https://forms.gle/zMGBbaaLCvun9qis6)
  - [Questionário 04 — Engenharia de Requisitos](https://forms.gle/axyhqPk2dPcjVZ6eA)
- **Revisão P2 (Aula 16):**
  - [Questionário 05 — Diagrama de Casos de Uso](https://forms.gle/7Y5VyovdDyYFcs9a9)
  - [Questionário 06 — Arquitetura de Software](https://forms.gle/NqLwS4pgBmUnCUa66)
  - [Questionário 07 — Diagrama de Classes](https://forms.gle/VQpMB7dH7jb74mwU9)
  - [Questionário 08 — Diagramas UML: Sequência, Comunicação e Atividades](https://forms.gle/Gr97hLMeQUsmiLUL6)
- **Revisão P3 (Aula 24):**
  - [Questionário 09 — Idiomas de Programação](https://forms.gle/q8RzXKS27cf2apd26)
  - [Questionário 10 — Testes de Software](https://forms.gle/o4QAyoZgKXNtcVDfA)
  - [Questionário 11 — Evolução de Software](https://forms.gle/yE9uwD3WsR6LYT7F9)
