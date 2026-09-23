package atlas.backend.service.impl;

import atlas.backend.exception.ResourceNotFoundException;
import atlas.backend.model.Usuario;
import atlas.backend.repository.UsuarioRepository;
import atlas.backend.service.UsuarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
    }

    @Override
    public Usuario crear(Usuario usuario) {
        if (usuario.getNombreUsuario() != null && usuarioRepository.existsByNombreUsuario(usuario.getNombreUsuario())) {
            throw new IllegalArgumentException("Ya existe un usuario con el nombre de usuario: " + usuario.getNombreUsuario());
        }
        if (usuario.getCorreo() != null && usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            throw new IllegalArgumentException("Ya existe un usuario con el correo: " + usuario.getCorreo());
        }
        if (usuario.getCreatedAt() == null) {
            usuario.setCreatedAt(OffsetDateTime.now());
        }
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario actualizar(Long id, Usuario usuarioDetalles) {
        Usuario usuarioExistente = obtenerPorId(id);

        if (usuarioDetalles.getNombreUsuario() != null 
                && !usuarioDetalles.getNombreUsuario().equalsIgnoreCase(usuarioExistente.getNombreUsuario())
                && usuarioRepository.existsByNombreUsuario(usuarioDetalles.getNombreUsuario())) {
            throw new IllegalArgumentException("Ya existe un usuario con el nombre de usuario: " + usuarioDetalles.getNombreUsuario());
        }

        if (usuarioDetalles.getCorreo() != null 
                && !usuarioDetalles.getCorreo().equalsIgnoreCase(usuarioExistente.getCorreo())
                && usuarioRepository.existsByCorreo(usuarioDetalles.getCorreo())) {
            throw new IllegalArgumentException("Ya existe un usuario con el correo: " + usuarioDetalles.getCorreo());
        }

        usuarioExistente.setNombreUsuario(usuarioDetalles.getNombreUsuario());
        if (usuarioDetalles.getContrasenaHash() != null) {
            usuarioExistente.setContrasenaHash(usuarioDetalles.getContrasenaHash());
        }
        usuarioExistente.setCorreo(usuarioDetalles.getCorreo());

        return usuarioRepository.save(usuarioExistente);
    }

    @Override
    public void eliminar(Long id) {
        Usuario usuario = obtenerPorId(id);
        usuarioRepository.delete(usuario);
    }
}
