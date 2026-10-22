package atlas.backend.controller;

import atlas.backend.dto.BindingDTO;
import atlas.backend.dto.ColaDTO;
import atlas.backend.dto.ExchangeDTO;
import atlas.backend.dto.QueueInfoDTO;
import atlas.backend.service.RabbitAdminService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controlador REST para la administracion dinamica de RabbitMQ.
 * Todas las rutas se encuentran protegidas por la configuracion de seguridad de Atlas.
 */
@RestController
@RequestMapping("/admin/rabbitmq")
public class RabbitAdminController {

    private final RabbitAdminService rabbitAdminService;

    public RabbitAdminController(RabbitAdminService rabbitAdminService) {
        this.rabbitAdminService = rabbitAdminService;
    }

    // ==========================================
    // COLAS
    // ==========================================

    @PostMapping("/colas")
    public ResponseEntity<?> crearCola(@Valid @RequestBody ColaDTO colaDTO) {
        String nombre = rabbitAdminService.crearCola(colaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "mensaje", "Cola creada exitosamente",
                "cola", nombre,
                "durable", colaDTO.getDurable(),
                "autoDelete", colaDTO.getAutoDelete()
        ));
    }

    @DeleteMapping("/colas/{nombre}")
    public ResponseEntity<?> eliminarCola(@PathVariable String nombre) {
        boolean eliminada = rabbitAdminService.eliminarCola(nombre);
        if (!eliminada) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "error", "La cola no existe o no pudo ser eliminada: " + nombre
            ));
        }
        return ResponseEntity.ok(Map.of(
                "mensaje", "Cola eliminada exitosamente",
                "cola", nombre
        ));
    }

    @GetMapping("/colas/{nombre}")
    public ResponseEntity<?> obtenerInfoCola(@PathVariable String nombre) {
        QueueInfoDTO info = rabbitAdminService.obtenerInfoCola(nombre);
        if (info == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "error", "Cola no encontrada en el broker: " + nombre
            ));
        }
        return ResponseEntity.ok(info);
    }

    // ==========================================
    // EXCHANGES
    // ==========================================

    @PostMapping("/exchanges")
    public ResponseEntity<?> crearExchange(@Valid @RequestBody ExchangeDTO exchangeDTO) {
        String nombre = rabbitAdminService.crearExchange(exchangeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "mensaje", "Exchange creado exitosamente",
                "exchange", nombre,
                "tipo", exchangeDTO.getTipo().toLowerCase(),
                "durable", exchangeDTO.getDurable(),
                "autoDelete", exchangeDTO.getAutoDelete()
        ));
    }

    @DeleteMapping("/exchanges/{nombre}")
    public ResponseEntity<?> eliminarExchange(@PathVariable String nombre) {
        boolean eliminado = rabbitAdminService.eliminarExchange(nombre);
        if (!eliminado) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "error", "El exchange no existe o no pudo ser eliminado: " + nombre
            ));
        }
        return ResponseEntity.ok(Map.of(
                "mensaje", "Exchange eliminado exitosamente",
                "exchange", nombre
        ));
    }

    // ==========================================
    // BINDINGS
    // ==========================================

    @PostMapping("/bindings")
    public ResponseEntity<?> crearBinding(@Valid @RequestBody BindingDTO bindingDTO) {
        rabbitAdminService.crearBinding(bindingDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "mensaje", "Binding creado exitosamente",
                "cola", bindingDTO.getCola(),
                "exchange", bindingDTO.getExchange(),
                "routingKey", bindingDTO.getRoutingKey()
        ));
    }

    @DeleteMapping("/bindings")
    public ResponseEntity<?> eliminarBinding(@Valid @RequestBody BindingDTO bindingDTO) {
        boolean eliminado = rabbitAdminService.eliminarBinding(bindingDTO);
        return ResponseEntity.ok(Map.of(
                "mensaje", "Binding eliminado exitosamente",
                "cola", bindingDTO.getCola(),
                "exchange", bindingDTO.getExchange(),
                "routingKey", bindingDTO.getRoutingKey()
        ));
    }

    // ==========================================
    // MANEJO DE EXCEPCIONES LOCAL
    // ==========================================

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "error", ex.getMessage()
        ));
    }
}