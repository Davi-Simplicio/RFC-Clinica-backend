package br.org.rfcc.saudefem.repository;

import br.org.rfcc.saudefem.domain.entity.Procedimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProcedimentoRepository extends JpaRepository<Procedimento, Long> {

    Optional<Procedimento> findByCodigoIgnoreCase(String codigo);

    List<Procedimento> findByAtivoTrueOrderByNomeAsc();

    List<Procedimento> findByNomeContainingIgnoreCase(String nome);
}
