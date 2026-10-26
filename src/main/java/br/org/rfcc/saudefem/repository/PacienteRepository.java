package br.org.rfcc.saudefem.repository;

import br.org.rfcc.saudefem.domain.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    Optional<Paciente> findByCpf(String cpf);

    Optional<Paciente> findByCns(String cns);

    boolean existsByCpf(String cpf);

    List<Paciente> findByNomeCompletoContainingIgnoreCase(String nome);

    @Query("""
        SELECT p FROM Paciente p
        LEFT JOIN FETCH p.dadosRecepcao dr
        WHERE (:termo IS NULL OR LOWER(p.nomeCompleto) LIKE LOWER(CONCAT('%', :termo, '%'))
               OR p.cpf LIKE CONCAT('%', :termo, '%'))
          AND (:planoSaude IS NULL OR dr.planoSaude = :planoSaude)
        ORDER BY p.nomeCompleto ASC
    """)
    List<Paciente> buscarPorTermoEPlanoSaude(
            @Param("termo") String termo,
            @Param("planoSaude") String planoSaude
    );
}
