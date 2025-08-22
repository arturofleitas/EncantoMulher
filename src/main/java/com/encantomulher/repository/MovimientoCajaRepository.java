package com.encantomulher.repository;

import com.encantomulher.model.CajaDiaria;
import com.encantomulher.model.MovimientoCaja;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovimientoCajaRepository extends JpaRepository<MovimientoCaja, Long> {
    List<MovimientoCaja> findByCajaOrderByFechaMovimientoDesc(CajaDiaria caja);
}