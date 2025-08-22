package com.encantomulher.service;

import com.encantomulher.model.Venta;
import com.encantomulher.repository.VentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VentaServiceImpl implements VentaService {

    private final VentaRepository repo;

    public VentaServiceImpl(VentaRepository repo) {
        this.repo = repo;
    }

    @Override public List<Venta> findAll() { return repo.findAll(); }

    @Override public Venta findById(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Override @Transactional(readOnly = true)
    public Venta findByIdConDetalles(Long id) {
        return repo.findByIdFetchDetalles(id).orElse(null);
    }

    @Override public Venta save(Venta v) { return repo.save(v); }

    @Override public void delete(Long id) { repo.deleteById(id); }
}