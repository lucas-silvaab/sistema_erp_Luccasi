// Define o pacote onde a classe está. 'controller' indica que ela lida com as requisições web.
package com.example.sistema.demo.controller;

// Importa as anotações necessárias do Spring Framework para criar a API.
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Importa as classes que serão usadas pelo controller.
import com.example.sistema.demo.business.UsuarioService; // A classe de regras de negócio.
import com.example.sistema.demo.infrastructure.entitys.Usuario; // A classe de entidade.

// Importa uma anotação do Lombok para facilitar a criação de construtores.
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
// Importa classes para montar a resposta HTTP.
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
// Importa as anotações que definem os tipos de requisição HTTP (POST, GET, DELETE, PUT).
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import java.util.List;

/**
 * Controller é a camada que recebe as requisições externas (do navegador, de outra aplicação, etc.).
 * Ele funciona como um porteiro, direcionando cada requisição para o serviço correto.
 */

// @RestController: Combina @Controller e @ResponseBody, simplificando a criação de APIs REST.
// Basicamente, diz ao Spring que os retornos dos métodos desta classe serão o corpo da resposta HTTP (geralmente em JSON).
@RestController
// @RequestMapping("/usuario"): Define que todos os endpoints (URLs) desta classe começarão com "/usuario".
// Ex: http://localhost:8080/usuario
@RequestMapping("/usuario")
// @RequiredArgsConstructor: Anotação do Lombok que cria um construtor com todos os campos 'final' da classe.
// É uma forma limpa de fazer a injeção de dependência do 'UsuarioService'.
@RequiredArgsConstructor
public class UsuarioController {

    // Declara o serviço que contém a lógica de negócio.
    // 'final' garante que, após injetado pelo construtor, ele não será alterado.
    private final UsuarioService usuarioService;

    // --- Endpoints da API ---

    // @PostMapping: Mapeia requisições HTTP do tipo POST para este método.
    // Usado para CRIAR um novo recurso. URL: POST /usuario
    @PostMapping
    public ResponseEntity<Void> salvarUsuario(@RequestBody Usuario usuario) {
        // @RequestBody: Pega o JSON enviado no corpo da requisição e o transforma em um objeto 'Usuario'.
        usuarioService.salvarUsuario(usuario); // Chama o serviço para salvar o usuário.
        // Retorna uma resposta HTTP 200 (OK) sem conteúdo no corpo.
        return ResponseEntity.ok().build();
    }

    // @GetMapping: Mapeia requisições HTTP do tipo GET.
    // Usado para LER ou buscar um recurso. URL: GET /usuario?email=exemplo@email.com
    @GetMapping
    public ResponseEntity<Usuario> buscarUsuarioPorEmail(@RequestParam String email) {
        // @RequestParam: Pega o valor de um parâmetro da URL (a parte depois do "?").
        // Chama o serviço para buscar o usuário e o retorna no corpo da resposta HTTP 200 (OK).
        return ResponseEntity.ok(usuarioService.buscarUsuarioPorEmailUsuario(email));
    }

    // @DeleteMapping: Mapeia requisições HTTP do tipo DELETE.
    // Usado para DELETAR um recurso. URL: DELETE /usuario?email=exemplo@email.com
    @DeleteMapping
    public ResponseEntity<Void> deletarUsuarioPorEmail(@RequestParam String email) {
        usuarioService.deletarUsuarioPorEmail(email); // Chama o serviço para deletar o usuário.
        // Retorna uma resposta HTTP 200 (OK) sem corpo.
        return ResponseEntity.ok().build();
    }

    // @PutMapping: Mapeia requisições HTTP do tipo PUT.
    // Usado para ATUALIZAR um recurso existente. URL: PUT /usuario?id=1
    @PutMapping
    public ResponseEntity<Void> atualizarUsuarioPorId(@RequestParam Integer id, @RequestBody Usuario usuario) {
        // Recebe o 'id' pela URL e os novos dados do usuário ('usuario') pelo corpo da requisição.
        usuarioService.atualizarUsuarioPorId(id, usuario); // Chama o serviço para atualizar.
        // Retorna uma resposta HTTP 200 (OK) sem corpo.
        return ResponseEntity.ok().build();
    }
     @GetMapping("/todos")
    public ResponseEntity<List<Usuario>> listarTodos() {
        // 1. Chama o método do serviço que busca os usuários
        List<Usuario> usuarios = usuarioService.listarTodosUsuarios();

        // 2. Retorna a lista no corpo da resposta HTTP com o status "OK"
        return ResponseEntity.ok(usuarios);
    }
    
    @GetMapping("/info")
    public ResponseEntity<Usuario> getInfoUsuarioLogado(@AuthenticationPrincipal UserDetails userDetails) {
        // Se userDetails for nulo, significa que não há usuário logado
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // O "username" que configuramos no Spring Security é o email
        String email = userDetails.getUsername();
        
        // Buscamos o usuário completo no banco de dados
        Usuario usuario = usuarioService.buscarUsuarioPorEmailUsuario(email);

        // NUNCA retorne a senha para o frontend. Anule-a antes de enviar.
        usuario.setSenha(null);

        return ResponseEntity.ok(usuario);
    }
}