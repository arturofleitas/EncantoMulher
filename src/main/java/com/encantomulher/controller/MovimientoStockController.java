package com.encantomulher.controller;

import com.encantomulher.model.MovimientoStock;
import com.encantomulher.repository.MovimientoStockRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MovimientoStockController {

    @Autowired
    private MovimientoStockRepository movimientoStockRepository;

    @GetMapping("/stock")
    public String listarMovimientosStock(Model model) {
        List<MovimientoStock> movimientos = movimientoStockRepository.findAll();
        model.addAttribute("movimientos", movimientos);
        return "stock"; // stock.html
    }
}