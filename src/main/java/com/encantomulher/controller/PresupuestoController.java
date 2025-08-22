package com.encantomulher.controller;

import com.encantomulher.model.Cliente;
import com.encantomulher.model.Presupuesto;
import com.encantomulher.model.Usuario;
import com.encantomulher.repository.PresupuestoRepository;
import com.encantomulher.service.ClienteService;
import com.encantomulher.service.UsuarioService;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PresupuestoController {

    private final PresupuestoRepository presupuestoRepository;
    
    @Autowired
    private ClienteService clienteServicio;

    @Autowired
    private UsuarioService usuarioServicio;

    PresupuestoController(PresupuestoRepository presupuestoRepository) {
        this.presupuestoRepository = presupuestoRepository;
    }

    @GetMapping("/presupuesto")
    public String mostrarPresupuestos(Model model) {
        List<Presupuesto> presupuestos = presupuestoRepository.findAll();
        model.addAttribute("presupuestos", presupuestos);
        return "presupuesto"; // Carga presupuesto.html
    }

    @GetMapping("/presupuesto/nuevo")
    public String mostrarFormularioNuevoPresupuesto(Model model) {
    model.addAttribute("presupuesto", new Presupuesto());

    // Cargar clientes para el select
    List<Cliente> clientes = clienteServicio.obtenerTodos();
    model.addAttribute("clientes", clientes);

    // Cargar usuarios para el select
    List<Usuario> usuarios = usuarioServicio.obtenerTodos();
    model.addAttribute("usuarios", usuarios);

    return "presupuesto_nuevo";
}



    @PostMapping("/presupuesto/guardar")
    public String guardarPresupuesto(@ModelAttribute Presupuesto presupuesto) {
    presupuesto.setFechaEmision(LocalDateTime.now());
    presupuestoRepository.save(presupuesto);
        return "redirect:/presupuesto";
    }

}