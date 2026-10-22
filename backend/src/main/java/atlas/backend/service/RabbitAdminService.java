package atlas.backend.service;

import atlas.backend.dto.BindingDTO;
import atlas.backend.dto.ColaDTO;
import atlas.backend.dto.ExchangeDTO;
import atlas.backend.dto.QueueInfoDTO;

/**
 * Servicio para la administracion dinamica de recursos en RabbitMQ.
 */
public interface RabbitAdminService {

    String crearCola(ColaDTO colaDTO);

    boolean eliminarCola(String nombre);

    String crearExchange(ExchangeDTO exchangeDTO);

    boolean eliminarExchange(String nombre);

    void crearBinding(BindingDTO bindingDTO);

    boolean eliminarBinding(BindingDTO bindingDTO);

    QueueInfoDTO obtenerInfoCola(String nombre);
}