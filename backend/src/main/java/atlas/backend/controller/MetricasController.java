package atlas.backend.controller;

import atlas.backend.repository.ClienteRepository;
import atlas.backend.repository.ContratoRepository;
import atlas.backend.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/metricas")
public class MetricasController {

    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final ContratoRepository contratoRepository;

    public MetricasController(UsuarioRepository usuarioRepository,
                              ClienteRepository clienteRepository,
                              ContratoRepository contratoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.clienteRepository = clienteRepository;
        this.contratoRepository = contratoRepository;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> obtenerMetricas(@RequestParam(required = false) Long empresaId) {
        Map<String, Object> response = new HashMap<>();

        long totalUsuarios = usuarioRepository.count();
        long totalClientes = clienteRepository.count();

        LocalDate hoy = LocalDate.now();
        long contratos7Dias = 0;
        long contratos15Dias = 0;
        long contratos30Dias = 0;

        List<?> proximosVencer = List.of();
        List<?> vencidos = List.of();

        if (empresaId != null) {
            contratos7Dias = contratoRepository.countPorVencer(empresaId, hoy, hoy.plusDays(7));
            contratos15Dias = contratoRepository.countPorVencer(empresaId, hoy, hoy.plusDays(15));
            contratos30Dias = contratoRepository.countPorVencer(empresaId, hoy, hoy.plusDays(30));

            proximosVencer = contratoRepository.findPorVencer(empresaId, hoy, hoy.plusDays(30));
            vencidos = contratoRepository.findVencidos(empresaId, hoy);
        }

        Map<String, Object> metricasMap = new HashMap<>();
        metricasMap.put("totalUsuarios", totalUsuarios);
        metricasMap.put("totalClientes", totalClientes);
        metricasMap.put("contratos7Dias", contratos7Dias);
        metricasMap.put("contratos15Dias", contratos15Dias);
        metricasMap.put("contratos30Dias", contratos30Dias);

        response.put("metricas", metricasMap);
        response.put("contratosProximosAVencer", proximosVencer);
        response.put("contratosVencidos", vencidos);

        return ResponseEntity.ok(response);
    }
}
