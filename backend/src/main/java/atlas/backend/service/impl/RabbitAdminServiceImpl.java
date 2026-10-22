package atlas.backend.service.impl;

import atlas.backend.dto.BindingDTO;
import atlas.backend.dto.ColaDTO;
import atlas.backend.dto.ExchangeDTO;
import atlas.backend.dto.QueueInfoDTO;
import atlas.backend.service.RabbitAdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Set;

@Service
public class RabbitAdminServiceImpl implements RabbitAdminService {

    private static final Logger log = LoggerFactory.getLogger(RabbitAdminServiceImpl.class);

    private final AmqpAdmin amqpAdmin;

    // Recursos criticos protegidos para impedir su eliminacion
    private static final Set<String> PROTECTED_QUEUES = Set.of(
            "contrato.creado.queue",
            "contrato.actualizado.queue",
            "auditoria.registro.queue",
            "contrato.creado.dlq",
            "contrato.actualizado.dlq",
            "auditoria.registro.dlq"
    );

    private static final Set<String> PROTECTED_EXCHANGES = Set.of(
            "atlas.eventos",
            "atlas.dlx"
    );

    public RabbitAdminServiceImpl(AmqpAdmin amqpAdmin) {
        this.amqpAdmin = amqpAdmin;
    }

    @Override
    public String crearCola(ColaDTO colaDTO) {
        validarNombre(colaDTO.getNombre(), "cola");

        QueueBuilder builder = Boolean.TRUE.equals(colaDTO.getDurable())
                ? QueueBuilder.durable(colaDTO.getNombre().trim())
                : QueueBuilder.nonDurable(colaDTO.getNombre().trim());

        if (Boolean.TRUE.equals(colaDTO.getAutoDelete())) {
            builder.autoDelete();
        }

        if (colaDTO.getDeadLetterExchange() != null && !colaDTO.getDeadLetterExchange().isBlank()) {
            builder.deadLetterExchange(colaDTO.getDeadLetterExchange().trim());
        }

        if (colaDTO.getDeadLetterRoutingKey() != null && !colaDTO.getDeadLetterRoutingKey().isBlank()) {
            builder.deadLetterRoutingKey(colaDTO.getDeadLetterRoutingKey().trim());
        }

        Queue queue = builder.build();
        String declaredName = amqpAdmin.declareQueue(queue);
        log.info("[RabbitAdmin] Cola creada exitosamente: {}", declaredName);
        return declaredName;
    }

    @Override
    public boolean eliminarCola(String nombre) {
        validarNombre(nombre, "cola");
        String cleanName = nombre.trim();

        if (esColaProtegida(cleanName)) {
            throw new IllegalArgumentException("No se permite eliminar la cola crítica del sistema: " + cleanName);
        }

        boolean deleted = amqpAdmin.deleteQueue(cleanName);
        log.info("[RabbitAdmin] Eliminacion de cola '{}' resultado: {}", cleanName, deleted);
        return deleted;
    }

    @Override
    public String crearExchange(ExchangeDTO exchangeDTO) {
        validarNombre(exchangeDTO.getNombre(), "exchange");
        if (exchangeDTO.getTipo() == null || exchangeDTO.getTipo().isBlank()) {
            throw new IllegalArgumentException("El tipo de exchange es obligatorio");
        }

        String tipo = exchangeDTO.getTipo().trim().toLowerCase(Locale.ROOT);
        String nombre = exchangeDTO.getNombre().trim();
        boolean durable = Boolean.TRUE.equals(exchangeDTO.getDurable());
        boolean autoDelete = Boolean.TRUE.equals(exchangeDTO.getAutoDelete());

        Exchange exchange;
        switch (tipo) {
            case "direct":
                exchange = new DirectExchange(nombre, durable, autoDelete);
                break;
            case "topic":
                exchange = new TopicExchange(nombre, durable, autoDelete);
                break;
            case "fanout":
                exchange = new FanoutExchange(nombre, durable, autoDelete);
                break;
            case "headers":
                exchange = new HeadersExchange(nombre, durable, autoDelete);
                break;
            default:
                throw new IllegalArgumentException("Tipo de exchange no soportado: '" + tipo +
                        "'. Tipos válidos: direct, topic, fanout, headers");
        }

        amqpAdmin.declareExchange(exchange);
        log.info("[RabbitAdmin] Exchange '{}' de tipo '{}' declarado exitosamente", nombre, tipo);
        return nombre;
    }

    @Override
    public boolean eliminarExchange(String nombre) {
        validarNombre(nombre, "exchange");
        String cleanName = nombre.trim();

        if (esExchangeProtegido(cleanName)) {
            throw new IllegalArgumentException("No se permite eliminar el exchange crítico del sistema: " + cleanName);
        }

        boolean deleted = amqpAdmin.deleteExchange(cleanName);
        log.info("[RabbitAdmin] Eliminacion de exchange '{}' resultado: {}", cleanName, deleted);
        return deleted;
    }

    @Override
    public void crearBinding(BindingDTO bindingDTO) {
        validarNombre(bindingDTO.getCola(), "cola");
        validarNombre(bindingDTO.getExchange(), "exchange");

        String cola = bindingDTO.getCola().trim();
        String exchange = bindingDTO.getExchange().trim();
        String routingKey = bindingDTO.getRoutingKey() != null ? bindingDTO.getRoutingKey().trim() : "";

        Binding binding = new Binding(cola, Binding.DestinationType.QUEUE, exchange, routingKey, null);
        amqpAdmin.declareBinding(binding);
        log.info("[RabbitAdmin] Binding creado: cola='{}' -> exchange='{}' con routingKey='{}'", cola, exchange, routingKey);
    }

    @Override
    public boolean eliminarBinding(BindingDTO bindingDTO) {
        validarNombre(bindingDTO.getCola(), "cola");
        validarNombre(bindingDTO.getExchange(), "exchange");

        String cola = bindingDTO.getCola().trim();
        String exchange = bindingDTO.getExchange().trim();
        String routingKey = bindingDTO.getRoutingKey() != null ? bindingDTO.getRoutingKey().trim() : "";

        if (esColaProtegida(cola) && esExchangeProtegido(exchange)) {
            throw new IllegalArgumentException("No se permite eliminar un binding de la infraestructura crítica del sistema");
        }

        Binding binding = new Binding(cola, Binding.DestinationType.QUEUE, exchange, routingKey, null);
        amqpAdmin.removeBinding(binding);
        log.info("[RabbitAdmin] Binding eliminado: cola='{}' -> exchange='{}' con routingKey='{}'", cola, exchange, routingKey);
        return true;
    }

    @Override
    public QueueInfoDTO obtenerInfoCola(String nombre) {
        validarNombre(nombre, "cola");
        String cleanName = nombre.trim();

        QueueInformation info = amqpAdmin.getQueueInfo(cleanName);
        if (info == null) {
            return null;
        }

        return new QueueInfoDTO(info.getName(), info.getMessageCount(), info.getConsumerCount(), info.getType());
    }

    private void validarNombre(String nombre, String tipoRecurso) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del " + tipoRecurso + " no puede ser nulo o vacío");
        }
    }

    private boolean esColaProtegida(String nombre) {
        return PROTECTED_QUEUES.contains(nombre);
    }

    private boolean esExchangeProtegido(String nombre) {
        return PROTECTED_EXCHANGES.contains(nombre);
    }
}