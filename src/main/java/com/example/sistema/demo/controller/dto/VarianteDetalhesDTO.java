package com.example.sistema.demo.controller.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class VarianteDetalhesDTO {
    private Integer id;
    private String sku;
    private BigDecimal preco;
    private Integer estoque;
    private ProdutoBaseSimplesDTO produtoBase; // Usa o DTO simples
    private List<ValorOpcaoDetalhesDTO> configuracao; // Usa o DTO de detalhes da opção
}