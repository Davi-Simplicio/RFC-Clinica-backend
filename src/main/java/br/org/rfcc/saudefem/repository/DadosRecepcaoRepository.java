package br.org.rfcc.saudefem.repository;

import br.org.rfcc.saudefem.domain.entity.DadosRecepcao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DadosRecepcaoRepository extends JpaRepository<DadosRecepcao, Long> {

    Optional<DadosRecepcao> findByPacienteId(Long pacienteId);

    List<DadosRecepcao> findByPlanoSaudeIgnoreCase(String planoSaude);
}
