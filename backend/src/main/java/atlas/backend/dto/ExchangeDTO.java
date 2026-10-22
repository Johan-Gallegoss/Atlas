package atlas.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class ExchangeDTO {

    @NotBlank(message = "El nombre del exchange es obligatorio")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "El nombre del exchange solo puede contener letras, números, puntos, guiones y guiones bajos")
    private String nombre;

    @NotBlank(message = "El tipo de exchange es obligatorio (direct, topic, fanout, headers)")
    private String tipo;

    private Boolean durable = true;

    private Boolean autoDelete = false;

    public ExchangeDTO() {}

    public ExchangeDTO(String nombre, String tipo, Boolean durable, Boolean autoDelete) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.durable = durable != null ? durable : true;
        this.autoDelete = autoDelete != null ? autoDelete : false;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public Boolean getDurable() { return durable != null ? durable : true; }
    public void setDurable(Boolean durable) { this.durable = durable; }

    public Boolean getAutoDelete() { return autoDelete != null ? autoDelete : false; }
    public void setAutoDelete(Boolean autoDelete) { this.autoDelete = autoDelete; }
}