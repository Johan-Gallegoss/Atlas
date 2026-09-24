package atlas.backend.controller;

import atlas.backend.exception.ResourceNotFoundException;
import atlas.backend.model.Empresa;
import atlas.backend.model.EmpresaUsuario;
import atlas.backend.model.Rol;
import atlas.backend.model.Usuario;
import atlas.backend.repository.EmpresaUsuarioRepository;
import atlas.backend.repository.RolRepository;
import atlas.backend.service.EmpresaService;
import atlas.backend.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/empresas")
public class EmpresaController {

    private final EmpresaService empresaService;
    private final UsuarioService usuarioService;
    private final RolRepository rolRepository;
    private final EmpresaUsuarioRepository empresaUsuarioRepository;

    public EmpresaController(EmpresaService empresaService,
                             UsuarioService usuarioService,
                             RolRepository rolRepository,
                             EmpresaUsuarioRepository empresaUsuarioRepository) {
        this.empresaService = empresaService;
        this.usuarioService = usuarioService;
        this.rolRepository = rolRepository;
        this.empresaUsuarioRepository = empresaUsuarioRepository;
    }

    @GetMapping
    public ResponseEntity<List<Empresa>> listarEmpresas() {
        List<Empresa> empresas = empresaService.obtenerTodas();
        return ResponseEntity.ok(empresas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Empresa> obtenerEmpresa(@PathVariable Long id) {
        Empresa empresa = empresaService.obtenerPorId(id);
        return ResponseEntity.ok(empresa);
    }

    @PostMapping
    public ResponseEntity<Empresa> crearEmpresa(@RequestBody RegistroDTO dto) {
        Empresa empresaTarget = dto.toEmpresa();
        Empresa nuevaEmpresa = empresaService.crear(empresaTarget);

        if (dto.getUsuario() != null) {
            Usuario nuevoUsuario = usuarioService.crear(dto.getUsuario());

            Rol rolAdmin = rolRepository.findByNombre("administrador")
                    .orElseGet(() -> rolRepository.save(new Rol("administrador")));

            EmpresaUsuario eu = new EmpresaUsuario();
            eu.setEmpresa(nuevaEmpresa);
            eu.setUsuario(nuevoUsuario);
            eu.setRol(rolAdmin);
            eu.setCreatedAt(OffsetDateTime.now());
            empresaUsuarioRepository.save(eu);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaEmpresa);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Empresa> actualizarEmpresa(@PathVariable Long id, @RequestBody Empresa empresa) {
        Empresa empresaActualizada = empresaService.actualizar(id, empresa);
        return ResponseEntity.ok(empresaActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEmpresa(@PathVariable Long id) {
        empresaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
}
