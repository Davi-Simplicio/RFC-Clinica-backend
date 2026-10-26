package br.org.rfcc.saudefem.repository;

import br.org.rfcc.saudefem.domain.entity.Agendamento;
import br.org.rfcc.saudefem.domain.enums.PeriodoTurno;
import br.org.rfcc.saudefem.domain.enums.StatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    Optional<Agendamento> findByProtocolo(String protocolo);

    List<Agendamento> findByDataAtendimentoOrderByHorarioAsc(LocalDate dataAtendimento);

    List<Agendamento> findByDataAtendimentoAndStatusOrderByHorarioAsc(
            LocalDate dataAtendimento,
            StatusAgendamento status
    );

    List<Agendamento> findByDataAtendimentoAndPeriodoOrderByHorarioAsc(
            LocalDate dataAtendimento,
            PeriodoTurno periodo
    );

    List<Agendamento> findByPacienteIdOrderByDataAtendimentoDescHorarioDesc(Long pacienteId);

    List<Agendamento> findByProfissionalResponsavelIdAndDataAtendimentoOrderByHorarioAsc(
            Long profissionalId,
            LocalDate dataAtendimento
    );

    @Query("""
        SELECT DISTINCT a FROM Agendamento a
        JOIN FETCH a.paciente p
        JOIN FETCH a.profissionalResponsavel prof
        LEFT JOIN FETCH a.procedimentos proc
        WHERE a.dataAtendimento = :data
          AND (:status IS NULL OR a.status = :status)
          AND (:termo IS NULL
               OR LOWER(p.nomeCompleto) LIKE LOWER(CONCAT('%', :termo, '%'))
               OR LOWER(a.protocolo) LIKE LOWER(CONCAT('%', :termo, '%'))
               OR LOWER(proc.nome) LIKE LOWER(CONCAT('%', :termo, '%')))
        ORDER BY a.horario ASC
    """)
    List<Agendamento> filtrarAgendaDoDia(
            @Param("data") LocalDate data,
            @Param("status") StatusAgendamento status,
            @Param("termo") String termo
    );
}
