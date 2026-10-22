package atlas.backend.dto;

public class QueueInfoDTO {

    private String nombre;
    private long mensajes;
    private int consumidores;
    private String tipo;

    public QueueInfoDTO() {}

    public QueueInfoDTO(String nombre, long mensajes, int consumidores, String tipo) {
        this.nombre = nombre;
        this.mensajes = mensajes;
        this.consumidores = consumidores;
        this.tipo = tipo;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public long getMensajes() { return mensajes; }
    public void setMensajes(long mensajes) { this.mensajes = mensajes; }

    public int getConsumidores() { return consumidores; }
    public void setConsumidores(int consumidores) { this.consumidores = consumidores; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}