package atlas.backend.repository;

import atlas.backend.model.Contrato;
import atlas.backend.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContratoRepository extends JpaRepository<Contrato, Long> {

    // Listar contratos de una empresa (via cliente.empresa_id) ordenados por id desc
    @Query("""
        SELECT c FROM Contrato c
        JOIN c.cliente cl
        WHERE cl.empresa.id = :empresaId
        ORDER BY c.id DESC
    """)
    List<Contrato> findByEmpresaId(@Param("empresaId") Long empresaId);

    // Contratos por vencer: fecha_fin entre hoy y una fecha futura
    @Query("""
        SELECT c FROM Contrato c
        JOIN c.cliente cl
        WHERE cl.empresa.id = :empresaId
          AND c.fechaFin >= :desde
          AND c.fechaFin <= :hasta
        ORDER BY c.fechaFin ASC
    """)
    List<Contrato> findPorVencer(@Param("empresaId") Long empresaId,
                                  @Param("desde") LocalDate desde,
                                  @Param("hasta") LocalDate hasta);

    // Contratos vencidos: fecha_fin ya pasó
    @Query("""
        SELECT c FROM Contrato c
        JOIN c.cliente cl
        WHERE cl.empresa.id = :empresaId
          AND c.fechaFin < :hoy
        ORDER BY c.fechaFin DESC
    """)
    List<Contrato> findVencidos(@Param("empresaId") Long empresaId,
                                 @Param("hoy") LocalDate hoy);

    // Contar contratos que vencen hasta una fecha (para dashboard)
    @Query("""
        SELECT COUNT(c) FROM Contrato c
        JOIN c.cliente cl
        WHERE cl.empresa.id = :empresaId
          AND c.fechaFin >= :desde
          AND c.fechaFin <= :hasta
    """)
    long countPorVencer(@Param("empresaId") Long empresaId,
                         @Param("desde") LocalDate desde,
                         @Param("hasta") LocalDate hasta);

    // Obtener contrato validando que pertenezca a la empresa
    @Query("""
        SELECT c FROM Contrato c
        JOIN c.cliente cl
        WHERE c.id = :contratoId
          AND cl.empresa.id = :empresaId
    """)
    Optional<Contrato> findByIdAndEmpresaId(@Param("contratoId") Long contratoId,
                                             @Param("empresaId") Long empresaId);

    List<Contrato> findByClienteId(Long clienteId);
}
