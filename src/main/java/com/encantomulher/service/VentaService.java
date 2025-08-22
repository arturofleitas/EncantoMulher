package com.encantomulher.service;

import com.encantomulher.model.Venta;
import java.util.List;

public interface VentaService {
    List<Venta> findAll();
    Venta findById(Long id);
    Venta findByIdConDetalles(Long id);
    Venta save(Venta venta);
    void delete(Long id);
}
