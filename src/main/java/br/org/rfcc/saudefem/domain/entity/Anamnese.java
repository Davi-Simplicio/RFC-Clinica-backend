package br.org.rfcc.saudefem.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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

    @Min(0)
    @Max(120)
    @Column(name = "idade_primeira_relacao")
    private Integer idadePrimeiraRelacao; // Coitarca (anos)

    @Min(0)
    @Max(120)
    @Column(name = "idade_primeira_menstruacao")
    private Integer idadePrimeiraMenstruacao; // Menarca (anos)

    @Column(name = "ano_ultima_menstruacao")
    private Integer anoUltimaMenstruacao;

    @Column(name = "ano_ultimo_parto")
    private Integer anoUltimoParto;

    @Min(0)
    @Column(name = "partos_normais", nullable = false)
    @Builder.Default
    private Integer partosNormais = 0;

    @Min(0)
    @Column(name = "partos_cesaria", nullable = false)
    @Builder.Default
    private Integer partosCesaria = 0;

    @Column(name = "ano_curetagem")
    private Integer anoCuretagem;

    @Column(name = "ano_cauterizacao")
    private Integer anoCauterizacao;

    @Column(name = "teve_aborto", nullable = false)
    @Builder.Default
    private Boolean teveAborto = false;

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
    private LocalDateTime dataAtualizacao;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false, unique = true)
    private Paciente paciente;

    @PrePersist
    @PreUpdate
    protected void registrarAtualizacao() {
        this.dataAtualizacao = LocalDateTime.now();
    }

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
