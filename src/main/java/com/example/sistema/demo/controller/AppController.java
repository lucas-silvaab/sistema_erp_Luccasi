package com.example.sistema.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {

    /**
     * A rota principal ("/") agora mostra a página de usuários.
     * Como essa rota será protegida pelo Spring Security, o usuário
     * só chegará aqui após o login.
     */
    
    @GetMapping("/")
    public String paginaInicial() {
        return "index"; // Renderiza o arquivo /templates/usuarios.html
    }

    /**
     * A rota "/login" agora mostra a página com o formulário de login.
     * Esta rota será pública.
     */
    @GetMapping("/login")
    public String paginaLogin(Authentication authentication) {
        // VERIFICAÇÃO: Se o objeto de autenticação não for nulo e o usuário estiver autenticado...
        if (authentication != null && authentication.isAuthenticated()) {
            // ...então redirecione para a página principal.
            return "redirect:/";
        }
        
        // Se não houver usuário logado, mostre a página de login normalmente.
        return "login";
    }

}