package br.org.rfcc.saudefem.domain.enums;

public enum StatusAgendamento {
    MARCADO("Marcado"),
    EM_ANDAMENTO("Em Andamento"),
    ATENDIDO("Atendido"),
    FALTOU("Faltou");

    private final String descricao;

    StatusAgendamento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
