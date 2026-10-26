package br.org.rfcc.saudefem.repository;

import br.org.rfcc.saudefem.domain.entity.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

    Optional<Endereco> findByPacienteId(Long pacienteId);

    List<Endereco> findByCidadeIgnoreCaseAndEstadoIgnoreCase(String cidade, String estado);

    List<Endereco> findByCep(String cep);
}
