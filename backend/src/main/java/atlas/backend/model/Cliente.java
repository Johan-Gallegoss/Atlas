package atlas.backend.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "correo", nullable = false, unique = true)
    private String correo;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "es_empresa", nullable = false)
    private Boolean esEmpresa = false;

    @Column(name = "rut")
    private String rut;

    @Column(name = "nombre_fantasia")
    private String nombreFantasia;

    @Column(name = "giro_actividad_economica")
    private String giroActividadEconomica;

    @Column(name = "nombre_representante")
    private String nombreRepresentante;

    @Column(name = "cargo_representante")
    private String cargoRepresentante;

    @Column(name = "correo_representante")
    private String correoRepresentante;

    @Column(name = "telefono_representante")
    private String telefonoRepresentante;

    @Column(name = "relacion_representante")
    private String relacionRepresentante;

    @Column(name = "direccion_calle")
    private String direccionCalle;

    @Column(name = "direccion_numero")
    private String direccionNumero;

    @Column(name = "direccion_ciudad")
    private String direccionCiudad;

    @Column(name = "direccion_region")
    private String direccionRegion;

    @Column(name = "sitio_web")
    private String sitioWeb;

    @Column(name = "telefono_corporativo")
    private String telefonoCorporativo;

    @Column(name = "nombre_banco")
    private String nombreBanco;

    @Column(name = "numero_cuenta")
    private String numeroCuenta;

    @Column(name = "titular_cuenta")
    private String titularCuenta;

    @Column(name = "metodo_pago")
    private String metodoPago;

    @Column(name = "dia_pago")
    private String diaPago;

    @Column(name = "moneda")
    private String moneda;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = true)
    private Empresa empresa;

    @Column(name = "created_at", columnDefinition = "timestamp with time zone")
    private OffsetDateTime createdAt;

    public Cliente() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public Boolean getEsEmpresa() { return esEmpresa; }
    public void setEsEmpresa(Boolean esEmpresa) { this.esEmpresa = esEmpresa; }

    public String getRut() { return rut; }
    public void setRut(String rut) { this.rut = rut; }

    public String getNombreFantasia() { return nombreFantasia; }
    public void setNombreFantasia(String nombreFantasia) { this.nombreFantasia = nombreFantasia; }

    public String getGiroActividadEconomica() { return giroActividadEconomica; }
    public void setGiroActividadEconomica(String giroActividadEconomica) { this.giroActividadEconomica = giroActividadEconomica; }

    public String getNombreRepresentante() { return nombreRepresentante; }
    public void setNombreRepresentante(String nombreRepresentante) { this.nombreRepresentante = nombreRepresentante; }

    public String getCargoRepresentante() { return cargoRepresentante; }
    public void setCargoRepresentante(String cargoRepresentante) { this.cargoRepresentante = cargoRepresentante; }

    public String getCorreoRepresentante() { return correoRepresentante; }
    public void setCorreoRepresentante(String correoRepresentante) { this.correoRepresentante = correoRepresentante; }

    public String getTelefonoRepresentante() { return telefonoRepresentante; }
    public void setTelefonoRepresentante(String telefonoRepresentante) { this.telefonoRepresentante = telefonoRepresentante; }

    public String getRelacionRepresentante() { return relacionRepresentante; }
    public void setRelacionRepresentante(String relacionRepresentante) { this.relacionRepresentante = relacionRepresentante; }

    public String getDireccionCalle() { return direccionCalle; }
    public void setDireccionCalle(String direccionCalle) { this.direccionCalle = direccionCalle; }

    public String getDireccionNumero() { return direccionNumero; }
    public void setDireccionNumero(String direccionNumero) { this.direccionNumero = direccionNumero; }

    public String getDireccionCiudad() { return direccionCiudad; }
    public void setDireccionCiudad(String direccionCiudad) { this.direccionCiudad = direccionCiudad; }

    public String getDireccionRegion() { return direccionRegion; }
    public void setDireccionRegion(String direccionRegion) { this.direccionRegion = direccionRegion; }

    public String getSitioWeb() { return sitioWeb; }
    public void setSitioWeb(String sitioWeb) { this.sitioWeb = sitioWeb; }

    public String getTelefonoCorporativo() { return telefonoCorporativo; }
    public void setTelefonoCorporativo(String telefonoCorporativo) { this.telefonoCorporativo = telefonoCorporativo; }

    public String getNombreBanco() { return nombreBanco; }
    public void setNombreBanco(String nombreBanco) { this.nombreBanco = nombreBanco; }

    public String getNumeroCuenta() { return numeroCuenta; }
    public void setNumeroCuenta(String numeroCuenta) { this.numeroCuenta = numeroCuenta; }

    public String getTitularCuenta() { return titularCuenta; }
    public void setTitularCuenta(String titularCuenta) { this.titularCuenta = titularCuenta; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public String getDiaPago() { return diaPago; }
    public void setDiaPago(String diaPago) { this.diaPago = diaPago; }

    public String getMoneda() { return moneda; }
    public void setMoneda(String moneda) { this.moneda = moneda; }

    public Empresa getEmpresa() { return empresa; }
    public void setEmpresa(Empresa empresa) { this.empresa = empresa; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}
