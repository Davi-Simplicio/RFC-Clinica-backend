package br.org.rfcc.saudefem.repository;

import br.org.rfcc.saudefem.domain.entity.Usuario;
import br.org.rfcc.saudefem.domain.enums.RolePerfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmailIgnoreCase(String email);

    List<Usuario> findByPerfilAndAtivoTrue(RolePerfil perfil);

    boolean existsByEmailIgnoreCase(String email);
}
