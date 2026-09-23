package atlas.backend.controller;

import atlas.backend.exception.ResourceNotFoundException;
import atlas.backend.model.EmpresaUsuario;
import atlas.backend.service.EmpresaUsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/empresa-usuarios")
public class EmpresaUsuarioController {

    private final EmpresaUsuarioService empresaUsuarioService;

    public EmpresaUsuarioController(EmpresaUsuarioService empresaUsuarioService) {
        this.empresaUsuarioService = empresaUsuarioService;
    }

    @GetMapping
    public ResponseEntity<List<EmpresaUsuario>> listarTodos() {
        List<EmpresaUsuario> lista = empresaUsuarioService.obtenerTodos();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpresaUsuario> obtenerPorId(@PathVariable Long id) {
        EmpresaUsuario eu = empresaUsuarioService.obtenerPorId(id);
        return ResponseEntity.ok(eu);
    }

    @PostMapping
    public ResponseEntity<EmpresaUsuario> crear(@RequestBody EmpresaUsuario empresaUsuario) {
        EmpresaUsuario nuevo = empresaUsuarioService.crear(empresaUsuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmpresaUsuario> actualizar(@PathVariable Long id, @RequestBody EmpresaUsuario empresaUsuario) {
        EmpresaUsuario actualizado = empresaUsuarioService.actualizar(id, empresaUsuario);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        empresaUsuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
