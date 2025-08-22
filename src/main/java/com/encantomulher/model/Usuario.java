package com.encantomulher.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Usuario implements org.springframework.security.core.userdetails.UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @Column(length = 20, unique = true)
    private String documento;

    @Column(length = 100, nullable = false)
    private String nombre;

    @Column(length = 100, unique = true, nullable = false)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(length = 255, nullable = false)
    private String password;

    @Column(length = 20)
    private String telefono;

    @Column(columnDefinition = "TEXT")
    private String direccion;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    private LocalDate fechaContratacion;

    private LocalDateTime fechaRegistro = LocalDateTime.now();

    private LocalDateTime ultimoAcceso;

    @Enumerated(EnumType.STRING)
    private Estado estado = Estado.activo;

    public enum Rol {
        VENDEDOR, ADMIN, GERENTE, STOCK
    }

    public enum Estado {
        activo, inactivo, suspendido
    }

    @OneToMany(mappedBy = "usuario")
    private List<Venta> ventas;

    @OneToMany(mappedBy = "usuario")
    private List<Presupuesto> presupuestos;

    @OneToMany(mappedBy = "usuario")
    private List<CajaDiaria> cajasDiarias;

    @OneToMany(mappedBy = "usuario")
    private List<MovimientoStock> movimientosStock;

    @OneToMany(mappedBy = "usuario")
    private List<MovimientoCaja> movimientosCaja;

      @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // convierte el enum rol en autoridad de Spring Security, con prefijo "ROLE_"
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.rol.name().toUpperCase()));
    }

    //getters ands setters

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
   
    public LocalDate getFechaContratacion() {
        return fechaContratacion;
    }

    public void setFechaContratacion(LocalDate fechaContratacion) {
        this.fechaContratacion = fechaContratacion;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public LocalDateTime getUltimoAcceso() {
        return ultimoAcceso;
    }

    public void setUltimoAcceso(LocalDateTime ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public List<Venta> getVentas() {
        return ventas;
    }

    public void setVentas(List<Venta> ventas) {
        this.ventas = ventas;
    }

    public List<Presupuesto> getPresupuestos() {
        return presupuestos;
    }

    public void setPresupuestos(List<Presupuesto> presupuestos) {
        this.presupuestos = presupuestos;
    }

    public List<CajaDiaria> getCajasDiarias() {
        return cajasDiarias;
    }

    public void setCajasDiarias(List<CajaDiaria> cajasDiarias) {
        this.cajasDiarias = cajasDiarias;
    }

    public List<MovimientoStock> getMovimientosStock() {
        return movimientosStock;
    }

    public void setMovimientosStock(List<MovimientoStock> movimientosStock) {
        this.movimientosStock = movimientosStock;
    }

    public List<MovimientoCaja> getMovimientosCaja() {
        return movimientosCaja;
    }

    public void setMovimientosCaja(List<MovimientoCaja> movimientosCaja) {
        this.movimientosCaja = movimientosCaja;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // o lógica propia
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.estado != Estado.suspendido;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // o lógica propia
    }

    @Override
    public boolean isEnabled() {
        return this.estado == Estado.activo;
    }

    @Override
public String toString() {
    return "Usuario{" +
            "idUsuario=" + idUsuario +
            ", documento='" + documento + '\'' +
            ", nombre='" + nombre + '\'' +
            ", email='" + email + '\'' +
            ", username='" + username + '\'' +
            ", password='" + password + '\'' +
            ", telefono='" + telefono + '\'' +
            ", direccion='" + direccion + '\'' +
            ", rol=" + rol +
            ", estado=" + estado +
            '}';
}

}

