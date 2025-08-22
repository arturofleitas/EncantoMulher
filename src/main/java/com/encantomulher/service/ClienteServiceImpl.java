package com.encantomulher.service;

import com.encantomulher.model.Cliente;
import com.encantomulher.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public List<Cliente> obtenerTodos() {
        return clienteRepository.findAll();
    }

    @Override
    public Cliente obtenerPorId(Long id) {
        // 🔹 Devuelve el cliente o null si no existe
        return clienteRepository.findById(id).orElse(null);
    }

    @Override
    public Cliente guardar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    public void eliminar(Long id) {
        clienteRepository.deleteById(id);
    }

    @Override
    public List<Cliente> buscarPorNombreODocumento(String query) {
        // 🔎 Debes definir este método en ClienteRepository
        return clienteRepository.findByNombreContainingIgnoreCaseOrDocumentoContainingIgnoreCase(query, query);
    }
}