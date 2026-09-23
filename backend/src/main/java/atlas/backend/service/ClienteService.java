package atlas.backend.service;

import atlas.backend.model.Cliente;
import java.util.List;

public interface ClienteService {
    List<Cliente> listarPorEmpresa(Long empresaId);
    Cliente obtenerPorIdYEmpresa(Long id, Long empresaId);
    List<Cliente> buscarPorNombreYEmpresa(String nombre, Long empresaId);
    Cliente crear(Cliente cliente);
    Cliente actualizar(Long id, Long empresaId, Cliente clienteDetalles);
    void eliminar(Long id, Long empresaId);
}
