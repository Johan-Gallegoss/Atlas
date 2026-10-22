package atlas.backend.service;

import atlas.backend.dto.BindingDTO;
import atlas.backend.dto.ColaDTO;
import atlas.backend.dto.ExchangeDTO;
import atlas.backend.dto.QueueInfoDTO;
import atlas.backend.service.impl.RabbitAdminServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RabbitAdminServiceTest {

    @Mock
    private AmqpAdmin amqpAdmin;

    private RabbitAdminService rabbitAdminService;

    @BeforeEach
    void setUp() {
        rabbitAdminService = new RabbitAdminServiceImpl(amqpAdmin);
    }

    @Test
    @DisplayName("Debe crear una cola correctamente con sus propiedades")
    void crearCola_Exito() {
        ColaDTO dto = new ColaDTO("temporal.queue", true, false);
        dto.setDeadLetterExchange("atlas.dlx");
        dto.setDeadLetterRoutingKey("temporal.dlq");

        when(amqpAdmin.declareQueue(any(Queue.class))).thenReturn("temporal.queue");

        String nombre = rabbitAdminService.crearCola(dto);

        assertEquals("temporal.queue", nombre);
        ArgumentCaptor<Queue> captor = ArgumentCaptor.forClass(Queue.class);
        verify(amqpAdmin).declareQueue(captor.capture());
        Queue q = captor.getValue();
        assertEquals("temporal.queue", q.getName());
        assertTrue(q.isDurable());
        assertEquals("atlas.dlx", q.getArguments().get("x-dead-letter-exchange"));
        assertEquals("temporal.dlq", q.getArguments().get("x-dead-letter-routing-key"));
    }

    @Test
    @DisplayName("Debe bloquear la eliminación de una cola crítica del sistema")
    void eliminarCola_Critica_Bloqueada() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                rabbitAdminService.eliminarCola("contrato.creado.queue"));
        assertTrue(ex.getMessage().contains("No se permite eliminar la cola crítica"));

        assertThrows(IllegalArgumentException.class, () ->
                rabbitAdminService.eliminarCola("contrato.creado.dlq"));

        verify(amqpAdmin, never()).deleteQueue(anyString());
    }

    @Test
    @DisplayName("Debe eliminar una cola no crítica exitosamente")
    void eliminarCola_NoCritica_Exito() {
        when(amqpAdmin.deleteQueue("temporal.queue")).thenReturn(true);

        boolean res = rabbitAdminService.eliminarCola("temporal.queue");

        assertTrue(res);
        verify(amqpAdmin).deleteQueue("temporal.queue");
    }

    @Test
    @DisplayName("Debe crear un exchange válido exitosamente")
    void crearExchange_Exito() {
        ExchangeDTO dto = new ExchangeDTO("notificaciones.direct", "direct", true, false);

        String nombre = rabbitAdminService.crearExchange(dto);

        assertEquals("notificaciones.direct", nombre);
        ArgumentCaptor<Exchange> captor = ArgumentCaptor.forClass(Exchange.class);
        verify(amqpAdmin).declareExchange(captor.capture());
        Exchange ex = captor.getValue();
        assertEquals("notificaciones.direct", ex.getName());
        assertEquals("direct", ex.getType());
        assertTrue(ex.isDurable());
    }

    @Test
    @DisplayName("Debe rechazar la creación de un exchange con tipo inválido")
    void crearExchange_TipoInvalido() {
        ExchangeDTO dto = new ExchangeDTO("invalido.exchange", "desconocido", true, false);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                rabbitAdminService.crearExchange(dto));

        assertTrue(ex.getMessage().contains("Tipo de exchange no soportado"));
        verify(amqpAdmin, never()).declareExchange(any());
    }

    @Test
    @DisplayName("Debe bloquear la eliminación de un exchange crítico del sistema")
    void eliminarExchange_Critico_Bloqueado() {
        assertThrows(IllegalArgumentException.class, () ->
                rabbitAdminService.eliminarExchange("atlas.eventos"));

        assertThrows(IllegalArgumentException.class, () ->
                rabbitAdminService.eliminarExchange("atlas.dlx"));

        verify(amqpAdmin, never()).deleteExchange(anyString());
    }

    @Test
    @DisplayName("Debe crear y eliminar bindings correctamente")
    void binding_Operaciones() {
        BindingDTO dto = new BindingDTO("temporal.queue", "temporal.direct", "temp.key");

        rabbitAdminService.crearBinding(dto);
        verify(amqpAdmin).declareBinding(any(Binding.class));

        rabbitAdminService.eliminarBinding(dto);
        verify(amqpAdmin).removeBinding(any(Binding.class));
    }

    @Test
    @DisplayName("Debe bloquear la eliminación de bindings de infraestructura crítica")
    void bindingCritico_Bloqueado() {
        BindingDTO dto = new BindingDTO("contrato.creado.queue", "atlas.eventos", "contrato.creado");

        assertThrows(IllegalArgumentException.class, () ->
                rabbitAdminService.eliminarBinding(dto));

        verify(amqpAdmin, never()).removeBinding(any(Binding.class));
    }

    @Test
    @DisplayName("Debe obtener información de una cola existente")
    void obtenerInfoCola_Existe() {
        QueueInformation qInfo = new QueueInformation("temporal.queue", 5L, 2);
        when(amqpAdmin.getQueueInfo("temporal.queue")).thenReturn(qInfo);

        QueueInfoDTO info = rabbitAdminService.obtenerInfoCola("temporal.queue");

        assertNotNull(info);
        assertEquals("temporal.queue", info.getNombre());
        assertEquals(5L, info.getMensajes());
        assertEquals(2, info.getConsumidores());
    }

    @Test
    @DisplayName("Debe retornar null cuando la cola no existe")
    void obtenerInfoCola_NoExiste() {
        when(amqpAdmin.getQueueInfo("no.existe.queue")).thenReturn(null);

        QueueInfoDTO info = rabbitAdminService.obtenerInfoCola("no.existe.queue");

        assertNull(info);
    }
}