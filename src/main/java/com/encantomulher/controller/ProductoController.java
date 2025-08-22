package com.encantomulher.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.encantomulher.model.Producto;
import com.encantomulher.service.ProductoService;

@Controller
@RequestMapping("/producto")
public class ProductoController {
    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("productos", service.obtenerTodos());
        return "producto";
    }

    @GetMapping("/nuevo")
    public String nuevoFormulario(Model model) {
        model.addAttribute("producto", new Producto());
        return "producto_nuevo";
    }

    @GetMapping("/categoria/{categoria}")
    public String listarPorCategoria(@PathVariable("categoria") String categoria, Model model) {
        model.addAttribute("productos", service.listarPorCategoria(categoria));
        model.addAttribute("categoriaSeleccionada", categoria);
        return "producto";
    }


    @PostMapping
    public String guardar(@ModelAttribute Producto producto) {
        service.guardar(producto);
        return "redirect:/producto";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Producto producto = service.obtenerPorId(id).orElseThrow();
        model.addAttribute("producto", producto);
        return "producto_nuevo";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return "redirect:/producto";
    }

}