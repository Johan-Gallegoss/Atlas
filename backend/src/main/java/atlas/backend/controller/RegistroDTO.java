package atlas.backend.controller;

import atlas.backend.model.Empresa;
import atlas.backend.model.Usuario;

public class RegistroDTO {
    private Empresa empresa;
    private Usuario usuario;

    private String nombre;
    private String direccion;
    private String telefono;
    private String correo;

    public RegistroDTO() {}

    public Empresa getEmpresa() { return empresa; }
    public void setEmpresa(Empresa empresa) { this.empresa = empresa; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public Empresa toEmpresa() {
        if (this.empresa != null) {
            return this.empresa;
        }
        Empresa e = new Empresa();
        e.setNombre(this.nombre);
        e.setDireccion(this.direccion);
        e.setTelefono(this.telefono);
        e.setCorreo(this.correo);
        return e;
    }
}
