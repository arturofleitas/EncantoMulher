package com.encantomulher.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

      @GetMapping("/login")
    public String mostrarFormularioLogin() {
        return "login";  // Debe existir login.html en templates
    }

}
