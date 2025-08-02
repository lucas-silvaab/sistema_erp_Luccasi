package com.example.sistema.demo.controller.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
public class VarianteRequestDTO {
    private String sku;
    private Set<Integer> valoresOpcaoIds; // Apenas sku e as opções
}