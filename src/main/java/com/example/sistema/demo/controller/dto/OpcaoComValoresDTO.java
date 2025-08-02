package com.example.sistema.demo.controller.dto;

import lombok.Data;
import java.util.List;

@Data
public class OpcaoComValoresDTO {
    private Integer id;
    private String nome;
    private List<ValorOpcaoDTO> valores;
}