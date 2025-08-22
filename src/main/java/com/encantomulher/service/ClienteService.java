package com.encantomulher.service;

import java.util.List;
import com.encantomulher.model.Cliente;

public interface ClienteService {
    List<Cliente> obtenerTodos();
    Cliente obtenerPorId(Long id);   // 🔹 Ya devuelve Cliente directo
    Cliente guardar(Cliente cliente);
    void eliminar(Long id);

    // 🔎 Para el buscador
    List<Cliente> buscarPorNombreODocumento(String query);
}
