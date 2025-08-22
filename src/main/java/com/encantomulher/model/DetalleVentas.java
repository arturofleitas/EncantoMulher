package com.encantomulher.model;

import java.math.BigDecimal;
import java.util.Objects;
import jakarta.persistence.*;

@Entity
public class DetalleVentas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetalle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_venta", nullable = false)
    private Venta venta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal precioUnitario;

    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal descuento = BigDecimal.ZERO;

    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal subtotal;

    // ==== Getters/Setters ====
    public Long getIdDetalle() { return idDetalle; }
    public void setIdDetalle(Long idDetalle) { this.idDetalle = idDetalle; }

    public Venta getVenta() { return venta; }
    public void setVenta(Venta venta) { this.venta = venta; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }

    public BigDecimal getDescuento() { return descuento; }
    public void setDescuento(BigDecimal descuento) { this.descuento = descuento; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    // ==== Helpers opcionales ====
    /** Calcula subtotal = precioUnitario * cantidad - descuento (no deja negativo) */
    public void recalcularSubtotal() {
        BigDecimal base = (precioUnitario != null ? precioUnitario : BigDecimal.ZERO)
                .multiply(BigDecimal.valueOf(cantidad != null ? cantidad : 0));
        BigDecimal desc = descuento != null ? descuento : BigDecimal.ZERO;
        BigDecimal total = base.subtract(desc);
        if (total.signum() < 0) total = BigDecimal.ZERO;
        this.subtotal = total;
    }

    // equals/hashCode SOLO por id (evita problemas en sets/listas y con proxies LAZY)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DetalleVentas)) return false;
        DetalleVentas that = (DetalleVentas) o;
        return Objects.equals(idDetalle, that.idDetalle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idDetalle);
    }
}