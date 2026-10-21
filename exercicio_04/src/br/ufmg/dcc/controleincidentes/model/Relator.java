package br.ufmg.dcc.controleincidentes.model;

import java.time.LocalDateTime;
import java.util.Objects;

public abstract class Relator {
    protected String id;
    protected String nome;
    protected String telefone;

    public Relator(String id, String nome, String telefone) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
    }

    // Registra um incidente no sistema com os dados coletados pelo relator
    public Incidente relatarIncidente(String idIncidente, TipoIncidente tipo, NivelGravidade gravidade, String descricao, Localizacao local) {
        Incidente inc = new Incidente(idIncidente, LocalDateTime.now(), tipo, gravidade, descricao, local, this);
        inc.registrar();
        return inc;
    }

    // Sobrecarga compatível com a assinatura direta do diagrama UML
    public Incidente relatarIncidente(String dados) {
        return relatarIncidente("INC-" + System.currentTimeMillis() % 10000,
                TipoIncidente.ACIDENTE_TRANSITO,
                NivelGravidade.MEDIA,
                dados,
                new Localizacao(0.0, 0.0, "A definir", "Centro"));
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getTelefone() {
        return telefone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Relator)) return false;
        Relator relator = (Relator) o;
        return Objects.equals(id, relator.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", nome, telefone);
    }
}
