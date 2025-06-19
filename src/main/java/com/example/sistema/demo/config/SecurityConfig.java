package com.example.sistema.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.sistema.demo.business.UsuarioService;
import com.example.sistema.demo.infrastructure.entitys.Usuario;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authorize -> authorize
                // Regras para endpoints públicos (arquivos estáticos, login, cadastro de usuário)
                .requestMatchers(
                    "/login", 
                    "/css/**",
                    "/images/**", 
                    "/js/**", 
                    "/webjars/**",
                    "/index.html" // Permite acesso à página index.html (já que o JS dela redireciona para login se não autenticado)
                ).permitAll()

                // Permite acesso PÚBLICO apenas ao método POST em /usuario (para cadastro de novos usuários)
                .requestMatchers(HttpMethod.POST, "/usuario").permitAll()
                
                // Adicione a regra para sua nova página de importações.
                // Como ela usa o JS para carregar dados que exigem autenticação,
                // a página em si pode ser permitida a todos, mas o JS falhará se não houver login.
                // Ou, se você quer que a página de importações só seja acessível após login:
                // .requestMatchers("/importacoes.html").authenticated() // Se você quer que a página em si exija login

                // Exige autenticação para qualquer outra requisição,
                // o que inclui:
                // - GET, PUT, DELETE em /usuario
                // - GET /usuario/todos, GET /usuario/info
                // - TODOS os endpoints da API /importacoes/** (GET, POST, PUT, DELETE)
                //   já que eles não foram explicitamente permitidos para todos.
                .anyRequest().authenticated() 
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true) // Redireciona para a raiz ou /index.html após login
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
            );
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(UsuarioService usuarioService) {
        return email -> {
            try {
                Usuario usuario = usuarioService.buscarUsuarioPorEmailUsuario(email);
                return org.springframework.security.core.userdetails.User.builder()
                        .username(usuario.getEmail())
                        .password(usuario.getSenha())
                        .roles("USER") // Você pode ter roles mais específicas para importações se quiser
                        .build();
            } catch (RuntimeException e) {
                throw new UsernameNotFoundException("Usuário não encontrado: " + email);
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}