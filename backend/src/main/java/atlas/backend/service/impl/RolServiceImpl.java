package atlas.backend.service.impl;

import atlas.backend.exception.ResourceNotFoundException;
import atlas.backend.model.Rol;
import atlas.backend.repository.RolRepository;
import atlas.backend.service.RolService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RolServiceImpl implements RolService {

    private final RolRepository rolRepository;

    public RolServiceImpl(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Rol> obtenerTodos() {
        return rolRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Rol obtenerPorId(Long id) {
        return rolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con id: " + id));
    }

    @Override
    public Rol crear(Rol rol) {
        if (rol.getNombre() != null && rolRepository.existsByNombre(rol.getNombre())) {
            throw new IllegalArgumentException("Ya existe un rol con el nombre: " + rol.getNombre());
        }
        return rolRepository.save(rol);
    }

    @Override
    public Rol actualizar(Long id, Rol rolDetalles) {
        Rol rolExistente = obtenerPorId(id);

        if (rolDetalles.getNombre() != null 
                && !rolDetalles.getNombre().equalsIgnoreCase(rolExistente.getNombre())
                && rolRepository.existsByNombre(rolDetalles.getNombre())) {
            throw new IllegalArgumentException("Ya existe un rol con el nombre: " + rolDetalles.getNombre());
        }

        rolExistente.setNombre(rolDetalles.getNombre());
        return rolRepository.save(rolExistente);
    }

    @Override
    public void eliminar(Long id) {
        Rol rol = obtenerPorId(id);
        rolRepository.delete(rol);
    }
}
