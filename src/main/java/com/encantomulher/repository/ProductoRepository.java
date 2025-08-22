package com.encantomulher.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.encantomulher.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long>{
    List<Producto> findByCategoria(Producto.Categoria categoria);
    List<Producto> findByNombreContainingIgnoreCaseOrCodigoBarrasContainingIgnoreCase(String nombre, String codigo);


}
