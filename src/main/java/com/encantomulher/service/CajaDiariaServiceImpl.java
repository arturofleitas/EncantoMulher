package com.encantomulher.service;

import com.encantomulher.model.CajaDiaria;
import com.encantomulher.model.CajaDiaria.EstadoCaja;
import com.encantomulher.model.Usuario;
import com.encantomulher.repository.CajaDiariaRepository;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CajaDiariaServiceImpl implements CajaDiariaService {

    @Autowired
    private CajaDiariaRepository cajaDiariaRepository;

    @Override
    @Transactional(readOnly = true)
    public CajaDiaria obtenerCajaAbierta(Usuario usuario) {
        return cajaDiariaRepository
                .findTopByUsuarioAndEstadoOrderByFechaAperturaDesc(usuario, EstadoCaja.abierta)
                .orElse(null);
    }

    @Override
    @Transactional
    public void abrirCaja(Usuario usuario) {
        // Si ya hay una caja abierta, no abrir otra
        if (obtenerCajaAbierta(usuario) != null) return;

        CajaDiaria nuevaCaja = new CajaDiaria();
        nuevaCaja.setUsuario(usuario);
        nuevaCaja.setEstado(EstadoCaja.abierta);
        nuevaCaja.setFechaApertura(java.time.LocalDateTime.now());

        // por defecto en 0 para evitar NPEs al formatear/operar
        nuevaCaja.setSaldoInicial(BigDecimal.ZERO);
        nuevaCaja.setSaldoFinal(BigDecimal.ZERO);

        cajaDiariaRepository.save(nuevaCaja);
    }

    @Override
    @Transactional
    public void cerrarCaja(Usuario usuario) {
        CajaDiaria cajaAbierta = obtenerCajaAbierta(usuario);
        if (cajaAbierta != null) {
            cajaAbierta.setEstado(EstadoCaja.cerrada);
            cajaAbierta.setFechaCierre(java.time.LocalDateTime.now());
            // Dejar saldoFinal tal cual esté al momento del cierre
            if (cajaAbierta.getSaldoFinal() == null) {
                cajaAbierta.setSaldoFinal(BigDecimal.ZERO);
            }
            cajaDiariaRepository.save(cajaAbierta);
        }
    }

    @Override
    @Transactional
    public void guardarCaja(CajaDiaria caja) {
        // normalizamos nulos
        if (caja.getSaldoInicial() == null) caja.setSaldoInicial(BigDecimal.ZERO);
        if (caja.getSaldoFinal() == null)   caja.setSaldoFinal(BigDecimal.ZERO);
        cajaDiariaRepository.save(caja);
    }
}