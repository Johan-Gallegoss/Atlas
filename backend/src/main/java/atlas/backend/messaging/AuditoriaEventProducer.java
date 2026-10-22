package atlas.backend.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Productor de eventos de dominio para Auditoria en RabbitMQ.
 * Publica el evento "auditoria.registro" al exchange central de Atlas.
 */
@Component
public class AuditoriaEventProducer {

    private static final Logger log = LoggerFactory.getLogger(AuditoriaEventProducer.class);

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.rabbitmq.exchange}")
    private String exchange;

    @Value("${app.rabbitmq.routing-key.auditoria-registro}")
    private String auditoriaRoutingKey;

    public AuditoriaEventProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publicarAuditoriaRegistro(Long auditoriaId, String usuarioId, String accion) {
        AuditoriaRegistroEvent evento = new AuditoriaRegistroEvent(auditoriaId, usuarioId, accion);
        try {
            rabbitTemplate.convertAndSend(exchange, auditoriaRoutingKey, evento);
            log.info("[RabbitMQ] Evento publicado: exchange={}, routingKey={}, auditoriaId={}, accion={}",
                    exchange, auditoriaRoutingKey, auditoriaId, accion);
        } catch (Exception ex) {
            log.error("[RabbitMQ] Error publicando auditoria.registro para auditoriaId={}: {}",
                    auditoriaId, ex.getMessage(), ex);
        }
    }
}