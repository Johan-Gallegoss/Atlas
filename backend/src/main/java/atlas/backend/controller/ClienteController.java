package atlas.backend.controller;

import atlas.backend.exception.ResourceNotFoundException;
import atlas.backend.model.Cliente;
import atlas.backend.repository.ClienteRepository;
import atlas.backend.service.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final ClienteRepository clienteRepository;

    public ClienteController(ClienteService clienteService, ClienteRepository clienteRepository) {
        this.clienteService = clienteService;
        this.clienteRepository = clienteRepository;
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> listarClientes(@RequestParam(required = false) Long empresaId) {
        if (empresaId == null) {
            return ResponseEntity.ok(clienteRepository.findAll());
        }
        List<Cliente> clientes = clienteService.listarPorEmpresa(empresaId);
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obtenerCliente(@PathVariable Long id, @RequestParam(required = false) Long empresaId) {
        if (empresaId == null) {
            return ResponseEntity.ok(clienteRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id " + id)));
        }
        Cliente cliente = clienteService.obtenerPorIdYEmpresa(id, empresaId);
        return ResponseEntity.ok(cliente);
    }

    @GetMapping("/search/{nombre}")
    public ResponseEntity<List<Cliente>> buscarPorNombre(@PathVariable String nombre, @RequestParam(required = false) Long empresaId) {
        if (empresaId == null) {
            return ResponseEntity.ok(clienteRepository.findByNombreContainingIgnoreCaseOrderByNombreAsc(nombre));
        }
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
            @RequestParam(required = false) Long empresaId,
            @RequestBody Cliente cliente) {
        if (empresaId == null && cliente.getEmpresa() != null) {
            empresaId = cliente.getEmpresa().getId();
        }
        if (empresaId == null) {
            Cliente clienteExistente = clienteRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id " + id));
            cliente.setId(id);
            return ResponseEntity.ok(clienteRepository.save(cliente));
        }
        Cliente clienteActualizado = clienteService.actualizar(id, empresaId, cliente);
        return ResponseEntity.ok(clienteActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id, @RequestParam(required = false) Long empresaId) {
        if (empresaId == null) {
            clienteRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        clienteService.eliminar(id, empresaId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
