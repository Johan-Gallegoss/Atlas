package atlas.backend.service;

import atlas.backend.model.Empresa;
import java.util.List;

public interface EmpresaService {
    List<Empresa> obtenerTodas();
    Empresa obtenerPorId(Long id);
    Empresa crear(Empresa empresa);
    Empresa actualizar(Long id, Empresa empresaDetalles);
    void eliminar(Long id);
}
