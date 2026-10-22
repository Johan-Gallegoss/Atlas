package atlas.backend.messaging;

import java.time.OffsetDateTime;

/**
 * DTO del evento "contrato.creado".
 */
public class ContratoCreadoEvent {

    private Long contratoId;
    private String titulo;
    private String tipoEvento;
    private OffsetDateTime ocurridoEn;

    public ContratoCreadoEvent() {}

    public ContratoCreadoEvent(Long contratoId, String titulo) {
        this.contratoId = contratoId;
        this.titulo = titulo;
        this.tipoEvento = "contrato.creado";
        this.ocurridoEn = OffsetDateTime.now();
    }

    public Long getContratoId() { return contratoId; }
    public void setContratoId(Long contratoId) { this.contratoId = contratoId; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getTipoEvento() { return tipoEvento; }
    public void setTipoEvento(String tipoEvento) { this.tipoEvento = tipoEvento; }

    public OffsetDateTime getOcurridoEn() { return ocurridoEn; }
    public void setOcurridoEn(OffsetDateTime ocurridoEn) { this.ocurridoEn = ocurridoEn; }

    @Override
    public String toString() {
        return "ContratoCreadoEvent{contratoId=" + contratoId +
               ", titulo='" + titulo + "', tipoEvento='" + tipoEvento +
               "', ocurridoEn=" + ocurridoEn + "}";
    }
}

