package br.org.rfcc.saudefem.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_anamneses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Anamnese {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "primeira_relacao", length = 20)
    private String primeiraRelacao;

    @Column(name = "primeira_menstruacao", length = 20)
    private String primeiraMenstruacao;

    @Column(name = "ano_ultima_menstruacao", length = 10)
    private String anoUltimaMenstruacao;

    @Column(name = "ano_ultimo_parto", length = 10)
    private String anoUltimoParto;

    @Column(name = "partos_normais", nullable = false)
    @Builder.Default
    private Integer partosNormais = 0;

    @Column(name = "partos_cesaria", nullable = false)
    @Builder.Default
    private Integer partosCesaria = 0;

    @Column(name = "curetagem_ano", length = 10)
    private String curetagemAno;

    @Column(name = "cauterizacao_ano", length = 10)
    private String cauterizacaoAno;

    @Column(name = "deja_aborto", nullable = false)
    @Builder.Default
    private Boolean dejaAborto = false;

    @Column(name = "toma_anticoncepcional", nullable = false)
    @Builder.Default
    private Boolean tomaAnticoncepcional = false;

    @Column(name = "cancer_familia", columnDefinition = "TEXT")
    private String cancerFamilia;

    @Column(name = "mamas", columnDefinition = "TEXT")
    private String mamas;

    @Column(name = "sintomas", columnDefinition = "TEXT")
    private String sintomas;

    @Column(name = "data_atualizacao", nullable = false)
    @Builder.Default
    private LocalDateTime dataAtualizacao = LocalDateTime.now();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false, unique = true)
    private Paciente paciente;

    public int calcularTotalPartos() {
        int normais = (partosNormais != null) ? partosNormais : 0;
        int cesarias = (partosCesaria != null) ? partosCesaria : 0;
        return normais + cesarias;
    }

    public boolean possuiHistoricoOncologicoFamiliar() {
        return cancerFamilia != null
                && !cancerFamilia.isBlank()
                && !cancerFamilia.toLowerCase().contains("sem histórico")
                && !cancerFamilia.toLowerCase().contains("nenhum caso");
    }
}
