package atlas.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class ColaDTO {

    @NotBlank(message = "El nombre de la cola es obligatorio")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "El nombre de la cola solo puede contener letras, números, puntos, guiones y guiones bajos")
    private String nombre;

    private Boolean durable = true;

    private Boolean autoDelete = false;

    private String deadLetterExchange;

    private String deadLetterRoutingKey;

    public ColaDTO() {}

    public ColaDTO(String nombre, Boolean durable, Boolean autoDelete) {
        this.nombre = nombre;
        this.durable = durable != null ? durable : true;
        this.autoDelete = autoDelete != null ? autoDelete : false;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Boolean getDurable() { return durable != null ? durable : true; }
    public void setDurable(Boolean durable) { this.durable = durable; }

    public Boolean getAutoDelete() { return autoDelete != null ? autoDelete : false; }
    public void setAutoDelete(Boolean autoDelete) { this.autoDelete = autoDelete; }

    public String getDeadLetterExchange() { return deadLetterExchange; }
    public void setDeadLetterExchange(String deadLetterExchange) { this.deadLetterExchange = deadLetterExchange; }

    public String getDeadLetterRoutingKey() { return deadLetterRoutingKey; }
    public void setDeadLetterRoutingKey(String deadLetterRoutingKey) { this.deadLetterRoutingKey = deadLetterRoutingKey; }
}