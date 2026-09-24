package atlas.backend.controller;

import atlas.backend.model.Auditoria;
import atlas.backend.repository.AuditoriaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/auditoria")
public class AuditoriaController {

    private final AuditoriaRepository auditoriaRepository;

    public AuditoriaController(AuditoriaRepository auditoriaRepository) {
        this.auditoriaRepository = auditoriaRepository;
    }

    @GetMapping
    public ResponseEntity<List<Auditoria>> listarAuditoria(@RequestParam(required = false) String usuarioId) {
        if (usuarioId != null && !usuarioId.isBlank()) {
            return ResponseEntity.ok(auditoriaRepository.findByUsuarioIdOrderByIdDesc(usuarioId));
        }
        return ResponseEntity.ok(auditoriaRepository.findAllByOrderByIdDesc());
    }

    @PostMapping
    public ResponseEntity<Auditoria> registrarAuditoria(@RequestBody Auditoria auditoria) {
        if (auditoria.getFechaHora() == null) {
            auditoria.setFechaHora(OffsetDateTime.now());
        }
        Auditoria guardado = auditoriaRepository.save(auditoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }
}
