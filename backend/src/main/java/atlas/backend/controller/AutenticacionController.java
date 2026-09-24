package atlas.backend.controller;

import atlas.backend.model.EmpresaUsuario;
import atlas.backend.model.Usuario;
import atlas.backend.repository.EmpresaUsuarioRepository;
import atlas.backend.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/autenticacion")
public class AutenticacionController {

    private final UsuarioRepository usuarioRepository;
    private final EmpresaUsuarioRepository empresaUsuarioRepository;

    public AutenticacionController(UsuarioRepository usuarioRepository,
                                  EmpresaUsuarioRepository empresaUsuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.empresaUsuarioRepository = empresaUsuarioRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String input = body.get("nombre_usuario");
        if (input == null || input.isBlank()) {
            input = body.get("nombreUsuario");
        }
        if (input == null || input.isBlank()) {
            input = body.get("correo");
        }
        String password = body.get("password");

        if (input == null || password == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Credenciales requeridas", "glosa", "Debe ingresar nombre de usuario y contraseña"));
        }

        Optional<Usuario> optUsuario = usuarioRepository.findByNombreUsuario(input);
        if (optUsuario.isEmpty()) {
            optUsuario = usuarioRepository.findByCorreo(input);
        }

        if (optUsuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Credenciales inválidas", "glosa", "Nombre de usuario o contraseña incorrectos"));
        }

        Usuario usuario = optUsuario.get();

        if (!password.equals(usuario.getContrasenaHash())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Credenciales inválidas", "glosa", "Nombre de usuario o contraseña incorrectos"));
        }

        Long empresaId = null;
        String rolNombre = "administrador";

        Optional<EmpresaUsuario> optEu = empresaUsuarioRepository.findTopByUsuarioIdOrderByIdDesc(usuario.getId());
        if (optEu.isPresent()) {
            EmpresaUsuario eu = optEu.get();
            if (eu.getEmpresa() != null) {
                empresaId = eu.getEmpresa().getId();
            }
            if (eu.getRol() != null) {
                rolNombre = eu.getRol().getNombre();
            }
        }

        String headerJson = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
        String payloadJson = String.format(
                "{\"sub\":\"%s\",\"usuarioId\":%d,\"nombre_usuario\":\"%s\",\"email\":\"%s\",\"empresaId\":%s,\"role\":\"%s\"}",
                usuario.getId(),
                usuario.getId(),
                usuario.getNombreUsuario(),
                usuario.getCorreo(),
                empresaId != null ? empresaId : "null",
                rolNombre
        );

        String headerB64 = Base64.getUrlEncoder().withoutPadding().encodeToString(headerJson.getBytes(StandardCharsets.UTF_8));
        String payloadB64 = Base64.getUrlEncoder().withoutPadding().encodeToString(payloadJson.getBytes(StandardCharsets.UTF_8));
        String dummySignature = Base64.getUrlEncoder().withoutPadding().encodeToString("atlas-local-secret-signature".getBytes(StandardCharsets.UTF_8));

        String token = headerB64 + "." + payloadB64 + "." + dummySignature;

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("usuarioId", usuario.getId());
        response.put("empresaId", empresaId);
        response.put("role", rolNombre);

        return ResponseEntity.ok(response);
    }
}
