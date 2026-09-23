package atlas.backend.service.impl;

import atlas.backend.exception.ResourceNotFoundException;
import atlas.backend.model.EmpresaUsuario;
import atlas.backend.repository.EmpresaUsuarioRepository;
import atlas.backend.service.EmpresaUsuarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@Transactional
public class EmpresaUsuarioServiceImpl implements EmpresaUsuarioService {

    private final EmpresaUsuarioRepository empresaUsuarioRepository;

    public EmpresaUsuarioServiceImpl(EmpresaUsuarioRepository empresaUsuarioRepository) {
        this.empresaUsuarioRepository = empresaUsuarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmpresaUsuario> obtenerTodos() {
        return empresaUsuarioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public EmpresaUsuario obtenerPorId(Long id) {
        return empresaUsuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EmpresaUsuario no encontrado con id: " + id));
    }

    @Override
    public EmpresaUsuario crear(EmpresaUsuario empresaUsuario) {
        if (empresaUsuario.getCreatedAt() == null) {
            empresaUsuario.setCreatedAt(OffsetDateTime.now());
        }
        return empresaUsuarioRepository.save(empresaUsuario);
    }

    @Override
    public EmpresaUsuario actualizar(Long id, EmpresaUsuario detalles) {
        EmpresaUsuario existente = obtenerPorId(id);

        if (detalles.getEmpresa() != null) {
            existente.setEmpresa(detalles.getEmpresa());
        }
        if (detalles.getUsuario() != null) {
            existente.setUsuario(detalles.getUsuario());
        }
        if (detalles.getRol() != null) {
            existente.setRol(detalles.getRol());
        }

        return empresaUsuarioRepository.save(existente);
    }

    @Override
    public void eliminar(Long id) {
        EmpresaUsuario existente = obtenerPorId(id);
        empresaUsuarioRepository.delete(existente);
    }
}
