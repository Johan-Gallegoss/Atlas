package atlas.backend.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "auditoria")
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    // Almacena el nombre de usuario como texto, sin FK a la tabla usuarios
    @Column(name = "usuario_id", nullable = false)
    private String usuarioId;

    @Column(name = "accion", nullable = false)
    private String accion;

    @Column(name = "fecha_hora", columnDefinition = "timestamp with time zone")
    private OffsetDateTime fechaHora;

    public Auditoria() {}

    public Auditoria(String usuarioId, String accion) {
        this.usuarioId = usuarioId;
        this.accion = accion;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }

    public String getAccion() { return accion; }
    public void setAccion(String accion) { this.accion = accion; }

    public OffsetDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(OffsetDateTime fechaHora) { this.fechaHora = fechaHora; }
}
