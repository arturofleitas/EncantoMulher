package com.encantomulher.service;

import com.encantomulher.model.MovimientoCaja;
import com.encantomulher.model.Usuario;
import com.encantomulher.model.Venta;

import java.math.BigDecimal;

public interface MovimientoCajaService {
    // Registra un movimiento en la caja abierta del usuario (ingreso/egreso)
    MovimientoCaja registrarMovimiento(Usuario usuario,
                                       BigDecimal monto,
                                       MovimientoCaja.TipoMovimiento tipo,
                                       String concepto,
                                       String descripcion,
                                       Venta venta);
}