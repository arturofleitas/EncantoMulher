package com.encantomulher.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.*;

@Entity
public class CajaDiaria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCaja;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    private LocalDateTime fechaApertura;
    private LocalDateTime fechaCierre;

    private BigDecimal saldoInicial;
    private BigDecimal saldoFinal;

    @Enumerated(EnumType.STRING)
    private EstadoCaja estado;

    public enum EstadoCaja { abierta, cerrada }

    // IMPORTANTE: colecciones LAZY para evitar duplicados en hidratar
    @OneToMany(mappedBy = "caja", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
    private List<MovimientoCaja> movimientosCaja = new ArrayList<>();

    // --- getters/setters ---

    public Long getIdCaja() { return idCaja; }
    public void setIdCaja(Long idCaja) { this.idCaja = idCaja; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public LocalDateTime getFechaApertura() { return fechaApertura; }
    public void setFechaApertura(LocalDateTime fechaApertura) { this.fechaApertura = fechaApertura; }

    public LocalDateTime getFechaCierre() { return fechaCierre; }
    public void setFechaCierre(LocalDateTime fechaCierre) { this.fechaCierre = fechaCierre; }

    public BigDecimal getSaldoInicial() { return saldoInicial; }
    public void setSaldoInicial(BigDecimal saldoInicial) { this.saldoInicial = saldoInicial; }

    public BigDecimal getSaldoFinal() { return saldoFinal; }
    public void setSaldoFinal(BigDecimal saldoFinal) { this.saldoFinal = saldoFinal; }

    public EstadoCaja getEstado() { return estado; }
    public void setEstado(EstadoCaja estado) { this.estado = estado; }

    public List<MovimientoCaja> getMovimientosCaja() { return movimientosCaja; }
    public void setMovimientosCaja(List<MovimientoCaja> movimientosCaja) { this.movimientosCaja = movimientosCaja; }

    // --- equals & hashCode SOLO por id ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CajaDiaria)) return false;
        CajaDiaria that = (CajaDiaria) o;
        return Objects.equals(idCaja, that.idCaja);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCaja);
    }
}