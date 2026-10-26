package br.org.rfcc.saudefem.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_dados_recepcao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DadosRecepcao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dum")
    private LocalDate dum; // Data da Última Menstruação (D.U.M.)

    @Column(name = "plano_saude", nullable = false, length = 60)
    @Builder.Default
    private String planoSaude = "Particular"; // Particular, SUS, Unimed, Bradesco Saúde, etc.

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "atualizado_em", nullable = false)
    @Builder.Default
    private LocalDateTime atualizadoEm = LocalDateTime.now();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false, unique = true)
    private Paciente paciente;

    public boolean isAtendimentoSus() {
        return "SUS".equalsIgnoreCase(this.planoSaude);
    }
}
