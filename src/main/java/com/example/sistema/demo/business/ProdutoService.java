package com.example.sistema.demo.business;

import com.example.sistema.demo.controller.dto.ProdutoBaseSimplesDTO;
import com.example.sistema.demo.controller.dto.ValorOpcaoDetalhesDTO;
import com.example.sistema.demo.controller.dto.VarianteDetalhesDTO;
import com.example.sistema.demo.controller.dto.VarianteRequestDTO;
import com.example.sistema.demo.infrastructure.entitys.*;
import com.example.sistema.demo.infrastructure.repository.ProdutoBaseRepository;
import com.example.sistema.demo.infrastructure.repository.ValorOpcaoRepository;
import com.example.sistema.demo.infrastructure.repository.VarianteProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal; // Importe BigDecimal
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoBaseRepository produtoBaseRepository;
    private final VarianteProdutoRepository varianteProdutoRepository;
    private final ValorOpcaoRepository valorOpcaoRepository;

    @Transactional
    public ProdutoBase salvarProdutoBase(ProdutoBase produtoBase) {
        return produtoBaseRepository.save(produtoBase);
    }

    public List<ProdutoBase> listarTodosProdutosBase() {
        return produtoBaseRepository.findAll();
    }
    
    @Transactional
    public VarianteProduto criarVariante(Integer produtoBaseId, VarianteRequestDTO dto) {
        ProdutoBase produtoBase = produtoBaseRepository.findById(produtoBaseId)
                .orElseThrow(() -> new RuntimeException("Produto Base não encontrado!"));

        Set<ValorOpcao> configuracao = new HashSet<>(valorOpcaoRepository.findAllById(dto.getValoresOpcaoIds()));
        if (configuracao.size() != dto.getValoresOpcaoIds().size()){
            throw new RuntimeException("Um ou mais IDs de valores de opção são inválidos.");
        }
        
        VarianteProduto novaVariante = VarianteProduto.builder()
                .produtoBase(produtoBase)
                .sku(dto.getSku())
                .preco(BigDecimal.ZERO) // Definido como 0 por padrão
                .estoque(0)             // Definido como 0 por padrão
                .configuracao(configuracao)
                .build();
        
        return varianteProdutoRepository.save(novaVariante);
    }

    public Optional<VarianteDetalhesDTO> buscarVariantePorId(Integer id) {
        Optional<VarianteProduto> varianteOpt = varianteProdutoRepository.findById(id);
        if (varianteOpt.isEmpty()) {
            return Optional.empty();
        }
        VarianteProduto variante = varianteOpt.get();
        VarianteDetalhesDTO dto = new VarianteDetalhesDTO();
        dto.setId(variante.getId());
        dto.setSku(variante.getSku());
        dto.setPreco(variante.getPreco());
        dto.setEstoque(variante.getEstoque());
        ProdutoBaseSimplesDTO pbDto = new ProdutoBaseSimplesDTO();
        pbDto.setId(variante.getProdutoBase().getId());
        pbDto.setNome(variante.getProdutoBase().getNome());
        dto.setProdutoBase(pbDto);
        List<ValorOpcaoDetalhesDTO> configDto = variante.getConfiguracao().stream().map(config -> {
            ValorOpcaoDetalhesDTO vDto = new ValorOpcaoDetalhesDTO();
            vDto.setOpcaoNome(config.getOpcao().getNome());
            vDto.setValor(config.getValor());
            return vDto;
        }).collect(Collectors.toList());
        dto.setConfiguracao(configDto);
        return Optional.of(dto);
    }
}