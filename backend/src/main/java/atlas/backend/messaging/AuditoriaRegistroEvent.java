package atlas.backend.messaging;

import java.time.OffsetDateTime;

/**
 * DTO del evento "auditoria.registro".
 */
public class AuditoriaRegistroEvent {

    private Long auditoriaId;
    private String usuarioId;
    private String accion;
    private String tipoEvento;
    private OffsetDateTime ocurridoEn;

    public AuditoriaRegistroEvent() {}

    public AuditoriaRegistroEvent(Long auditoriaId, String usuarioId, String accion) {
        this.auditoriaId = auditoriaId;
        this.usuarioId = usuarioId;
        this.accion = accion;
        this.tipoEvento = "auditoria.registro";
        this.ocurridoEn = OffsetDateTime.now();
    }

    public Long getAuditoriaId() { return auditoriaId; }
    public void setAuditoriaId(Long auditoriaId) { this.auditoriaId = auditoriaId; }

    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }

    public String getAccion() { return accion; }
    public void setAccion(String accion) { this.accion = accion; }

    public String getTipoEvento() { return tipoEvento; }
    public void setTipoEvento(String tipoEvento) { this.tipoEvento = tipoEvento; }

    public OffsetDateTime getOcurridoEn() { return ocurridoEn; }
    public void setOcurridoEn(OffsetDateTime ocurridoEn) { this.ocurridoEn = ocurridoEn; }

    @Override
    public String toString() {
        return "AuditoriaRegistroEvent{auditoriaId=" + auditoriaId +
                ", usuarioId='" + usuarioId + "', accion='" + accion +
                "', tipoEvento='" + tipoEvento + "', ocurridoEn=" + ocurridoEn + "}";
    }
}