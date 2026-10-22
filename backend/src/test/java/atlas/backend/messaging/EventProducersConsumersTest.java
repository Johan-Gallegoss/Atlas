package atlas.backend.messaging;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventProducersConsumersTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    private ContratoEventProducer contratoProducer;
    private AuditoriaEventProducer auditoriaProducer;
    private ContratoEventConsumer contratoConsumer;
    private AuditoriaEventConsumer auditoriaConsumer;

    @BeforeEach
    void setUp() {
        contratoProducer = new ContratoEventProducer(rabbitTemplate);
        ReflectionTestUtils.setField(contratoProducer, "exchange", "atlas.eventos");
        ReflectionTestUtils.setField(contratoProducer, "contratoCreadoRoutingKey", "contrato.creado");
        ReflectionTestUtils.setField(contratoProducer, "contratoActualizadoRoutingKey", "contrato.actualizado");

        auditoriaProducer = new AuditoriaEventProducer(rabbitTemplate);
        ReflectionTestUtils.setField(auditoriaProducer, "exchange", "atlas.eventos");
        ReflectionTestUtils.setField(auditoriaProducer, "auditoriaRoutingKey", "auditoria.registro");

        contratoConsumer = new ContratoEventConsumer();
        auditoriaConsumer = new AuditoriaEventConsumer();
    }

    @Test
    @DisplayName("ContratoEventProducer debe publicar contrato.creado")
    void publicarContratoCreado_Exito() {
        contratoProducer.publicarContratoCreado(10L, "Contrato Test 10");

        ArgumentCaptor<ContratoCreadoEvent> captor = ArgumentCaptor.forClass(ContratoCreadoEvent.class);
        verify(rabbitTemplate).convertAndSend(eq("atlas.eventos"), eq("contrato.creado"), captor.capture());

        ContratoCreadoEvent event = captor.getValue();
        assertEquals(10L, event.getContratoId());
        assertEquals("Contrato Test 10", event.getTitulo());
        assertEquals("contrato.creado", event.getTipoEvento());
        assertNotNull(event.getOcurridoEn());
    }

    @Test
    @DisplayName("ContratoEventProducer debe publicar contrato.actualizado")
    void publicarContratoActualizado_Exito() {
        contratoProducer.publicarContratoActualizado(10L, "Contrato Actualizado 10");

        ArgumentCaptor<ContratoActualizadoEvent> captor = ArgumentCaptor.forClass(ContratoActualizadoEvent.class);
        verify(rabbitTemplate).convertAndSend(eq("atlas.eventos"), eq("contrato.actualizado"), captor.capture());

        ContratoActualizadoEvent event = captor.getValue();
        assertEquals(10L, event.getContratoId());
        assertEquals("Contrato Actualizado 10", event.getTitulo());
        assertEquals("contrato.actualizado", event.getTipoEvento());
        assertNotNull(event.getOcurridoEn());
    }

    @Test
    @DisplayName("AuditoriaEventProducer debe publicar auditoria.registro")
    void publicarAuditoriaRegistro_Exito() {
        auditoriaProducer.publicarAuditoriaRegistro(50L, "admin", "CREAR_USUARIO");

        ArgumentCaptor<AuditoriaRegistroEvent> captor = ArgumentCaptor.forClass(AuditoriaRegistroEvent.class);
        verify(rabbitTemplate).convertAndSend(eq("atlas.eventos"), eq("auditoria.registro"), captor.capture());

        AuditoriaRegistroEvent event = captor.getValue();
        assertEquals(50L, event.getAuditoriaId());
        assertEquals("admin", event.getUsuarioId());
        assertEquals("CREAR_USUARIO", event.getAccion());
        assertEquals("auditoria.registro", event.getTipoEvento());
        assertNotNull(event.getOcurridoEn());
    }

    @Test
    @DisplayName("Productor no debe lanzar excepción si RabbitTemplate falla")
    void productor_ToleranciaFallos() {
        doThrow(new RuntimeException("Error simulado RabbitMQ"))
                .when(rabbitTemplate).convertAndSend(anyString(), anyString(), any(Object.class));

        assertDoesNotThrow(() -> contratoProducer.publicarContratoCreado(1L, "Test"));
        assertDoesNotThrow(() -> contratoProducer.publicarContratoActualizado(1L, "Test"));
        assertDoesNotThrow(() -> auditoriaProducer.publicarAuditoriaRegistro(1L, "admin", "LOGIN"));
    }

    @Test
    @DisplayName("Consumidores deben procesar eventos válidos sin excepciones")
    void consumidores_ProcesamientoExitoso() {
        ContratoCreadoEvent cCreado = new ContratoCreadoEvent(1L, "Titulo");
        ContratoActualizadoEvent cActualizado = new ContratoActualizadoEvent(1L, "Titulo Modificado");
        AuditoriaRegistroEvent aReg = new AuditoriaRegistroEvent(1L, "admin", "UPDATE");

        assertDoesNotThrow(() -> contratoConsumer.onContratoCreado(cCreado));
        assertDoesNotThrow(() -> contratoConsumer.onContratoActualizado(cActualizado));
        assertDoesNotThrow(() -> auditoriaConsumer.onAuditoriaRegistro(aReg));
    }
}