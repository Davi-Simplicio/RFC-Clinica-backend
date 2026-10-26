package br.org.rfcc.saudefem.domain.enums;

public enum TipoTelefone {
    CELULAR("Celular"),
    FIXO("Fixo"),
    RECADO("Recado"),
    TRABALHO("Trabalho");

    private final String rotulo;

    TipoTelefone(String rotulo) {
        this.rotulo = rotulo;
    }

    public String getRotulo() {
        return rotulo;
    }
}
