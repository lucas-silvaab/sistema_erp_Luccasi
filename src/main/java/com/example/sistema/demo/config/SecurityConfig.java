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
            // Regras para endpoints públicos
            .requestMatchers(
                "/login", 
                "/css/**", 
                "/js/**", 
                "/webjars/**"
            ).permitAll()

            // REGRA CORRIGIDA: Permite acesso PÚBLICO apenas ao método POST em /usuario
            // Isso permite que novos usuários se cadastrem sem estarem logados.
            .requestMatchers(HttpMethod.POST, "/usuario").permitAll()
            
            // Qualquer outra requisição, incluindo GET, DELETE, PUT em /usuario, exige autenticação.
            .anyRequest().authenticated() 
        )
        .formLogin(form -> form
            .loginPage("/login")
            .defaultSuccessUrl("/", true)
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
                // Certifique-se de que sua entidade Usuario tem um campo 'senha'
                return org.springframework.security.core.userdetails.User.builder()
                        .username(usuario.getEmail())
                        .password(usuario.getSenha()) // Usando o campo de senha
                        .roles("USER")
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