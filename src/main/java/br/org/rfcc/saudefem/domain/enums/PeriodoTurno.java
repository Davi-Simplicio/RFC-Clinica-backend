package br.org.rfcc.saudefem.domain.enums;

public enum PeriodoTurno {
    MANHA("MANHÃ"),
    TARDE("TARDE");

    private final String rotulo;

    PeriodoTurno(String rotulo) {
        this.rotulo = rotulo;
    }

    public String getRotulo() {
        return rotulo;
    }
}
