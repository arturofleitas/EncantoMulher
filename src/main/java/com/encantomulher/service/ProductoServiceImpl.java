package com.encantomulher.service;

import com.encantomulher.model.Producto;
import com.encantomulher.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }

    @Override
    public List<Producto> listarPorCategoria(String categoria) {
        return productoRepository.findByCategoria(
            Producto.Categoria.valueOf(categoria.toUpperCase())
        );
    }

    @Override
    public Optional<Producto> obtenerPorId(Long id) {
        return productoRepository.findById(id);
    }

    @Override
    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public void eliminar(Long id) {
        productoRepository.deleteById(id);
    }

    @Override
    public List<Producto> buscarPorNombreOCodigo(String query) {
        return productoRepository.findByNombreContainingIgnoreCaseOrCodigoBarrasContainingIgnoreCase(query, query);
    }
}