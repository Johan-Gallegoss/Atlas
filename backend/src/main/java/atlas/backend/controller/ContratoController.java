package atlas.backend.controller;

import atlas.backend.exception.ResourceNotFoundException;
import atlas.backend.model.Contrato;
import atlas.backend.repository.ContratoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/contratos")
public class ContratoController {

    private final ContratoRepository contratoRepository;

    public ContratoController(ContratoRepository contratoRepository) {
        this.contratoRepository = contratoRepository;
    }

    @GetMapping
    public ResponseEntity<List<Contrato>> listarContratos(@RequestParam(required = false) Long empresaId) {
        if (empresaId != null) {
            return ResponseEntity.ok(contratoRepository.findByEmpresaId(empresaId));
        }
        return ResponseEntity.ok(contratoRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contrato> obtenerContrato(@PathVariable Long id, @RequestParam(required = false) Long empresaId) {
        if (empresaId != null) {
            return ResponseEntity.ok(contratoRepository.findByIdAndEmpresaId(id, empresaId)
                    .orElseThrow(() -> new ResourceNotFoundException("Contrato no encontrado con id " + id)));
        }
        return ResponseEntity.ok(contratoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contrato no encontrado con id " + id)));
    }

    @PostMapping
    public ResponseEntity<Contrato> crearContrato(@RequestBody Contrato contrato) {
        if (contrato.getCreatedAt() == null) {
            contrato.setCreatedAt(OffsetDateTime.now());
        }
        Contrato nuevoContrato = contratoRepository.save(contrato);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoContrato);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Contrato> actualizarContrato(@PathVariable Long id, @RequestBody Contrato contratoDetails) {
        Contrato existente = contratoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contrato no encontrado con id " + id));
        existente.setTitulo(contratoDetails.getTitulo());
        existente.setDescripcion(contratoDetails.getDescripcion());
        existente.setFechaInicio(contratoDetails.getFechaInicio());
        existente.setFechaFin(contratoDetails.getFechaFin());
        if (contratoDetails.getCliente() != null) {
            existente.setCliente(contratoDetails.getCliente());
        }
        Contrato actualizado = contratoRepository.save(existente);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarContrato(@PathVariable Long id) {
        contratoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
