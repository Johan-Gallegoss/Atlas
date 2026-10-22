package atlas.backend.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Consumidor de eventos de dominio para Contratos.
 * Escucha "contrato.creado" y "contrato.actualizado".
 */
@Component
public class ContratoEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(ContratoEventConsumer.class);

    @RabbitListener(queues = "${app.rabbitmq.queue.contrato-creado}")
    public void onContratoCreado(ContratoCreadoEvent evento) {
        log.info("[RabbitMQ] Evento recibido: tipo={}, contratoId={}, titulo={}, ocurridoEn={}",
                 evento.getTipoEvento(),
                 evento.getContratoId(),
                 evento.getTitulo(),
                 evento.getOcurridoEn());
    }

    @RabbitListener(queues = "${app.rabbitmq.queue.contrato-actualizado}")
    public void onContratoActualizado(ContratoActualizadoEvent evento) {
        log.info("[RabbitMQ] Evento recibido: tipo={}, contratoId={}, titulo={}, ocurridoEn={}",
                 evento.getTipoEvento(),
                 evento.getContratoId(),
                 evento.getTitulo(),
                 evento.getOcurridoEn());
    }
}