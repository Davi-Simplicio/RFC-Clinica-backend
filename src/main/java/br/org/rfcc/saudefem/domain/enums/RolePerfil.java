package br.org.rfcc.saudefem.domain.enums;

public enum RolePerfil {
    RECEPCIONISTA("Recepcionista", "Agenda e cadastro"),
    MEDICA("Médica", "Consulta e anamnese"),
    ENFERMEIRA("Enfermeira", "Procedimentos");

    private final String titulo;
    private final String descricao;

    RolePerfil(String titulo, String descricao) {
        this.titulo = titulo;
        this.descricao = descricao;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }
}
