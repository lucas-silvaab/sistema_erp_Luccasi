package com.example.sistema.demo.controller;

import com.example.sistema.demo.business.ProdutoService;
import com.example.sistema.demo.controller.dto.VarianteRequestDTO;
import com.example.sistema.demo.infrastructure.entitys.ProdutoBase;
import com.example.sistema.demo.infrastructure.entitys.VarianteProduto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api") // É uma boa prática versionar ou prefixar sua API
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;

    // Endpoint para criar um PRODUTO BASE (o pai)
    @PostMapping("/produtos-base")
    public ResponseEntity<ProdutoBase> criarProdutoBase(@RequestBody ProdutoBase produtoBase) {
        ProdutoBase novoProduto = produtoService.salvarProdutoBase(produtoBase);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoProduto);
    }

    // Endpoint para listar todos os PRODUTOS BASE
    @GetMapping("/produtos-base")
    public ResponseEntity<List<ProdutoBase>> listarProdutosBase() {
        return ResponseEntity.ok(produtoService.listarTodosProdutosBase());
    }

    // Endpoint para criar uma VARIANTE para um produto base existente
    @PostMapping("/produtos-base/{produtoBaseId}/variantes")
    public ResponseEntity<VarianteProduto> criarVariante(
            @PathVariable Integer produtoBaseId,
            @RequestBody VarianteRequestDTO varianteDTO) {
                
        VarianteProduto novaVariante = produtoService.criarVariante(produtoBaseId, varianteDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaVariante);
    }
}