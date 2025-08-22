package com.encantomulher.repository;

import com.encantomulher.model.Venta;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VentaRepository extends JpaRepository<Venta, Long> {

    @Query("""
           select v
           from Venta v
           left join fetch v.detalles d
           left join fetch d.producto p
           where v.idVenta = :id
           """)
    Optional<Venta> findByIdFetchDetalles(@Param("id") Long id);
}