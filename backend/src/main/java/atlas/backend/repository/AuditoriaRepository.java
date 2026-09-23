package atlas.backend.repository;

import atlas.backend.model.Auditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {

    // Listar todos los registros ordenados por id descendente (mas reciente primero)
    List<Auditoria> findAllByOrderByIdDesc();

    // Filtrar logs por nombre de usuario
    List<Auditoria> findByUsuarioIdOrderByIdDesc(String usuarioId);
}
