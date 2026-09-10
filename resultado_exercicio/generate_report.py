import os
import sys
import urllib.request
import plantuml
from fpdf import FPDF

# Configurações de Arquivos e URLs
PLANTUML_URL = 'http://www.plantuml.com/plantuml/png/'
BASE_DIR = os.path.dirname(os.path.abspath(__file__))
PUML_FILE = os.path.join(BASE_DIR, 'sistema_controle_incidentes.puml')
PNG_FILE = os.path.join(BASE_DIR, 'sistema_controle_incidentes.png')
PDF_OUTPUT_FILE = os.path.join(BASE_DIR, 'exercicio_casos_de_uso.pdf')


class CustomPDF(FPDF):
    def __init__(self):
        super().__init__()
        self.set_margins(20, 20, 20)
        self.set_auto_page_break(auto=True, margin=20)
        
    def header(self):
        # Cabeçalho apenas a partir da página 2
        if self.page_no() > 1:
            self.set_font('helvetica', 'I', 8)
            self.set_text_color(120, 120, 120)
            self.cell(0, 8, 'Engenharia de Software 1 - Controle de Veículos em Incidentes', align='L')
            self.cell(0, 8, f'Página {self.page_no()}', align='R')
            self.ln(8)
            self.set_draw_color(210, 210, 210)
            self.set_line_width(0.3)
            self.line(20, 24, 190, 24)
            self.ln(6)

    def footer(self):
        # Rodapé apenas a partir da página 2
        if self.page_no() > 1:
            self.set_y(-15)
            self.set_font('helvetica', 'I', 8)
            self.set_text_color(130, 130, 130)
            self.cell(0, 10, 'DCC / ICEx / UFMG - Prof. Eduardo Figueiredo', align='C')

    def cover_page(self):
        self.set_auto_page_break(auto=False)
        self.add_page()
        self.set_text_color(0, 0, 0)
        
        # Cabeçalho Institucional
        self.ln(10)
        self.set_font('helvetica', 'B', 13)
        self.cell(0, 7, 'UNIVERSIDADE FEDERAL DE MINAS GERAIS', align='C')
        self.ln(7)
        self.set_font('helvetica', '', 11)
        self.cell(0, 6, 'INSTITUTO DE CIÊNCIAS EXATAS (ICEx)', align='C')
        self.ln(6)
        self.cell(0, 6, 'DEPARTAMENTO DE CIÊNCIA DA COMPUTAÇÃO (DCC)', align='C')
        
        # Linha Divisória de Destaque
        self.ln(15)
        self.set_draw_color(0, 102, 204)
        self.set_line_width(1.2)
        self.line(45, self.get_y(), 165, self.get_y())
        
        # Título Principal
        self.ln(45)
        self.set_font('helvetica', 'B', 20)
        self.set_text_color(0, 102, 204)
        self.multi_cell(0, 10, 'SISTEMA PARA CONTROLE DE VEÍCULOS\nEM INCIDENTES', align='C')
        self.ln(8)
        self.set_font('helvetica', 'B', 13)
        self.set_text_color(90, 90, 90)
        self.cell(0, 8, 'Modelagem de Casos de Uso e Especificação de Cenários', align='C')
        
        # Contexto do Trabalho
        self.ln(14)
        self.set_font('helvetica', 'I', 10.5)
        self.set_text_color(110, 110, 110)
        self.multi_cell(0, 5.5, 'Exercício Prático apresentado para a disciplina de Engenharia de Software 1\nministrada pelo Prof. Eduardo Figueiredo.', align='C')
        
        # Bloco do Autor
        self.set_y(225)
        self.set_font('helvetica', 'B', 11)
        self.set_text_color(40, 40, 40)
        self.cell(0, 5.5, 'AUTOR:', align='C')
        self.ln(6)
        self.set_font('helvetica', '', 12)
        self.cell(0, 6, 'Ítalo Leal', align='C')
        self.ln(5.5)
        self.set_font('helvetica', 'I', 10)
        self.set_text_color(100, 100, 100)
        self.cell(0, 5, 'Aluno de Engenharia de Software 1', align='C')
        
        # Rodapé da Capa (Local e Data)
        self.set_y(260)
        self.set_font('helvetica', 'B', 10)
        self.set_text_color(60, 60, 60)
        self.cell(0, 5, 'Belo Horizonte', align='C')
        self.ln(5)
        self.set_font('helvetica', '', 10)
        self.cell(0, 5, 'Setembro de 2026', align='C')
        
        # Restaura quebra de página automática
        self.set_auto_page_break(auto=True, margin=20)

    def add_section(self, title):
        self.ln(3)
        self.set_font('helvetica', 'B', 13)
        self.set_text_color(0, 102, 204) # Azul padrão UFMG
        self.cell(0, 9, title)
        self.ln(9)
        self.set_text_color(0, 0, 0)

    def add_subsection(self, title):
        self.ln(3)
        self.set_font('helvetica', 'B', 10.5)
        self.set_text_color(45, 45, 45)
        self.cell(0, 7, title)
        self.ln(7)
        self.set_text_color(0, 0, 0)

    def write_paragraph(self, text):
        self.set_font('helvetica', '', 9.5)
        self.multi_cell(0, 5.5, text)
        self.ln(3)

    def write_bullet_point(self, title, text):
        self.set_font('helvetica', 'B', 9.5)
        self.write(5.2, "  * ")
        self.write(5.2, title)
        self.set_font('helvetica', '', 9.5)
        self.write(5.2, f": {text}\n")
        
    def write_scenario(self, code, name, actors, pre_condition, normal_flow, alt_flows, post_condition):
        # Estima se há espaço suficiente para iniciar o cenário nesta página
        # Cada cenário ocupa cerca de 75mm a 90mm
        if self.get_y() > 185:
            self.add_page()
            
        # Título do Cenário com caixa de destaque
        self.ln(3)
        self.set_font('helvetica', 'B', 10.5)
        self.set_text_color(255, 255, 255)
        self.set_fill_color(0, 102, 204)
        self.cell(0, 7.5, f"  {code}: {name}", fill=True)
        self.ln(9)
        
        self.set_text_color(0, 0, 0)
        
        # Atores
        self.set_font('helvetica', 'B', 9.5)
        self.write(5.2, "Atores: ")
        self.set_font('helvetica', '', 9.5)
        self.write(5.2, f"{actors}\n")
        
        # Pré-condição
        self.set_font('helvetica', 'B', 9.5)
        self.write(5.2, "Pré-condição: ")
        self.set_font('helvetica', '', 9.5)
        self.write(5.2, f"{pre_condition}\n")
        
        # Fluxo Normal
        self.set_font('helvetica', 'B', 9.5)
        self.write(5.2, "Fluxo Normal:\n")
        self.set_font('helvetica', '', 9.5)
        for idx, step in enumerate(normal_flow, 1):
            self.write(5.0, f"  {idx}. {step}\n")
            
        # Fluxos Alternativos
        self.set_font('helvetica', 'B', 9.5)
        self.write(5.2, "Fluxos Alternativos:\n")
        self.set_font('helvetica', '', 9.5)
        if alt_flows:
            for key, steps in alt_flows.items():
                self.set_font('helvetica', 'B', 9.0)
                self.write(5.0, f"  * {key}\n")
                self.set_font('helvetica', '', 9.0)
                for s_idx, step in enumerate(steps, 1):
                    self.write(4.8, f"    {s_idx}. {step}\n")
        else:
            self.write(5.0, "  * Nenhum.\n")
            
        # Pós-condição
        self.set_font('helvetica', 'B', 9.5)
        self.write(5.2, "Pós-condição: ")
        self.set_font('helvetica', '', 9.5)
        self.write(5.2, f"{post_condition}\n")
        
        # Linha separadora discreta
        self.ln(3)
        self.set_draw_color(220, 220, 220)
        self.set_line_width(0.4)
        self.line(20, self.get_y(), 190, self.get_y())
        self.ln(4)


def generate_png_from_puml():
    print("Gerando imagem do diagrama PlantUML via servidor com codificação UTF-8...")
    try:
        with open(PUML_FILE, 'r', encoding='utf-8') as f:
            puml_code = f.read()

        encoded = plantuml.deflate_and_encode(puml_code)
        url = PLANTUML_URL + encoded
        headers = {
            'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36'
        }
        req = urllib.request.Request(url, headers=headers)
        with urllib.request.urlopen(req) as response:
            if response.status == 200:
                png_data = response.read()
                with open(PNG_FILE, 'wb') as out_file:
                    out_file.write(png_data)
                print(f"Imagem PNG gerada com sucesso ({len(png_data)} bytes): {PNG_FILE}")
                return True
            else:
                print(f"Erro no servidor PlantUML: Código {response.status}", file=sys.stderr)
                return False
    except Exception as e:
        print(f"Erro ao gerar a imagem PlantUML: {e}", file=sys.stderr)
        return False


def build_pdf_report():
    print("Construindo relatório PDF consolidado...")
    pdf = CustomPDF()
    pdf.cover_page()
    
    # ---------------------------------------------------------
    # PÁGINA 2: INTRODUÇÃO E CONTEXTUALIZAÇÃO DO PROBLEMA
    # ---------------------------------------------------------
    pdf.add_page()
    pdf.add_section("1. Introdução ao Sistema")
    
    pdf.write_paragraph(
        "O presente documento descreve a modelagem formal de requisitos por meio de Diagrama de Casos de Uso (UML) "
        "e especificação detalhada de cenários para o 'Sistema para Controle de Veículos em Incidentes'. "
        "A finalidade central da solução é viabilizar o registro rápido de ocorrências emergenciais e automatizar "
        "a tomada de decisão quanto à seleção e ao direcionamento dos veículos e equipes de socorro mais apropriados."
    )
    
    pdf.write_paragraph(
        "De acordo com as diretrizes do exercício proposto pelo Prof. Eduardo Figueiredo, o algoritmo de direcionamento "
        "e despacho do sistema fundamenta-se estritamente em cinco fatores operacionais determinantes:"
    )
    
    pdf.write_bullet_point(
        "(i) Tipo de Incidente", 
        "Diferentes incidentes possuem graus distintos de severidade e exigem níveis imediatos de urgência e priorização de resposta."
    )
    pdf.write_bullet_point(
        "(ii) Posição dos Veículos Disponíveis", 
        "A regra geral é direcionar a viatura disponível geograficamente mais próxima. Todavia, como a posição exata pode "
        "não ser conhecida a qualquer instante (devido a áreas de sombra de cobertura ou falha temporária de telemetria GPS), "
        "o sistema prevê tanto a solicitação formal de coordenadas quanto o cálculo de estimativa com base em histórico operacional."
    )
    pdf.write_bullet_point(
        "(iii) Tipos de Veículos Disponíveis", 
        "Determinadas ocorrências exigem viaturas especializadas (ex.: caminhão auto-bomba de combate a incêndio, ambulância com UTI móvel) "
        "ou demandam uma sequência coordenada de envio de múltiplos veículos."
    )
    pdf.write_bullet_point(
        "(iv) Localização do Incidente", 
        "Em locais classificados como de alta vulnerabilidade social ou áreas de risco severo, a diretriz de segurança impede "
        "o despacho de uma viatura isolada, exigindo o envio simultâneo de reforço e comboio operacional."
    )
    pdf.write_bullet_point(
        "(v) Alertas Automáticos a Serviços de Emergência", 
        "Sempre que o incidente exigir intervenção complementar das forças públicas de atendimento, o sistema dispara alertas "
        "eletrônicos automáticos e integrados para a Polícia Militar, Corpo de Bombeiros e Serviço de Ambulância (SAMU)."
    )
    
    pdf.write_paragraph(
        "Adicionalmente, os chamados são inseridos no sistema por um Relator (papel desempenhado por policiais militares ou bombeiros "
        "que realizam o primeiro atendimento na cena do evento), garantindo rastreabilidade e integridade às informações cadastrais."
    )

    # ---------------------------------------------------------
    # PÁGINA 3: DIAGRAMA DE CASOS DE USO (UML)
    # ---------------------------------------------------------
    pdf.add_page()
    pdf.add_section("2. Diagrama de Casos de Uso (UML)")
    
    pdf.write_paragraph(
        "O Diagrama de Casos de Uso abaixo foi elaborado em total consonância com a Unified Modeling Language (UML), "
        "representando a fronteira do sistema, os atores que interagem com o software e as relações entre casos de uso "
        "(inclusão <<include>>, extensão <<extend>> e generalização entre atores)."
    )
    
    # Inserção da Imagem Centralizada com Dimensões Proporcionais
    if os.path.exists(PNG_FILE):
        img_x = 28
        img_y = pdf.get_y() + 4
        img_w = 154  # Largura confortável dentro dos limites A4 (210 - 2*20 = 170 útil)
        pdf.image(PNG_FILE, x=img_x, y=img_y, w=img_w)
    else:
        pdf.set_font('helvetica', 'B', 11)
        pdf.set_text_color(200, 0, 0)
        pdf.cell(0, 12, "[ERRO: Diagrama PNG não encontrado!]", align='C')
        pdf.set_text_color(0, 0, 0)

    # ---------------------------------------------------------
    # PÁGINA 4: ATORES E RELACIONAMENTOS DO SISTEMA
    # ---------------------------------------------------------
    pdf.add_page()
    pdf.add_section("2.1. Atores do Sistema e Estrutura de Relacionamentos")
    
    pdf.add_subsection("Atores Identificados:")
    pdf.write_bullet_point(
        "Relator", 
        "Ator abstrato responsável por alimentar o sistema com os dados preliminares do incidente. "
        "É especializado pelos atores operacionais 'Policial' e 'Bombeiro' que prestam socorro in loco."
    )
    pdf.write_bullet_point(
        "Operador do Sistema (Despachante)", 
        "Usuário centralizado na sala de operações que acompanha os incidentes ativos, analisa as propostas de alocação de veículos "
        "geradas pelo algoritmo inteligente e confirma ou ajusta os despachos."
    )
    pdf.write_bullet_point(
        "Veículo (Equipe em Campo)", 
        "Representa tanto a viatura conectada (via sistema telemático embarcado) quanto a respectiva equipe humana. "
        "Fornece telemetria de coordenadas e recebe ordens de despacho, além de atualizar o status operacional em tempo real."
    )
    pdf.write_bullet_point(
        "Serviço de Emergência", 
        "Ator externo que encapsula os órgãos públicos de segurança e resgate. É especializado pelas entidades externas: "
        "'Polícia Militar', 'Corpo de Bombeiros' e 'Ambulância (SAMU)'."
    )
    
    pdf.ln(3)
    pdf.add_subsection("Relacionamentos entre Casos de Uso:")
    pdf.write_bullet_point(
        "Associação (Ator - Caso de Uso)", 
        "Ligação direta indicando quais usuários requisitam ou recebem retornos funcionais de cada caso de uso."
    )
    pdf.write_bullet_point(
        "Inclusão (<<include>>: UC02 -> UC03)", 
        "Para que o caso de uso base 'UC02: Direcionar Veículo Apropriado' possa calcular a proximidade e sugerir o melhor veículo, "
        "a execução de 'UC03: Obter Posição do Veículo' é estritamente obrigatória."
    )
    pdf.write_bullet_point(
        "Extensão (<<extend>>: UC04 -> UC02)", 
        "Caso uma viatura esteja temporariamente sem sinal GPS ou com coordenadas desconhecidas, o fluxo opcional "
        "'UC04: Estimar Posição do Veículo' estende o comportamento de UC02 calculando a posição estimada por histórico."
    )
    pdf.write_bullet_point(
        "Extensão (<<extend>>: UC05 -> UC02)", 
        "Quando o incidente apresenta alta periculosidade ou requer formalmente o apoio de forças públicas externas, "
        "o caso de uso 'UC05: Alertar Serviços de Emergência' estende a rotina de despacho para acionamento automatizado de APIs externas."
    )
    pdf.write_bullet_point(
        "Generalização / Especialização", 
        "Aplicada entre atores com papéis compartilhados (Policial e Bombeiro são especializações de Relator; PM, Bombeiros e SAMU são especializações de Serviço de Emergência)."
    )

    # ---------------------------------------------------------
    # PÁGINA 5+: ESPECIFICAÇÃO DOS CENÁRIOS DE CASOS DE USO
    # ---------------------------------------------------------
    pdf.add_page()
    pdf.add_section("3. Especificação dos Cenários de Caso de Uso")
    
    pdf.write_paragraph(
        "A documentação a seguir utiliza a técnica formal de cenários ensinada na disciplina (Prof. Eduardo Figueiredo), "
        "detalhando para cada caso de uso: Atores envolvidos, Pré-condições, Fluxo Normal numerado, Fluxos Alternativos e Pós-condições."
    )
    
    # --- UC01 ---
    pdf.write_scenario(
        code="UC01",
        name="Registrar Incidente",
        actors="Relator (Policial ou Bombeiro)",
        pre_condition="O Relator está autenticado no terminal de campo ou rádio-terminal e com a tela de registro ativa.",
        normal_flow=[
            "O Relator inicia a opção de inclusão de uma nova ocorrência de incidente.",
            "O sistema exibe o formulário solicitando os dados essenciais: tipo do incidente (gravidade), endereço/localização física, nível de urgência, descrição do cenário e identificação do relator.",
            "O Relator preenche todas as informações requeridas e submete o formulário.",
            "O sistema valida o preenchimento dos campos obrigatórios e a integridade dos dados informados.",
            "O sistema grava o registro do incidente no banco de dados central e gera um identificador único de protocolo.",
            "O sistema emite a confirmação do registro ao terminal do Relator.",
            "O sistema dispara automaticamente o caso de uso 'UC02: Direcionar Veículo Apropriado' associado ao incidente recém-cadastrado."
        ],
        alt_flows={
            "Campos obrigatórios não preenchidos ou inválidos (Passo 4)": [
                "O sistema informa as inconsistências na tela do terminal.",
                "O Relator corrige as informações incorretas e reenvia.",
                "O fluxo retorna ao passo 4 do fluxo normal."
            ]
        },
        post_condition="O incidente encontra-se cadastrado no sistema no estado 'Pendente de Alocação' e pronto para roteamento."
    )
    
    # --- UC02 ---
    pdf.write_scenario(
        code="UC02",
        name="Direcionar Veículo Apropriado",
        actors="Operador do Sistema (Despachante), Veículo (Equipe em Campo), Sistema",
        pre_condition="Existe um incidente cadastrado pendente de atendimento (UC01).",
        normal_flow=[
            "O sistema analisa as características do incidente: tipo, severidade e localização geográfica.",
            "O sistema executa obrigatoriamente o caso de uso incluído 'UC03: Obter Posição do Veículo' para obter a localização instantânea das viaturas.",
            "O algoritmo de despacho calcula a alocação ótima considerando os 5 critérios: tipo de incidente, proximidade dos veículos, tipos de viaturas exigidas (especializadas ou comboio), área de risco local e necessidade de suporte externo.",
            "O sistema exibe a proposta de alocação de veículos e rotas recomendadas na tela do Operador do Sistema.",
            "O Operador do Sistema revisa a sugestão e seleciona a opção 'Aprovar e Despachar'.",
            "O sistema transmite a ordem de serviço eletrônica com as rotas GPS para os terminais móveis dos veículos designados.",
            "A equipe do veículo recebe a ordem e confirma o deslocamento para a ocorrência (UC07)."
        ],
        alt_flows={
            "Posição GPS de viatura indisponível (Passo 2)": [
                "O sistema aciona o caso de uso estendido 'UC04: Estimar Posição do Veículo'.",
                "A estimativa de localização é fornecida com margem de tolerância e o fluxo retorna ao passo 3."
            ],
            "Área de alto risco requer reforço de comboio (Passo 3)": [
                "O sistema detecta área de vulnerabilidade e aloca automaticamente veículos de escolta redundantes na proposta.",
                "O fluxo prossegue normalmente para aprovação no passo 4."
            ],
            "Incidente de alta gravidade exige forças públicas externas (Passo 3)": [
                "O sistema aciona o caso de uso estendido 'UC05: Alertar Serviços de Emergência'.",
                "O fluxo prossegue em paralelo para o passo 4."
            ],
            "Operador ajusta os veículos manualmente (Passo 5)": [
                "O Operador altera a lista de veículos sugeridos conforme discernimento tático.",
                "O Operador confirma o despacho manual e o fluxo segue para o passo 6."
            ]
        },
        post_condition="Os veículos apropriados são despachados para o local e o status da ocorrência muda para 'Veículos em Deslocamento'."
    )
    
    # --- UC03 ---
    pdf.write_scenario(
        code="UC03",
        name="Obter Posição do Veículo",
        actors="Sistema, Veículo (Equipe em Campo)",
        pre_condition="A rotina de direcionamento (UC02) requisitou o posicionamento das frotas ativas.",
        normal_flow=[
            "O sistema envia sinal de requisição de posicionamento para o módulo telemático embarcado dos veículos.",
            "O dispositivo receptor GPS de cada viatura processa as coordenadas atuais de latitude e longitude.",
            "A viatura transmite as coordenadas atualizadas e o horário de leitura para o servidor central.",
            "O sistema valida a consistência espacial dos dados e consolida a matriz de proximidade para uso pelo UC02."
        ],
        alt_flows={
            "Falha temporária de conectividade ou sinal GPS indisponível (Passo 3)": [
                "O sistema registra o veículo com o estado temporário de 'Sinal de Posição Indisponível'.",
                "O sistema delega a resolução para a rotina de estimativa (UC04) e encerra a consulta desta viatura."
            ]
        },
        post_condition="As coordenadas geográficas atuais das viaturas ativas são registradas e disponibilizadas para o cálculo de rotas."
    )
    
    # --- UC04 ---
    pdf.write_scenario(
        code="UC04",
        name="Estimar Posição do Veículo",
        actors="Sistema (Processamento Interno)",
        pre_condition="As coordenadas GPS exatas da viatura não puderam ser obtidas via telemetria direta (UC03).",
        normal_flow=[
            "O sistema consulta o histórico recente de deslocamento da viatura (última localização conhecida, horário e rota cadastrada).",
            "O sistema estima as coordenadas prováveis calculando a velocidade média teórica da via pelo tempo decorrido desde a última transmissão.",
            "O sistema calcula e atribui um índice percentual de confiança à localização estimada.",
            "O sistema disponibiliza a posição estimada e a margem de erro para o algoritmo de decisão do UC02."
        ],
        alt_flows={
            "Ausência prolongada de transmissões anteriores (Passo 1)": [
                "O sistema constata que o veículo não transmite dados há tempo superior ao limite seguro.",
                "O sistema sinaliza o veículo como 'Sem Comunicação' e o desconsidera para a rodada atual de despacho."
            ]
        },
        post_condition="A posição estimada do veículo é repassada com o indicador de confiança para subsidiar o direcionamento."
    )
    
    # --- UC05 ---
    pdf.write_scenario(
        code="UC05",
        name="Alertar Serviços de Emergência",
        actors="Sistema, Serviço de Emergência (Polícia Militar, Bombeiros, SAMU)",
        pre_condition="A gravidade do incidente ou a política operacional exige acionamento de serviços públicos externos (UC02).",
        normal_flow=[
            "O sistema identifica quais instituições públicas de socorro (PM, Bombeiros ou SAMU) devem ser comunicadas com base na tipologia do incidente.",
            "O sistema formata o pacote de dados estruturado com endereço, gravidade, descrição sumária e veículos internos já enviados.",
            "O sistema transmite o alerta digitalmente via serviço web seguro (API) diretamente para a central do órgão correspondente.",
            "O sistema do órgão receptor processa a mensagem e envia de volta um protocolo de confirmação de recebimento.",
            "O sistema registra o protocolo e o horário de acionamento no relatório de auditoria do incidente."
        ],
        alt_flows={
            "Indisponibilidade ou queda de conexão com a API externa (Passo 3)": [
                "O sistema emite um alerta sonoro e visual de alta prioridade na tela do Operador do Sistema.",
                "O sistema apresenta na tela o telefone direto do órgão externo para acionamento por voz emergencial.",
                "O Operador realiza a ligação telefônica e cadastra o protocolo manual no sistema."
            ]
        },
        post_condition="As autoridades públicas externas pertinentes são formalmente notificadas e os protocolos vinculados à ocorrência."
    )
    
    # --- UC06 ---
    pdf.write_scenario(
        code="UC06",
        name="Monitorar Status dos Incidentes",
        actors="Operador do Sistema (Despachante), Relator",
        pre_condition="O usuário está devidamente autenticado no sistema com perfil de consulta operacional.",
        normal_flow=[
            "O usuário acessa o Painel de Monitoramento Geral de Ocorrências.",
            "O sistema renderiza a interface com mapa interativo contendo os incidentes em andamento e as posições dos veículos.",
            "O usuário seleciona um incidente específico na lista ou diretamente sobre o mapa.",
            "O sistema recupera e exibe a ficha completa: dados do relator, tempo transcorrido, veículos alocados, rotas e histórico de status.",
            "O usuário acompanha o progresso operacional até a finalização da ocorrência."
        ],
        alt_flows={},
        post_condition="O usuário obtém visão tática atualizada e precisa da situação dos incidentes em atendimento."
    )
    
    # --- UC07 ---
    pdf.write_scenario(
        code="UC07",
        name="Atualizar Status do Veículo",
        actors="Veículo (Equipe em Campo), Sistema",
        pre_condition="A viatura está em operação de campo cadastrada e conectada à rede de comunicação.",
        normal_flow=[
            "A equipe da viatura seleciona no painel de bordo a mudança de status operacional ('Em Trânsito', 'Chegada ao Local', 'Em Atendimento', 'Concluído/Disponível').",
            "O terminal transmite o novo status acompanhado de timestamp e coordenadas GPS atuais para a central.",
            "O sistema valida a transição lógica de estado da viatura e persiste a modificação no banco de dados.",
            "O sistema atualiza a situação da viatura em tempo real no painel do despachante (UC06)."
        ],
        alt_flows={
            "Transição de estado inválida no fluxo de atendimento (Passo 3)": [
                "O sistema rejeita a transição e mantém o estado anterior da viatura.",
                "O sistema emite aviso de inconformidade no display embarcado da equipe.",
                "O fluxo é encerrado sem alteração de estado."
            ]
        },
        post_condition="O estado operacional da viatura e sua disponibilidade são sincronizados fielmente no sistema."
    )

    # ---------------------------------------------------------
    # CONCLUSÃO E CONSIDERAÇÕES DE ENGENHARIA
    # ---------------------------------------------------------
    if pdf.get_y() > 150:
        pdf.add_page()
    else:
        pdf.ln(6)
    pdf.add_section("4. Conclusão e Considerações Finais")
    
    pdf.write_paragraph(
        "A modelagem de Casos de Uso e a especificação de cenários apresentadas consolidam uma arquitetura de requisitos "
        "sólida para o 'Sistema para Controle de Veículos em Incidentes'. Ao decompor o problema nos conceitos fundamentais da UML, "
        "foi possível contemplar rigorosamente todas as necessidades operacionais e contingências formuladas no enunciado:"
    )
    
    pdf.write_bullet_point(
        "Rastreabilidade Completa dos Requisitos", 
        "Todos os 5 fatores condicionantes (tipo de incidente, posição e telemetria, tipos de veículos, risco da área física e alerta automático externo) "
        "foram mapeados de forma direta em casos de uso centrais, inclusões e extensões condicionais."
    )
    pdf.write_bullet_point(
        "Resiliência e Continuidade Operacional", 
        "A inclusão de UC04 (Estimar Posição) e os fluxos alternativos de falhas telemáticas ou indisponibilidade de APIs públicas "
        "garantem que o sistema não entre em impasse crítico durante operações reais de socorro."
    )
    pdf.write_bullet_point(
        "Aderência Metodológica", 
        "O trabalho seguiu integralmente os padrões ensinados pelo Prof. Eduardo Figueiredo na disciplina de Engenharia de Software 1 (DCC/UFMG), "
        "utilizando a notação padrão de Casos de Uso e cenários textuais estruturados."
    )
    
    pdf.ln(12)
    pdf.set_draw_color(180, 180, 180)
    self_y = pdf.get_y()
    pdf.line(50, self_y, 160, self_y)
    pdf.ln(4)
    pdf.set_font('helvetica', 'I', 9)
    pdf.cell(0, 8, "Exercício de Engenharia de Software 1 - DCC / ICEx / UFMG", align='C')

    # Salva o arquivo final
    pdf.output(PDF_OUTPUT_FILE)
    print(f"Relatório PDF final gerado com sucesso em: {PDF_OUTPUT_FILE}")


def main():
    print("=== INICIANDO GERAÇÃO DO EXERCÍCIO DE CASOS DE USO ===")
    if generate_png_from_puml():
        build_pdf_report()
        print("=== PROCESSO FINALIZADO COM SUCESSO! ===")
    else:
        print("=== FALHA NA GERAÇÃO DA IMAGEM DO DIAGRAMA ===", file=sys.stderr)
        sys.exit(1)


if __name__ == '__main__':
    main()
