package com.example.sistema.demo.business;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.sistema.demo.infrastructure.entitys.Usuario;
import com.example.sistema.demo.infrastructure.repository.UsuarioRepository;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public void salvarUsuario(Usuario usuario) {
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        repository.saveAndFlush(usuario);
    }

    public Usuario buscarUsuarioPorEmailUsuario(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email não encontrado!"));
    }

    public void deletarUsuarioPorEmail(String email) {
        repository.deleteByEmail(email);
    }

    // Método para atualizar (editar) os dados de um usuário existente, identificado pelo seu ID.
    public void atualizarUsuarioPorId(Integer id, Usuario usuarioRecebido) {
        // 1. Busca o usuário existente no banco de dados
        Usuario usuarioExistente = repository.findById(id).orElseThrow(() ->
                new RuntimeException("Id não encontrado"));

        // 2. Atualiza os campos do USUÁRIO EXISTENTE com os dados recebidos.
        //    Sempre atualize o nome e o email, pois eles vêm do formulário de edição.
        usuarioExistente.setNome(usuarioRecebido.getNome());
        usuarioExistente.setEmail(usuarioRecebido.getEmail());

        repository.saveAndFlush(usuarioExistente);
    }

    public List<Usuario> listarTodosUsuarios() {
        return repository.findAll();
    }
}