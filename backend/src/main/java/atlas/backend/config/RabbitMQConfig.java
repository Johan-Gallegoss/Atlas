package atlas.backend.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuracion de RabbitMQ para Atlas.
 * Topologia:
 * - TopicExchange: "atlas.eventos"
 * - DirectExchange (DLX): "atlas.dlx"
 * - Colas principales (durable + DLX + DLK):
 *     1. "contrato.creado.queue" -> DLQ: "contrato.creado.dlq"
 *     2. "contrato.actualizado.queue" -> DLQ: "contrato.actualizado.dlq"
 *     3. "auditoria.registro.queue" -> DLQ: "auditoria.registro.dlq"
 * - Colas DLQ duraderas asociadas a "atlas.dlx".
 * - SimpleRabbitListenerContainerFactory con defaultRequeueRejected=false y serializador JSON.
 */
@Configuration
public class RabbitMQConfig {

    // Exchanges
    @Value("${app.rabbitmq.exchange}")
    private String exchangeName;

    @Value("${app.rabbitmq.dlx-exchange}")
    private String dlxExchangeName;

    // Colas Principales
    @Value("${app.rabbitmq.queue.contrato-creado}")
    private String contratoCreadoQueueName;

    @Value("${app.rabbitmq.routing-key.contrato-creado}")
    private String contratoCreadoRoutingKey;

    @Value("${app.rabbitmq.queue.contrato-actualizado}")
    private String contratoActualizadoQueueName;

    @Value("${app.rabbitmq.routing-key.contrato-actualizado}")
    private String contratoActualizadoRoutingKey;

    @Value("${app.rabbitmq.queue.auditoria-registro}")
    private String auditoriaRegistroQueueName;

    @Value("${app.rabbitmq.routing-key.auditoria-registro}")
    private String auditoriaRegistroRoutingKey;

    // Colas DLQ
    @Value("${app.rabbitmq.dlq.contrato-creado}")
    private String contratoCreadoDlqName;

    @Value("${app.rabbitmq.dlq.contrato-actualizado}")
    private String contratoActualizadoDlqName;

    @Value("${app.rabbitmq.dlq.auditoria-registro}")
    private String auditoriaRegistroDlqName;

    // ==========================================
    // EXCHANGES
    // ==========================================

    @Bean
    public TopicExchange atlasEventosExchange() {
        return ExchangeBuilder
                .topicExchange(exchangeName)
                .durable(true)
                .build();
    }

    @Bean
    public DirectExchange atlasDlxExchange() {
        return ExchangeBuilder
                .directExchange(dlxExchangeName)
                .durable(true)
                .build();
    }

    // ==========================================
    // COLAS PRINCIPALES (CON DLQ CONFIGURADA)
    // ==========================================

    @Bean
    public Queue contratoCreadoQueue() {
        return QueueBuilder
                .durable(contratoCreadoQueueName)
                .deadLetterExchange(dlxExchangeName)
                .deadLetterRoutingKey(contratoCreadoDlqName)
                .build();
    }

    @Bean
    public Queue contratoActualizadoQueue() {
        return QueueBuilder
                .durable(contratoActualizadoQueueName)
                .deadLetterExchange(dlxExchangeName)
                .deadLetterRoutingKey(contratoActualizadoDlqName)
                .build();
    }

    @Bean
    public Queue auditoriaRegistroQueue() {
        return QueueBuilder
                .durable(auditoriaRegistroQueueName)
                .deadLetterExchange(dlxExchangeName)
                .deadLetterRoutingKey(auditoriaRegistroDlqName)
                .build();
    }

    // ==========================================
    // COLAS DLQ (DEAD LETTER QUEUES)
    // ==========================================

    @Bean
    public Queue contratoCreadoDlq() {
        return QueueBuilder
                .durable(contratoCreadoDlqName)
                .build();
    }

    @Bean
    public Queue contratoActualizadoDlq() {
        return QueueBuilder
                .durable(contratoActualizadoDlqName)
                .build();
    }

    @Bean
    public Queue auditoriaRegistroDlq() {
        return QueueBuilder
                .durable(auditoriaRegistroDlqName)
                .build();
    }

    // ==========================================
    // BINDINGS: COLAS PRINCIPALES -> TOPIC EXCHANGE
    // ==========================================

    @Bean
    public Binding contratoCreadoBinding(
            @Qualifier("contratoCreadoQueue") Queue contratoCreadoQueue,
            @Qualifier("atlasEventosExchange") TopicExchange atlasEventosExchange) {
        return BindingBuilder
                .bind(contratoCreadoQueue)
                .to(atlasEventosExchange)
                .with(contratoCreadoRoutingKey);
    }

    @Bean
    public Binding contratoActualizadoBinding(
            @Qualifier("contratoActualizadoQueue") Queue contratoActualizadoQueue,
            @Qualifier("atlasEventosExchange") TopicExchange atlasEventosExchange) {
        return BindingBuilder
                .bind(contratoActualizadoQueue)
                .to(atlasEventosExchange)
                .with(contratoActualizadoRoutingKey);
    }

    @Bean
    public Binding auditoriaRegistroBinding(
            @Qualifier("auditoriaRegistroQueue") Queue auditoriaRegistroQueue,
            @Qualifier("atlasEventosExchange") TopicExchange atlasEventosExchange) {
        return BindingBuilder
                .bind(auditoriaRegistroQueue)
                .to(atlasEventosExchange)
                .with(auditoriaRegistroRoutingKey);
    }

    // ==========================================
    // BINDINGS: COLAS DLQ -> DIRECT EXCHANGE (DLX)
    // ==========================================

    @Bean
    public Binding contratoCreadoDlqBinding(
            @Qualifier("contratoCreadoDlq") Queue contratoCreadoDlq,
            @Qualifier("atlasDlxExchange") DirectExchange atlasDlxExchange) {
        return BindingBuilder
                .bind(contratoCreadoDlq)
                .to(atlasDlxExchange)
                .with(contratoCreadoDlqName);
    }

    @Bean
    public Binding contratoActualizadoDlqBinding(
            @Qualifier("contratoActualizadoDlq") Queue contratoActualizadoDlq,
            @Qualifier("atlasDlxExchange") DirectExchange atlasDlxExchange) {
        return BindingBuilder
                .bind(contratoActualizadoDlq)
                .to(atlasDlxExchange)
                .with(contratoActualizadoDlqName);
    }

    @Bean
    public Binding auditoriaRegistroDlqBinding(
            @Qualifier("auditoriaRegistroDlq") Queue auditoriaRegistroDlq,
            @Qualifier("atlasDlxExchange") DirectExchange atlasDlxExchange) {
        return BindingBuilder
                .bind(auditoriaRegistroDlq)
                .to(atlasDlxExchange)
                .with(auditoriaRegistroDlqName);
    }

    // ==========================================
    // SERIALIZACION Y TEMPLATE
    // ==========================================

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    // ==========================================
    // LISTENER CONTAINER FACTORY (RECHAZO DEFINITIVO A DLQ)
    // ==========================================

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter jsonMessageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter);
        factory.setDefaultRequeueRejected(false);
        return factory;
    }
}