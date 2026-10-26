package br.org.rfcc.saudefem.domain.entity;

import br.org.rfcc.saudefem.domain.enums.PeriodoTurno;
import br.org.rfcc.saudefem.domain.enums.StatusAgendamento;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(
    name = "tb_agendamentos",
    indexes = {
        @Index(name = "idx_agendamento_data_horario", columnList = "data_atendimento, horario"),
        @Index(name = "idx_agendamento_status", columnList = "status")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "protocolo", nullable = false, unique = true, length = 30)
    private String protocolo; // Ex: PROT-2026-0847

    @NotNull
    @Column(name = "data_atendimento", nullable = false)
    private LocalDate dataAtendimento;

    @NotNull
    @Column(name = "horario", nullable = false)
    private LocalTime horario;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "periodo", nullable = false, length = 15)
    private PeriodoTurno periodo;

    @NotNull
    @Column(name = "duracao_minutos", nullable = false)
    @Builder.Default
    private Integer duracaoMinutos = 20;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private StatusAgendamento status = StatusAgendamento.MARCADO;

    @Column(name = "encaixe", nullable = false)
    @Builder.Default
    private Boolean encaixe = false;

    @Column(name = "notificado_whatsapp", nullable = false)
    @Builder.Default
    private Boolean notificadoWhatsapp = false;

    @Column(name = "criado_em", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime criadoEm = LocalDateTime.now();

    // Relacionamento N:1 com Paciente
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    // Relacionamento N:1 com Usuario (Profissional / Médica responsável)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "profissional_id", nullable = false)
    private Usuario profissionalResponsavel;

    // Relacionamento N:M com Procedimento (todo agendamento contempla ao menos 1 procedimento)
    @NotEmpty
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "tb_agendamento_procedimentos",
        joinColumns = @JoinColumn(name = "agendamento_id"),
        inverseJoinColumns = @JoinColumn(name = "procedimento_id")
    )
    @Builder.Default
    private Set<Procedimento> procedimentos = new HashSet<>();

    public void adicionarProcedimento(Procedimento procedimento) {
        this.procedimentos.add(procedimento);
        procedimento.getAgendamentos().add(this);
    }

    public void atualizarStatus(StatusAgendamento novoStatus) {
        this.status = novoStatus;
    }

    public String gerarMensagemConfirmacaoWhatsApp() {
        String primeiroNome = (paciente != null && paciente.getNomeCompleto() != null)
                ? paciente.getNomeCompleto().split(" ")[0]
                : "Paciente";
        String dataFmt = (dataAtendimento != null)
                ? dataAtendimento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                : "";
        String horaFmt = (horario != null)
                ? horario.format(DateTimeFormatter.ofPattern("HH:mm"))
                : "";
        String listaProcs = procedimentos.isEmpty()
                ? "Consulta Clínica"
                : procedimentos.stream().map(Procedimento::getNome).collect(Collectors.joining(", "));

        return String.format(
                "Olá, %s! 👋\n\nSua consulta está confirmada para:\n📅 %s às %s\n🩺 %s\n📋 Protocolo: %s",
                primeiroNome, dataFmt, horaFmt, listaProcs, protocolo
        );
    }
}
