package com.encantomulher.service;

import com.encantomulher.model.Usuario;
import java.util.List;

public interface UsuarioService {
    List<Usuario> obtenerTodos();
    Usuario obtenerPorId(Long id);
    Usuario guardar(Usuario usuario);
    void eliminar(Long id);
    Usuario findByUsername(String username); // agregado
}