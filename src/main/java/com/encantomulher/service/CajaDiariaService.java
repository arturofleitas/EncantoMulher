package com.encantomulher.service;

import com.encantomulher.model.CajaDiaria;
import com.encantomulher.model.Usuario;

public interface CajaDiariaService {
    CajaDiaria obtenerCajaAbierta(Usuario usuario);
    void abrirCaja(Usuario usuario);
    void cerrarCaja(Usuario usuario);

    // Nuevo método para guardar o actualizar una caja
    void guardarCaja(CajaDiaria caja);
}