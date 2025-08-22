package com.encantomulher.repository;

import com.encantomulher.model.CajaDiaria;
import com.encantomulher.model.Usuario;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CajaDiariaRepository extends JpaRepository<CajaDiaria, Long> {

    /**
     * Devuelve la última caja del usuario en el estado indicado (p.ej. ABIERTA),
     * ordenada por fecha de apertura descendente.
     * No hace fetch de la colección de movimientos: se recomienda cargarlos con
     * MovimientoCajaRepository.findByCajaOrderByFechaMovimientoDesc(caja).
     */
    Optional<CajaDiaria> findTopByUsuarioAndEstadoOrderByFechaAperturaDesc(
            Usuario usuario,
            CajaDiaria.EstadoCaja estado
    );

    /**
     * Variante con EntityGraph por si querés traer la caja junto con sus movimientos
     * y el usuario de cada movimiento en una sola consulta (sin ordenar aquí).
     * Úsalo de forma puntual cuando realmente necesites el "graph".
     */
    @EntityGraph(attributePaths = {
            "movimientosCaja",
            "movimientosCaja.usuario",
            "movimientosCaja.venta"
    })
    @Query("""
           select c
           from CajaDiaria c
           where c.idCaja = :idCaja
           """)
    Optional<CajaDiaria> findByIdWithMovimientos(@Param("idCaja") Long idCaja);
}