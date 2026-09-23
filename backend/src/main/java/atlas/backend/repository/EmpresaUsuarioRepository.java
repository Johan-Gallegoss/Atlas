package atlas.backend.repository;

import atlas.backend.model.EmpresaUsuario;
import atlas.backend.model.Empresa;
import atlas.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmpresaUsuarioRepository extends JpaRepository<EmpresaUsuario, Long> {
    List<EmpresaUsuario> findByEmpresa(Empresa empresa);
    List<EmpresaUsuario> findByEmpresaId(Long empresaId);
    List<EmpresaUsuario> findByUsuario(Usuario usuario);
    Optional<EmpresaUsuario> findTopByUsuarioIdOrderByIdDesc(Long usuarioId);
}
