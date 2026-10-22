package atlas.backend.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Productor de eventos de dominio para Contratos en RabbitMQ.
 * Publica eventos "contrato.creado" y "contrato.actualizado" al exchange central de Atlas.
 */
@Component
public class ContratoEventProducer {

    private static final Logger log = LoggerFactory.getLogger(ContratoEventProducer.class);

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.rabbitmq.exchange}")
    private String exchange;

    @Value("${app.rabbitmq.routing-key.contrato-creado}")
    private String contratoCreadoRoutingKey;

    @Value("${app.rabbitmq.routing-key.contrato-actualizado}")
    private String contratoActualizadoRoutingKey;

    public ContratoEventProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publicarContratoCreado(Long contratoId, String titulo) {
        ContratoCreadoEvent evento = new ContratoCreadoEvent(contratoId, titulo);
        try {
            rabbitTemplate.convertAndSend(exchange, contratoCreadoRoutingKey, evento);
            log.info("[RabbitMQ] Evento publicado: exchange={}, routingKey={}, contratoId={}",
                    exchange, contratoCreadoRoutingKey, contratoId);
        } catch (Exception ex) {
            log.error("[RabbitMQ] Error publicando contrato.creado para contratoId={}: {}",
                    contratoId, ex.getMessage(), ex);
        }
    }

    public void publicarContratoActualizado(Long contratoId, String titulo) {
        ContratoActualizadoEvent evento = new ContratoActualizadoEvent(contratoId, titulo);
        try {
            rabbitTemplate.convertAndSend(exchange, contratoActualizadoRoutingKey, evento);
            log.info("[RabbitMQ] Evento publicado: exchange={}, routingKey={}, contratoId={}",
                    exchange, contratoActualizadoRoutingKey, contratoId);
        } catch (Exception ex) {
            log.error("[RabbitMQ] Error publicando contrato.actualizado para contratoId={}: {}",
                    contratoId, ex.getMessage(), ex);
        }
    }
}