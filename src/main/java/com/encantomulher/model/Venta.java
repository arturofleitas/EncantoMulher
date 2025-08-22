package com.encantomulher.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.*;

@Entity
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVenta;

    // FK a Cliente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    // FK a Usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "venta", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
    private List<DetalleVentas> detalles = new ArrayList<>();

    // IMPORTANTE: 1-N con MovimientoCaja (no OneToOne)
    @OneToMany(mappedBy = "venta", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
    private List<MovimientoCaja> movimientosCaja = new ArrayList<>();

    @Column(name = "fecha_venta")
    private LocalDateTime fechaVenta = LocalDateTime.now();

    @Column(precision = 12, scale = 2)
    private BigDecimal subtotal;

    @Column(precision = 12, scale = 2)
    private BigDecimal descuento;

    @Column(precision = 12, scale = 2)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_pago")
    private TipoPago tipoPago;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    // Enums internos (alineados con lo que usás en el controlador)
    public enum TipoPago { efectivo, tarjeta, transferencia, mixto }
    public enum Estado   { pendiente, pagado, anulado }

    // ==== Getters/Setters ====

    public Long getIdVenta() { return idVenta; }
    public void setIdVenta(Long idVenta) { this.idVenta = idVenta; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public List<DetalleVentas> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleVentas> detalles) { this.detalles = detalles; }

    public List<MovimientoCaja> getMovimientosCaja() { return movimientosCaja; }
    public void setMovimientosCaja(List<MovimientoCaja> movimientosCaja) { this.movimientosCaja = movimientosCaja; }

    public LocalDateTime getFechaVenta() { return fechaVenta; }
    public void setFechaVenta(LocalDateTime fechaVenta) { this.fechaVenta = fechaVenta; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public BigDecimal getDescuento() { return descuento; }
    public void setDescuento(BigDecimal descuento) { this.descuento = descuento; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public TipoPago getTipoPago() { return tipoPago; }
    public void setTipoPago(TipoPago tipoPago) { this.tipoPago = tipoPago; }

    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }

    // ==== Helpers de conveniencia (mantienen ambos lados en sync) ====
    public void addMovimientoCaja(MovimientoCaja mov) {
        if (mov != null) {
            this.movimientosCaja.add(mov);
            mov.setVenta(this);
        }
    }

    public void removeMovimientoCaja(MovimientoCaja mov) {
        if (mov != null) {
            this.movimientosCaja.remove(mov);
            if (mov.getVenta() == this) mov.setVenta(null);
        }
    }

    // equals/hashCode SOLO por id
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Venta)) return false;
        Venta venta = (Venta) o;
        return Objects.equals(idVenta, venta.idVenta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idVenta);
    }
}