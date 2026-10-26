package br.org.rfcc.saudefem.repository;

import br.org.rfcc.saudefem.domain.entity.Anamnese;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnamneseRepository extends JpaRepository<Anamnese, Long> {

    Optional<Anamnese> findByPacienteId(Long pacienteId);

    List<Anamnese> findByTomaAnticoncepcionalTrue();

    List<Anamnese> findByTeveAbortoTrue();
}
