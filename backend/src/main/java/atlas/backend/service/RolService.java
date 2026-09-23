package atlas.backend.service;

import atlas.backend.model.Rol;
import java.util.List;

public interface RolService {
    List<Rol> obtenerTodos();
    Rol obtenerPorId(Long id);
    Rol crear(Rol rol);
    Rol actualizar(Long id, Rol rolDetalles);
    void eliminar(Long id);
}
