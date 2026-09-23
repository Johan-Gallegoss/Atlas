package atlas.backend.service.impl;

import atlas.backend.exception.ResourceNotFoundException;
import atlas.backend.model.Cliente;
import atlas.backend.repository.ClienteRepository;
import atlas.backend.service.ClienteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@Transactional
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> listarPorEmpresa(Long empresaId) {
        return clienteRepository.findByEmpresaIdOrderByIdDesc(empresaId);
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente obtenerPorIdYEmpresa(Long id, Long empresaId) {
        return clienteRepository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id " + id + " para la empresa " + empresaId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> buscarPorNombreYEmpresa(String nombre, Long empresaId) {
        return clienteRepository.findByNombreContainingIgnoreCaseAndEmpresaIdOrderByNombreAsc(nombre, empresaId);
    }

    @Override
    public Cliente crear(Cliente cliente) {
        if (cliente.getCreatedAt() == null) {
            cliente.setCreatedAt(OffsetDateTime.now());
        }
        return clienteRepository.save(cliente);
    }

    @Override
    public Cliente actualizar(Long id, Long empresaId, Cliente clienteDetalles) {
        Cliente clienteExistente = obtenerPorIdYEmpresa(id, empresaId);

        clienteExistente.setNombre(clienteDetalles.getNombre());
        clienteExistente.setCorreo(clienteDetalles.getCorreo());
        clienteExistente.setTelefono(clienteDetalles.getTelefono());
        clienteExistente.setDireccion(clienteDetalles.getDireccion());
        clienteExistente.setEsEmpresa(clienteDetalles.getEsEmpresa());
        clienteExistente.setRut(clienteDetalles.getRut());
        clienteExistente.setNombreFantasia(clienteDetalles.getNombreFantasia());
        clienteExistente.setGiroActividadEconomica(clienteDetalles.getGiroActividadEconomica());
        clienteExistente.setNombreRepresentante(clienteDetalles.getNombreRepresentante());
        clienteExistente.setCargoRepresentante(clienteDetalles.getCargoRepresentante());
        clienteExistente.setCorreoRepresentante(clienteDetalles.getCorreoRepresentante());
        clienteExistente.setTelefonoRepresentante(clienteDetalles.getTelefonoRepresentante());
        clienteExistente.setRelacionRepresentante(clienteDetalles.getRelacionRepresentante());
        clienteExistente.setDireccionCalle(clienteDetalles.getDireccionCalle());
        clienteExistente.setDireccionNumero(clienteDetalles.getDireccionNumero());
        clienteExistente.setDireccionCiudad(clienteDetalles.getDireccionCiudad());
        clienteExistente.setDireccionRegion(clienteDetalles.getDireccionRegion());
        clienteExistente.setSitioWeb(clienteDetalles.getSitioWeb());
        clienteExistente.setTelefonoCorporativo(clienteDetalles.getTelefonoCorporativo());
        clienteExistente.setNombreBanco(clienteDetalles.getNombreBanco());
        clienteExistente.setNumeroCuenta(clienteDetalles.getNumeroCuenta());
        clienteExistente.setTitularCuenta(clienteDetalles.getTitularCuenta());
        clienteExistente.setMetodoPago(clienteDetalles.getMetodoPago());
        clienteExistente.setDiaPago(clienteDetalles.getDiaPago());
        clienteExistente.setMoneda(clienteDetalles.getMoneda());

        if (clienteDetalles.getEmpresa() != null) {
            clienteExistente.setEmpresa(clienteDetalles.getEmpresa());
        }

        return clienteRepository.save(clienteExistente);
    }

    @Override
    public void eliminar(Long id, Long empresaId) {
        Cliente cliente = obtenerPorIdYEmpresa(id, empresaId);
        clienteRepository.delete(cliente);
    }
}
