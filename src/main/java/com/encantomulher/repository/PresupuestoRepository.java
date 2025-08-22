package com.encantomulher.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.encantomulher.model.Presupuesto;

public interface PresupuestoRepository extends JpaRepository<Presupuesto, Long> {
    // Podés agregar métodos personalizados si necesitás buscar por cliente, estado, fechas, etc.
}