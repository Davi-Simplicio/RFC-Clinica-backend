package br.org.rfcc.saudefem.repository;

import br.org.rfcc.saudefem.domain.entity.TelefoneContato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TelefoneContatoRepository extends JpaRepository<TelefoneContato, Long> {

    List<TelefoneContato> findByPacienteId(Long pacienteId);

    Optional<TelefoneContato> findFirstByPacienteIdAndPossuiWhatsappTrue(Long pacienteId);
}
