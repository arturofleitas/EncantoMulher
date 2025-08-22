package com.encantomulher.service;

import com.encantomulher.model.*;
import com.encantomulher.repository.CajaDiariaRepository;
import com.encantomulher.repository.MovimientoCajaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class MovimientoCajaServiceImpl implements MovimientoCajaService {

    private final CajaDiariaRepository cajaRepo;
    private final MovimientoCajaRepository movRepo;

    public MovimientoCajaServiceImpl(CajaDiariaRepository cajaRepo,
                                     MovimientoCajaRepository movRepo) {
        this.cajaRepo = cajaRepo;
        this.movRepo = movRepo;
    }

    @Override
    @Transactional
    public MovimientoCaja registrarMovimiento(Usuario usuario,
                                              BigDecimal monto,
                                              MovimientoCaja.TipoMovimiento tipo,
                                              String concepto,
                                              String descripcion,
                                              Venta venta) {
        // Trae la última caja ABIERTA del usuario
        CajaDiaria caja = cajaRepo
                .findTopByUsuarioAndEstadoOrderByFechaAperturaDesc(usuario, CajaDiaria.EstadoCaja.abierta)
                .orElseThrow(() -> new IllegalStateException("No hay caja abierta para " + usuario.getUsername()));

        BigDecimal montoSafe = monto != null ? monto : BigDecimal.ZERO;

        // Crear y guardar movimiento
        MovimientoCaja m = new MovimientoCaja();
        m.setUsuario(usuario);
        m.setCaja(caja);
        m.setMonto(montoSafe);
        m.setTipo(tipo);
        m.setConcepto(concepto);
        m.setDescripcion(descripcion);
        m.setFechaMovimiento(LocalDateTime.now());
        m.setVenta(venta);

        MovimientoCaja guardado = movRepo.save(m);

        // Actualizar saldo final de la caja (en memoria y BD)
        BigDecimal saldoActual = caja.getSaldoFinal() != null ? caja.getSaldoFinal() : BigDecimal.ZERO;
        if (tipo == MovimientoCaja.TipoMovimiento.ingreso) {
            saldoActual = saldoActual.add(montoSafe);
        } else {
            // egreso
            saldoActual = saldoActual.subtract(montoSafe);
        }
        caja.setSaldoFinal(saldoActual);
        cajaRepo.save(caja);

        return guardado;
    }
}