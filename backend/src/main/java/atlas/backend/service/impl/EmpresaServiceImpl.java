package atlas.backend.service.impl;

import atlas.backend.exception.ResourceNotFoundException;
import atlas.backend.model.Empresa;
import atlas.backend.repository.EmpresaRepository;
import atlas.backend.service.EmpresaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@Transactional
public class EmpresaServiceImpl implements EmpresaService {

    private final EmpresaRepository empresaRepository;

    public EmpresaServiceImpl(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Empresa> obtenerTodas() {
        return empresaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Empresa obtenerPorId(Long id) {
        return empresaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada con id: " + id));
    }

    @Override
    public Empresa crear(Empresa empresa) {
        if (empresa.getCorreo() != null && empresaRepository.existsByCorreo(empresa.getCorreo())) {
            throw new IllegalArgumentException("Ya existe una empresa registrada con el correo: " + empresa.getCorreo());
        }
        if (empresa.getCreatedAt() == null) {
            empresa.setCreatedAt(OffsetDateTime.now());
        }
        return empresaRepository.save(empresa);
    }

    @Override
    public Empresa actualizar(Long id, Empresa empresaDetalles) {
        Empresa empresaExistente = obtenerPorId(id);

        if (empresaDetalles.getCorreo() != null 
                && !empresaDetalles.getCorreo().equalsIgnoreCase(empresaExistente.getCorreo())
                && empresaRepository.existsByCorreo(empresaDetalles.getCorreo())) {
            throw new IllegalArgumentException("Ya existe una empresa registrada con el correo: " + empresaDetalles.getCorreo());
        }

        empresaExistente.setNombre(empresaDetalles.getNombre());
        empresaExistente.setDireccion(empresaDetalles.getDireccion());
        empresaExistente.setTelefono(empresaDetalles.getTelefono());
        empresaExistente.setCorreo(empresaDetalles.getCorreo());

        return empresaRepository.save(empresaExistente);
    }

    @Override
    public void eliminar(Long id) {
        Empresa empresa = obtenerPorId(id);
        empresaRepository.delete(empresa);
    }
}
