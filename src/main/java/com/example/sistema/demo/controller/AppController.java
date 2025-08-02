package com.example.sistema.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {

    // ... (seus métodos existentes para "/", "/login", "/produtos") ...
    @GetMapping("/")
    public String paginaInicial() { return "index"; }

    @GetMapping("/login")
    public String paginaLogin(Authentication auth) {
        if (auth != null && auth.isAuthenticated()) return "redirect:/";
        return "login";
    }

    @GetMapping("/produtos")
    public String paginaProdutos() { return "produto"; }

    // ADICIONE ESTE NOVO MÉTODO
    @GetMapping("/configuracoes")
    public String paginaConfiguracoes() {
        return "configuracoes"; // Renderiza /templates/configuracoes.html
    }
}