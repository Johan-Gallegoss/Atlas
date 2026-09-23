package atlas.backend.controller;

import atlas.backend.exception.ResourceNotFoundException;
import atlas.backend.model.Cliente;
import atlas.backend.service.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> listarClientes(@RequestParam Long empresaId) {
        List<Cliente> clientes = clienteService.listarPorEmpresa(empresaId);
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obtenerCliente(@PathVariable Long id, @RequestParam Long empresaId) {
        Cliente cliente = clienteService.obtenerPorIdYEmpresa(id, empresaId);
        return ResponseEntity.ok(cliente);
    }

    @GetMapping("/search/{nombre}")
    public ResponseEntity<List<Cliente>> buscarPorNombre(@PathVariable String nombre, @RequestParam Long empresaId) {
        List<Cliente> clientes = clienteService.buscarPorNombreYEmpresa(nombre, empresaId);
        return ResponseEntity.ok(clientes);
    }

    @PostMapping
    public ResponseEntity<Cliente> crearCliente(@RequestBody Cliente cliente) {
        Cliente nuevoCliente = clienteService.crear(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCliente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> actualizarCliente(
            @PathVariable Long id,
            @RequestParam Long empresaId,
            @RequestBody Cliente cliente) {
        Cliente clienteActualizado = clienteService.actualizar(id, empresaId, cliente);
        return ResponseEntity.ok(clienteActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id, @RequestParam Long empresaId) {
        clienteService.eliminar(id, empresaId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
