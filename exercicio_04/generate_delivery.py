import os
import sys
import subprocess
import zipfile
from fpdf import FPDF

if sys.stdout and hasattr(sys.stdout, 'reconfigure'):
    sys.stdout.reconfigure(encoding='utf-8')
if sys.stderr and hasattr(sys.stderr, 'reconfigure'):
    sys.stderr.reconfigure(encoding='utf-8')

BASE_DIR = os.path.dirname(os.path.abspath(__file__))
SRC_DIR = os.path.join(BASE_DIR, 'src')
BIN_DIR = os.path.join(BASE_DIR, 'bin')
PNG_FILE = os.path.join(BASE_DIR, 'diagrama_classes.png')
PUML_FILE = os.path.join(BASE_DIR, 'diagrama_classes.puml')
README_FILE = os.path.join(BASE_DIR, 'README.md')

PDF_OUTPUT = os.path.join(BASE_DIR, '[Java] Italo Leal.pdf')
ZIP_OUTPUT = os.path.join(BASE_DIR, '[Java] Italo Leal.zip')


def setup_java_env():
    current_path = os.environ.get('PATH', '')
    candidates = [
        r'C:\Program Files\Eclipse Adoptium\jdk-17.0.20.101-hotspot\bin',
        r'C:\Program Files\Eclipse Adoptium\jdk-17\bin',
        r'C:\Program Files\Java\jdk-17\bin'
    ]
    for c in candidates:
        if os.path.exists(c) and c not in current_path:
            os.environ['PATH'] = c + os.pathsep + os.environ['PATH']
            current_path = os.environ['PATH']


def compile_java():
    print("Compilando código Java...")
    os.makedirs(BIN_DIR, exist_ok=True)
    java_files = []
    for root, _, files in os.walk(SRC_DIR):
        for f in files:
            if f.endswith('.java'):
                java_files.append(os.path.join(root, f))
    
    cmd = ['javac', '-encoding', 'UTF-8', '-d', BIN_DIR] + java_files
    res = subprocess.run(cmd, capture_output=True, text=True, encoding='utf-8', errors='replace')
    if res.returncode != 0:
        print("Erro ao compilar:", res.stderr)
        sys.exit(1)
    print(f"Compilação concluída: {len(java_files)} arquivos .java compilados.")


def run_tests():
    print("Executando testes...")
    cmd = ['java', '-cp', BIN_DIR, 'br.ufmg.dcc.controleincidentes.test.TesteSistemaControleIncidentes']
    res = subprocess.run(cmd, capture_output=True, text=True, encoding='utf-8', errors='replace')
    print(res.stdout)
    if res.returncode != 0:
        print("Falha nos testes:", res.stderr)
        sys.exit(1)


def run_main():
    print("Executando Main...")
    cmd = ['java', '-cp', BIN_DIR, 'br.ufmg.dcc.controleincidentes.Main']
    res = subprocess.run(cmd, capture_output=True, text=True, encoding='utf-8', errors='replace')
    if res.returncode != 0:
        print("Falha na Main:", res.stderr)
        sys.exit(1)


class StudentReportPDF(FPDF):
    def __init__(self):
        super().__init__(orientation='P', unit='mm', format='A4')
        self.set_margins(16, 14, 16)
        self.set_auto_page_break(auto=True, margin=14)

    def normalize_text(self, text):
        if isinstance(text, str):
            text = (text.replace('\u2014', ' - ')
                        .replace('\u2013', ' - ')
                        .replace('\u2022', '-')
                        .replace('\u201c', '"')
                        .replace('\u201d', '"')
                        .replace('\u2018', "'")
                        .replace('\u2019', "'")
                        .replace('\u2026', '...'))
        return super().normalize_text(text)

    def header(self):
        self.set_font('helvetica', 'B', 9.5)
        self.set_text_color(0, 102, 204)
        self.cell(120, 5, 'UFMG / ICEx / DCC - Engenharia de Software 1 - Prof. Eduardo Figueiredo', align='L')
        self.set_font('helvetica', '', 9)
        self.set_text_color(90, 90, 90)
        self.cell(0, 5, f'Aluno: Ítalo Leal | Página {self.page_no()}/3', align='R')
        self.ln(5.5)
        self.set_draw_color(0, 102, 204)
        self.set_line_width(0.6)
        self.line(16, self.get_y(), 194, self.get_y())
        self.ln(3.5)

    def footer(self):
        self.set_y(-10)
        self.set_font('helvetica', 'I', 8)
        self.set_text_color(130, 130, 130)
        self.cell(0, 5, 'Exercício de Aula: Implementação Java - Sistema para Controle de Veículos em Incidentes', align='C')


def generate_pdf():
    print("Gerando PDF da entrega...")
    pdf = StudentReportPDF()

    # PÁGINA 1
    pdf.add_page()

    pdf.set_font('helvetica', 'B', 13.5)
    pdf.set_text_color(25, 25, 25)
    pdf.cell(0, 7, 'Exercício: Implementação Java - Sistema para Controle de Veículos em Incidentes', align='L')
    pdf.ln(6.5)

    pdf.set_font('helvetica', '', 9.3)
    pdf.set_text_color(55, 55, 55)
    pdf.multi_cell(0, 4.5, (
        'Implementação orientada a objetos desenvolvida a partir do Diagrama de Classes elaborado no Exercício 3. '
        'O código contempla a modelagem do ciclo de vida de incidentes, cálculo de distâncias geodésicas, estimativa de '
        'posicionamento para veículos sem sinal GPS, controle de envio para áreas de risco e despacho inteligente com '
        'acionamento automático de órgãos de emergência.'
    ))
    pdf.ln(3)

    pdf.set_font('helvetica', 'B', 11)
    pdf.set_text_color(0, 102, 204)
    pdf.cell(0, 6, '1. Diagrama de Classes UML de Referência', align='L')
    pdf.ln(5)

    if os.path.exists(PNG_FILE):
        y_diag = pdf.get_y()
        largura_img = 178
        pdf.image(PNG_FILE, x=16, y=y_diag, w=largura_img)
        pdf.set_y(y_diag + 60)
        pdf.set_font('helvetica', 'I', 8)
        pdf.set_text_color(110, 110, 110)
        pdf.cell(0, 4, 'Figura 1: Diagrama de Classes UML modelado no Exercício 3 e seguido na implementação.', align='C')
        pdf.ln(5)

    pdf.set_font('helvetica', 'B', 11)
    pdf.set_text_color(0, 102, 204)
    pdf.cell(0, 6, '2. Pacotes e Classes Implementadas', align='L')
    pdf.ln(4.5)

    pacotes = [
        ("model", "Contém as entidades do domínio: Incidente, Localizacao, AreaDeRisco, ServicoEmergencia, a classe abstrata Veiculo (especializada em ViaturaPolicial, CaminhaoBombeiro e Ambulancia), a classe abstrata Relator (especializada em Policial e Bombeiro) e os enums de apoio (TipoIncidente, NivelGravidade, StatusIncidente e StatusVeiculo)."),
        ("service", "Contém a classe ControleDespacho, responsável pelas regras de alocação de veículos mais próximos, verificação de segurança em áreas de risco e disparo de alertas externos."),
        ("test", "Contém a classe TesteSistemaControleIncidentes, com 8 métodos de teste cobrindo o funcionamento dos fatores especificados no enunciado.")
    ]

    for pac, desc in pacotes:
        pdf.set_font('helvetica', 'B', 9)
        pdf.set_text_color(0, 102, 204)
        pdf.write(4.4, "- ")
        pdf.write(4.4, pac)
        pdf.set_text_color(50, 50, 50)
        pdf.set_font('helvetica', '', 9)
        pdf.write(4.4, f": {desc}\n")
        pdf.ln(1.5)

    # PÁGINA 2
    pdf.add_page()

    pdf.set_font('helvetica', 'B', 12.5)
    pdf.set_text_color(0, 102, 204)
    pdf.cell(0, 6, '3. Atendimento aos 5 Fatores da Especificação', align='L')
    pdf.ln(6)

    fatores = [
        (
            "(i) Tipo de Incidente e Urgência",
            "Representados pelos enums TipoIncidente (Acidente de Trânsito, Incêndio, Assalto, Emergência Médica) e NivelGravidade (Baixa, Média, Alta). Em ControleDespacho, o tipo orienta quais veículos especializados alocar, e a gravidade Alta determina prioridade e exigência de múltiplos veículos."
        ),
        (
            "(ii) Posição das Viaturas e Estimativa",
            "A classe Localizacao calcula a distância geodésica em quilômetros usando a fórmula de Haversine. A classe Veiculo implementa solicitarPosicao(), que retorna a posição GPS direta se disponível, ou aciona estimarPosicao() (projeção baseada na última posição conhecida) quando o sinal estiver indisponível."
        ),
        (
            "(iii) Tipos de Veículos Disponíveis",
            "A frota é representada pela classe base Veiculo especializada em ViaturaPolicial (com blindagem e armamento), CaminhaoBombeiro (com capacidade do tanque de água e escada Magirus) e Ambulancia (com UTI móvel e suporte avançado). A relação 1..* com Incidente permite despachar comboios de viaturas."
        ),
        (
            "(iv) Localização e Áreas de Risco",
            "A classe AreaDeRisco possui o método contem(Localizacao). Em ControleDespacho, a operação validarEnvioSeguro() impede o despacho de apenas uma viatura isolada para áreas de risco e assegura que a quantidade mínima de veículos seja atendida."
        ),
        (
            "(v) Alerta aos Serviços de Emergência",
            "A classe ServicoEmergencia modela entidades como Polícia Militar, Bombeiros e SAMU. Em incidentes de alta gravidade ou que demandem integração entre órgãos, o método acionarServicosEmergencia() emite notificações aos serviços cadastrados."
        ),
        (
            "Entidade Relator",
            "Conforme pedido no enunciado, os dados do incidente são registrados por um relator. A classe abstrata Relator é especializada em Policial e Bombeiro, identificando quem atendeu ao chamado de emergência."
        )
    ]

    for tit, d in fatores:
        pdf.set_font('helvetica', 'B', 9.5)
        pdf.set_text_color(0, 102, 204)
        pdf.write(4.5, "- ")
        pdf.write(4.5, tit)
        pdf.set_text_color(40, 40, 40)
        pdf.set_font('helvetica', '', 9)
        pdf.write(4.5, f": {d}\n")
        pdf.ln(2.2)

    # PÁGINA 3
    pdf.add_page()

    pdf.set_font('helvetica', 'B', 12.5)
    pdf.set_text_color(0, 102, 204)
    pdf.cell(0, 6, '4. Idiomas de Programação Java e Conceitos OO', align='L')
    pdf.ln(6)

    conceitos = [
        ("Encapsulamento", "Atributos privados e protegidos, métodos de acesso e modificadores controlados. As listas de veículos alocados e serviços acionados na classe Incidente são retornadas como visualizações imutáveis com Collections.unmodifiableList()."),
        ("Herança e Polimorfismo", "Classes abstratas Veiculo e Relator definindo o contrato comum, com subclasses sobrescrevendo toString() e implementando comportamentos próprios como patrulhar(), combaterFogo() e socorrerVitima()."),
        ("Coesão e Acoplamento", "Separação clara entre classes de modelo (entidades de dados e comportamento local) e a classe de serviço ControleDespacho (regras de seleção e despacho de frotas)."),
        ("Convenções de Código", "Uso das convenções de nomenclatura Java (camelCase para métodos e atributos, PascalCase para classes e UPPER_SNAKE_CASE para enums).")
    ]

    for tit, d in conceitos:
        pdf.set_font('helvetica', 'B', 9.5)
        pdf.set_text_color(0, 102, 204)
        pdf.write(4.5, "* ")
        pdf.write(4.5, tit)
        pdf.set_text_color(40, 40, 40)
        pdf.set_font('helvetica', '', 9)
        pdf.write(4.5, f": {d}\n")
        pdf.ln(2)

    pdf.ln(2)
    pdf.set_font('helvetica', 'B', 11.5)
    pdf.set_text_color(0, 102, 204)
    pdf.cell(0, 6, '5. Resumo da Execução dos Testes', align='L')
    pdf.ln(5)

    tabela_testes = [
        ("Cálculo de distância geodésica (Haversine)", "Fator ii", "Aprovado"),
        ("Detecção de ponto dentro e fora de área de risco", "Fator iv", "Aprovado"),
        ("Fallback para estimativa de posição com GPS offline", "Fator ii", "Aprovado"),
        ("Ciclo de vida do incidente e status dos veículos", "Domínio", "Aprovado"),
        ("Bloqueio de viatura única em área de risco", "Fator iv", "Aprovado"),
        ("Acionamento automático de órgãos parceiros em alta gravidade", "Fator v", "Aprovado"),
        ("Associação do incidente ao relator policial", "Relator", "Aprovado"),
        ("Priorização de viaturas especializadas por tipo de sinistro", "Fator iii", "Aprovado")
    ]

    pdf.set_font('helvetica', 'B', 8.5)
    pdf.set_fill_color(230, 242, 255)
    pdf.set_text_color(0, 80, 160)
    pdf.cell(115, 5, "Descrição do Teste", border=1, align='L', fill=True)
    pdf.cell(35, 5, "Fator Avaliado", border=1, align='C', fill=True)
    pdf.cell(28, 5, "Status", border=1, align='C', fill=True)
    pdf.ln(5)

    pdf.set_font('helvetica', '', 8.5)
    for desc_t, fator_t, status_t in tabela_testes:
        pdf.set_text_color(50, 50, 50)
        pdf.cell(115, 4.5, desc_t, border=1, align='L')
        pdf.cell(35, 4.5, fator_t, border=1, align='C')
        pdf.set_font('helvetica', 'B', 8.5)
        pdf.set_text_color(0, 130, 50)
        pdf.cell(28, 4.5, status_t, border=1, align='C')
        pdf.set_font('helvetica', '', 8.5)
        pdf.ln(4.5)

    pdf.output(PDF_OUTPUT)
    print(f"PDF gerado com sucesso: {PDF_OUTPUT}")


def package_zip():
    print(f"Gerando arquivo compactado: {ZIP_OUTPUT}...")
    if os.path.exists(ZIP_OUTPUT):
        os.remove(ZIP_OUTPUT)

    with zipfile.ZipFile(ZIP_OUTPUT, 'w', zipfile.ZIP_DEFLATED) as z:
        for root, _, files in os.walk(SRC_DIR):
            for file in files:
                abs_p = os.path.join(root, file)
                rel_p = os.path.relpath(abs_p, BASE_DIR)
                z.write(abs_p, rel_p)

        for root, _, files in os.walk(BIN_DIR):
            for file in files:
                abs_p = os.path.join(root, file)
                rel_p = os.path.relpath(abs_p, BASE_DIR)
                z.write(abs_p, rel_p)

        for extra in ['diagrama_classes.puml', 'diagrama_classes.png', 'README.md', '[Java] Italo Leal.pdf']:
            p = os.path.join(BASE_DIR, extra)
            if os.path.exists(p):
                z.write(p, extra)

    print(f"Arquivo ZIP gerado com sucesso ({os.path.getsize(ZIP_OUTPUT)} bytes).")


def main():
    setup_java_env()
    compile_java()
    run_tests()
    run_main()
    generate_pdf()
    package_zip()
    print("\nProcesso finalizado com sucesso!")


if __name__ == '__main__':
    main()
