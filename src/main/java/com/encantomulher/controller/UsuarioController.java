package com.encantomulher.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping; // <-- Agregar
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping; // <-- Agregar
import org.springframework.web.bind.annotation.RequestMapping;

import com.encantomulher.model.Usuario;
import com.encantomulher.repository.UsuarioRepository;

@Controller
@RequestMapping("/registro")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping // <-- Añadir esto
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping // <-- Añadir esto
    public String registrarUsuario(@ModelAttribute Usuario usuario) {
        System.out.println("Usuario recibido: " + usuario);
        try {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            usuario.setFechaRegistro(LocalDateTime.now());
            usuario.setFechaContratacion(LocalDate.now()); // 👈 Asignar fecha actual
            usuario.setUltimoAcceso(LocalDateTime.now()); 
            usuario.setEstado(Usuario.Estado.activo);

            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));  // 👈 codifica
            usuarioRepository.save(usuario);
            System.out.println("Usuario guardado: " + usuario.getUsername());
            return "redirect:/login";
        } catch (Exception e) {
            e.printStackTrace();
            return "registro";
        }
    }
}