package com.encantomulher.repository;

import com.encantomulher.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    List<Cliente> findByNombreContainingIgnoreCaseOrDocumentoContainingIgnoreCase(String nombre, String documento);
}