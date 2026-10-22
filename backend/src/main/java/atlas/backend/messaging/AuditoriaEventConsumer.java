package atlas.backend.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Consumidor del evento "auditoria.registro".
 * Registra en logs la recepcion del evento de auditoria.
 */
@Component
public class AuditoriaEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(AuditoriaEventConsumer.class);

    @RabbitListener(queues = "${app.rabbitmq.queue.auditoria-registro}")
    public void onAuditoriaRegistro(AuditoriaRegistroEvent evento) {
        log.info("[RabbitMQ] Evento recibido: tipo={}, auditoriaId={}, usuarioId={}, accion={}, ocurridoEn={}",
                evento.getTipoEvento(),
                evento.getAuditoriaId(),
                evento.getUsuarioId(),
                evento.getAccion(),
                evento.getOcurridoEn());
    }
}