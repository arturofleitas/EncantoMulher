package com.encantomulher.service;

import com.encantomulher.model.Producto;
import java.util.List;
import java.util.Optional;

public interface ProductoService {
    List<Producto> obtenerTodos();
    List<Producto> listarPorCategoria(String categoria);
    Optional<Producto> obtenerPorId(Long id);
    Producto guardar(Producto producto);
    void eliminar(Long id);

    // 🔎 Búsqueda dinámica (por nombre o código)
    List<Producto> buscarPorNombreOCodigo(String query);
}