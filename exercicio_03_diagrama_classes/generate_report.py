import os
import sys
import urllib.request
import plantuml
from fpdf import FPDF

# -----------------------------------------------------------------------------
# Configurações de Diretórios e Nomes de Arquivos
# -----------------------------------------------------------------------------
BASE_DIR = os.path.dirname(os.path.abspath(__file__))
PLANTUML_URL = 'http://www.plantuml.com/plantuml/png/'

PUML_FILE = os.path.join(BASE_DIR, 'diagrama_classes.puml')
PNG_FILE = os.path.join(BASE_DIR, 'diagrama_classes.png')

PDF_STUDENT_OUTPUT = os.path.join(BASE_DIR, '[Classes] Italo Leal.pdf')
PDF_STANDARD_OUTPUT = os.path.join(BASE_DIR, 'exercicio-respostas.pdf')


class SimpleExercisePDF(FPDF):
    def __init__(self):
        super().__init__(orientation='L', unit='mm', format='A4')
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
        # Cabeçalho institucional limpo e padronizado em todas as páginas
        self.set_font('helvetica', 'B', 10)
        self.set_text_color(0, 102, 204)
        self.cell(170, 5, 'UFMG / ICEx / DCC - Engenharia de Software 1 - Prof. Eduardo Figueiredo', align='L')
        
        self.set_font('helvetica', '', 9.5)
        self.set_text_color(90, 90, 90)
        self.cell(0, 5, f'Aluno: Ítalo Leal | Página {self.page_no()}/2', align='R')
        self.ln(5.5)
        
        self.set_draw_color(0, 102, 204)
        self.set_line_width(0.6)
        self.line(16, self.get_y(), 281, self.get_y())
        self.ln(4)

    def footer(self):
        self.set_y(-11)
        self.set_font('helvetica', 'I', 8)
        self.set_text_color(130, 130, 130)
        self.cell(0, 6, 'Exercício de Aula: Diagrama de Classes - Sistema para Controle de Veículos em Incidentes', align='C')


def generate_png_if_needed():
    if not os.path.exists(PUML_FILE):
        print(f"Erro: Arquivo {PUML_FILE} não encontrado.", file=sys.stderr)
        return False
        
    print("Renderizando diagrama_classes.puml via PlantUML...")
    try:
        with open(PUML_FILE, 'r', encoding='utf-8') as f:
            code = f.read()
        encoded = plantuml.deflate_and_encode(code)
        url = PLANTUML_URL + encoded
        req = urllib.request.Request(url, headers={'User-Agent': 'Mozilla/5.0'})
        with urllib.request.urlopen(req) as resp:
            if resp.status == 200:
                data = resp.read()
                with open(PNG_FILE, 'wb') as out:
                    out.write(data)
                print(f"PNG gerado com sucesso ({len(data)} bytes).")
                return True
    except Exception as e:
        print(f"Aviso ao tentar renderizar online: {e}")
        if os.path.exists(PNG_FILE):
            print("Utilizando arquivo PNG existente.")
            return True
        return False
    return os.path.exists(PNG_FILE)


def build_pdf():
    print("Construindo relatório objetivo de 2 páginas...")
    pdf = SimpleExercisePDF()

    # -------------------------------------------------------------------------
    # PÁGINA 1: TÍTULO, IDENTIFICAÇÃO E DIAGRAMA DE CLASSES COMPLETO
    # -------------------------------------------------------------------------
    pdf.add_page()

    # Título da Atividade
    pdf.set_font('helvetica', 'B', 14)
    pdf.set_text_color(25, 25, 25)
    pdf.cell(0, 7, 'Exercício: Diagrama de Classes - Sistema para Controle de Veículos em Incidentes', align='L')
    pdf.ln(6.5)

    # Breve contextualização
    pdf.set_font('helvetica', '', 9.5)
    pdf.set_text_color(60, 60, 60)
    pdf.multi_cell(0, 4.8, (
        'Modelagem conceitual e estrutural orientada a objetos desenvolvida a partir da especificação do sistema. '
        'O diagrama contempla o ciclo de atendimento a incidentes, localização e áreas de risco, despacho de viaturas '
        'com estimativa de posicionamento, especializações da frota e acionamento de serviços de emergência externos.'
    ))
    pdf.ln(3)

    # Inclusão do Diagrama em alta resolução
    if os.path.exists(PNG_FILE):
        y_diagrama = pdf.get_y()
        # Largura total útil em A4 Paisagem com margens de 16mm: 297 - 32 = 265mm
        largura_img = 265
        pdf.image(PNG_FILE, x=16, y=y_diagrama, w=largura_img)
        
        # A imagem tem ratio ~3.11 -> altura ~85mm
        pdf.set_y(y_diagrama + 88)
        pdf.set_font('helvetica', 'I', 8.5)
        pdf.set_text_color(110, 110, 110)
        pdf.cell(0, 5, 'Figura 1: Diagrama de Classes UML com atributos, métodos, visibilidades e multiplicidades.', align='C')

    # -------------------------------------------------------------------------
    # PÁGINA 2: EXPLICAÇÃO OBJETIVA DOS 5 FATORES DO ENUNCIADO
    # -------------------------------------------------------------------------
    pdf.add_page()

    pdf.set_font('helvetica', 'B', 13)
    pdf.set_text_color(0, 102, 204)
    pdf.cell(0, 7, 'Mapeamento das Decisões de Modelagem (Atendimento aos 5 Fatores)', align='L')
    pdf.ln(8)

    fatores = [
        (
            "(i) Tipo e Urgência do Incidente",
            "O tipo de incidente e o nível de gravidade são modelados por enums (TipoIncidente e NivelGravidade) na classe Incidente. "
            "Isso permite ao ControleDespacho identificar incidentes críticos que exigem resposta imediata e priorizá-los na fila de despacho."
        ),
        (
            "(ii) Posição das Viaturas e Estimativa de Localização",
            "A classe Veiculo compõe sua posicaoAtual (Localizacao). Possui as operações solicitarPosicao() (obtenção direta via GPS) e "
            "estimarPosicao() (para casos em que o sinal da viatura está inacessível ou impreciso, calculando a posição estimada por aproximação)."
        ),
        (
            "(iii) Tipos de Veículos Disponíveis e Especializações",
            "A frota é representada pela classe base Veiculo especializada em ViaturaPolicial, CaminhaoBombeiro e Ambulancia. Cada subclasse "
            "possui atributos e operações próprias da sua função. A relação entre Incidente e Veiculo com multiplicidade 1..* permite despachar "
            "tanto um veículo único quanto uma série/comboio de veículos de diferentes tipos."
        ),
        (
            "(iv) Localização do Incidente e Áreas de Risco",
            "A classe AreaDeRisco delimita setores geográficos perigosos que exigem o despacho de múltiplos veículos (exigeMultiplosVeiculos: boolean "
            "e qtdMinimaVeiculos: int). A operação validarEnvioSeguro() em ControleDespacho impede que uma viatura isolada seja enviada a áreas perigosas."
        ),
        (
            "(v) Alerta Automático a Serviços de Emergência",
            "A classe ServicoEmergencia modela as entidades externas (Polícia Militar, Bombeiros, SAMU). A operação acionarServicosEmergencia() "
            "do ControleDespacho avalia a ocorrência e dispara automaticamente o alerta de apoio externo quando o incidente for de alta gravidade."
        ),
        (
            "Entidade Relator",
            "Conforme indicado no enunciado, todo incidente é aberto por um relator. A classe abstrata Relator é especializada em Policial e Bombeiro, "
            "registrando quem atendeu ao chamado de emergência e permitindo a rastreabilidade da denúncia."
        )
    ]

    for titulo, desc in fatores:
        pdf.set_font('helvetica', 'B', 10)
        pdf.set_text_color(0, 102, 204)
        pdf.write(5.4, "- ")
        pdf.write(5.4, titulo)
        pdf.set_text_color(35, 35, 35)
        pdf.set_font('helvetica', '', 9.5)
        pdf.write(5.4, f": {desc}\n")
        pdf.ln(3.2)

    # Salva os arquivos finais
    pdf.output(PDF_STUDENT_OUTPUT)
    pdf.output(PDF_STANDARD_OUTPUT)
    print(f"Sucesso! Relatório gerado em:")
    print(f"  -> {PDF_STUDENT_OUTPUT}")
    print(f"  -> {PDF_STANDARD_OUTPUT}")


def main():
    if generate_png_if_needed():
        build_pdf()
    else:
        print("Falha ao gerar o diagrama PNG.", file=sys.stderr)
        sys.exit(1)


if __name__ == '__main__':
    main()
