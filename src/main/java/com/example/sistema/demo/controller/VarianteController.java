package com.example.sistema.demo.controller;

import com.example.sistema.demo.business.ProdutoService;
import com.example.sistema.demo.controller.dto.VarianteDetalhesDTO; // MUDOU AQUI
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/variantes")
@RequiredArgsConstructor
public class VarianteController {

    private final ProdutoService produtoService;

    @GetMapping("/{id}")
    public ResponseEntity<VarianteDetalhesDTO> buscarVariantePorId(@PathVariable Integer id) { // MUDOU AQUI
        return produtoService.buscarVariantePorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}