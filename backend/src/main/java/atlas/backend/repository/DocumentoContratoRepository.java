package atlas.backend.repository;

import atlas.backend.model.DocumentoContrato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentoContratoRepository extends JpaRepository<DocumentoContrato, Long> {

    // Obtener el documento mas reciente de un contrato (equivalente a ORDER BY id DESC LIMIT 1)
    Optional<DocumentoContrato> findTopByContratoIdOrderByIdDesc(Long contratoId);

    // Listar todos los documentos de un contrato
    List<DocumentoContrato> findByContratoIdOrderByIdDesc(Long contratoId);

    // Eliminar todos los documentos de un contrato
    void deleteByContratoId(Long contratoId);
}
