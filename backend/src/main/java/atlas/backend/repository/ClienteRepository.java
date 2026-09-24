package atlas.backend.repository;

import atlas.backend.model.Cliente;
import atlas.backend.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    List<Cliente> findByEmpresaOrderByIdDesc(Empresa empresa);
    List<Cliente> findByEmpresaIdOrderByIdDesc(Long empresaId);
    List<Cliente> findByNombreContainingIgnoreCaseAndEmpresaIdOrderByNombreAsc(String nombre, Long empresaId);
    List<Cliente> findByNombreContainingIgnoreCaseOrderByNombreAsc(String nombre);
    Optional<Cliente> findByIdAndEmpresaId(Long id, Long empresaId);
    Optional<Cliente> findByCorreo(String correo);
    boolean existsByCorreo(String correo);
}
