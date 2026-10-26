package br.org.rfcc.saudefem.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tb_procedimentos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Procedimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "codigo", nullable = false, unique = true, length = 30)
    private String codigo; // Ex: PROC-001

    @NotBlank
    @Column(name = "nome", nullable = false, length = 120)
    private String nome; // Ex: Exame de Mama e Preventivo, Consulta Ginecológica, Colposcopia, Ultrassom Pélvico

    @Column(name = "descricao", length = 255)
    private String descricao;

    @NotNull
    @Column(name = "duracao_padrao_minutos", nullable = false)
    @Builder.Default
    private Integer duracaoPadraoMinutos = 20;

    @Column(name = "ativo", nullable = false)
    @Builder.Default
    private Boolean ativo = true;

    @ManyToMany(mappedBy = "procedimentos", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Agendamento> agendamentos = new HashSet<>();
}
