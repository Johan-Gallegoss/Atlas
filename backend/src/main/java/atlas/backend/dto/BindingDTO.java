package atlas.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class BindingDTO {

    @NotBlank(message = "El nombre de la cola es obligatorio")
    private String cola;

    @NotBlank(message = "El nombre del exchange es obligatorio")
    private String exchange;

    private String routingKey = "";

    public BindingDTO() {}

    public BindingDTO(String cola, String exchange, String routingKey) {
        this.cola = cola;
        this.exchange = exchange;
        this.routingKey = routingKey != null ? routingKey : "";
    }

    public String getCola() { return cola; }
    public void setCola(String cola) { this.cola = cola; }

    public String getExchange() { return exchange; }
    public void setExchange(String exchange) { this.exchange = exchange; }

    public String getRoutingKey() { return routingKey != null ? routingKey : ""; }
    public void setRoutingKey(String routingKey) { this.routingKey = routingKey; }
}