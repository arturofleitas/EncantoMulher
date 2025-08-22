package com.encantomulher.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.encantomulher.model.DetalleVentas;

@Repository
public interface DetalleVentasRepository extends JpaRepository<DetalleVentas, Long> {
     // Buscar todos los detalles de una venta específica
    List<DetalleVentas> findByVenta_IdVenta(Long idVenta);

    // Si quieres buscar por producto
    List<DetalleVentas> findByProducto_IdProducto(Long idProducto);

}
