package com.encantomulher.controller;

import com.encantomulher.model.CajaDiaria;
import com.encantomulher.model.MovimientoCaja;
import com.encantomulher.model.Usuario;
import com.encantomulher.repository.CajaDiariaRepository;
import com.encantomulher.repository.MovimientoCajaRepository;
import com.encantomulher.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class CajaController {

    @Autowired private CajaDiariaRepository cajaDiariaRepo;
    @Autowired private MovimientoCajaRepository movimientoCajaRepo;
    @Autowired private UsuarioRepository usuarioRepo;

    /** Resumen de la caja abierta del usuario logueado */
    @GetMapping("/caja")
    public String verCaja(Model model) {
        Usuario user = getCurrentUser();

        // Busca la última caja ABIERTA del usuario
        CajaDiaria caja = cajaDiariaRepo
                .findTopByUsuarioAndEstadoOrderByFechaAperturaDesc(user, CajaDiaria.EstadoCaja.abierta)
                .orElse(null);

        if (caja == null) {
            model.addAttribute("caja", null);
            return "caja";
        }

        // Movimientos ordenados (más nuevos primero)
        List<MovimientoCaja> movimientos = movimientoCajaRepo.findByCajaOrderByFechaMovimientoDesc(caja);

        // Totales
        BigDecimal ingresos = movimientos.stream()
                .filter(m -> m.getTipo() == MovimientoCaja.TipoMovimiento.ingreso)
                .map(MovimientoCaja::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal egresos = movimientos.stream()
                .filter(m -> m.getTipo() == MovimientoCaja.TipoMovimiento.egreso)
                .map(MovimientoCaja::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal saldoInicial = caja.getSaldoInicial() != null ? caja.getSaldoInicial() : BigDecimal.ZERO;
        BigDecimal saldoCalculado = saldoInicial.add(ingresos).subtract(egresos);

        // Si caja tiene saldoFinal, úsalo, sino mostramos el calculado en vivo
        BigDecimal saldoActual = (caja.getSaldoFinal() != null ? caja.getSaldoFinal() : saldoCalculado);

        model.addAttribute("caja", caja);
        model.addAttribute("movimientos", movimientos);
        model.addAttribute("totalIngresos", ingresos);
        model.addAttribute("totalEgresos", egresos);
        model.addAttribute("saldoActual", saldoActual);

        return "caja";
    }

    /** Abre una nueva caja (si no existe ya una abierta para el usuario) */
    @PostMapping("/caja/abrir")
    public String abrirCaja() {
        Usuario user = getCurrentUser();

        boolean yaAbierta = cajaDiariaRepo
                .findTopByUsuarioAndEstadoOrderByFechaAperturaDesc(user, CajaDiaria.EstadoCaja.abierta)
                .isPresent();
        if (!yaAbierta) {
            CajaDiaria caja = new CajaDiaria();
            caja.setUsuario(user);
            caja.setFechaApertura(LocalDateTime.now());
            caja.setEstado(CajaDiaria.EstadoCaja.abierta);
            // Si querés heredar saldo final de la última cerrada, aquí podrías consultarla.
            caja.setSaldoInicial(BigDecimal.ZERO);
            cajaDiariaRepo.save(caja);
        }
        return "redirect:/caja";
    }

    /** Cierra la caja abierta calculando el saldo final */
    @PostMapping("/caja/cerrar")
    public String cerrarCaja() {
        Usuario user = getCurrentUser();

        CajaDiaria caja = cajaDiariaRepo
                .findTopByUsuarioAndEstadoOrderByFechaAperturaDesc(user, CajaDiaria.EstadoCaja.abierta)
                .orElse(null);
        if (caja != null) {
            List<MovimientoCaja> movimientos = movimientoCajaRepo.findByCajaOrderByFechaMovimientoDesc(caja);
            BigDecimal ingresos = movimientos.stream()
                    .filter(m -> m.getTipo() == MovimientoCaja.TipoMovimiento.ingreso)
                    .map(MovimientoCaja::getMonto)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal egresos = movimientos.stream()
                    .filter(m -> m.getTipo() == MovimientoCaja.TipoMovimiento.egreso)
                    .map(MovimientoCaja::getMonto)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal saldoInicial = caja.getSaldoInicial() != null ? caja.getSaldoInicial() : BigDecimal.ZERO;
            BigDecimal saldoFinal = saldoInicial.add(ingresos).subtract(egresos);

            caja.setSaldoFinal(saldoFinal);
            caja.setFechaCierre(LocalDateTime.now());
            caja.setEstado(CajaDiaria.EstadoCaja.cerrada);
            cajaDiariaRepo.save(caja);
        }
        return "redirect:/caja";
    }

    // === Helper ===
    private Usuario getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return usuarioRepo.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario autenticado no encontrado: " + username));
    }
}