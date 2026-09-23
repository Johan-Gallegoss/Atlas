package atlas.backend;

import atlas.backend.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!prod")
public class EntityVerification implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(EntityVerification.class);

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final EmpresaRepository empresaRepository;
    private final EmpresaUsuarioRepository empresaUsuarioRepository;
    private final ClienteRepository clienteRepository;
    private final ContratoRepository contratoRepository;
    private final DocumentoContratoRepository documentoContratoRepository;
    private final AuditoriaRepository auditoriaRepository;

    public EntityVerification(RolRepository rolRepository,
                               UsuarioRepository usuarioRepository,
                               EmpresaRepository empresaRepository,
                               EmpresaUsuarioRepository empresaUsuarioRepository,
                               ClienteRepository clienteRepository,
                               ContratoRepository contratoRepository,
                               DocumentoContratoRepository documentoContratoRepository,
                               AuditoriaRepository auditoriaRepository) {
        this.rolRepository = rolRepository;
        this.usuarioRepository = usuarioRepository;
        this.empresaRepository = empresaRepository;
        this.empresaUsuarioRepository = empresaUsuarioRepository;
        this.clienteRepository = clienteRepository;
        this.contratoRepository = contratoRepository;
        this.documentoContratoRepository = documentoContratoRepository;
        this.auditoriaRepository = auditoriaRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("=== VERIFICACION COMPLETA DE LAS 8 ENTIDADES JPA ===");

        log.info("[1/8] Tabla 'roles':               {} registro(s)", rolRepository.count());
        log.info("[2/8] Tabla 'usuarios':             {} registro(s)", usuarioRepository.count());
        log.info("[3/8] Tabla 'empresas':             {} registro(s)", empresaRepository.count());
        log.info("[4/8] Tabla 'empresa_usuarios':     {} registro(s)", empresaUsuarioRepository.count());
        log.info("[5/8] Tabla 'clientes':             {} registro(s)", clienteRepository.count());
        log.info("[6/8] Tabla 'contratos':            {} registro(s)", contratoRepository.count());
        log.info("[7/8] Tabla 'documentos_contrato':  {} registro(s)", documentoContratoRepository.count());
        log.info("[8/8] Tabla 'auditoria':            {} registro(s)", auditoriaRepository.count());

        log.info("=== VERIFICACION COMPLETADA SIN ERRORES ===");
    }
}
