package atlas.backend.service;

import atlas.backend.model.EmpresaUsuario;
import java.util.List;

public interface EmpresaUsuarioService {
    List<EmpresaUsuario> obtenerTodos();
    EmpresaUsuario obtenerPorId(Long id);
    EmpresaUsuario crear(EmpresaUsuario empresaUsuario);
    EmpresaUsuario actualizar(Long id, EmpresaUsuario detalles);
    void eliminar(Long id);
}
