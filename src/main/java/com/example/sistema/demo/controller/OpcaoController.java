package com.example.sistema.demo.controller;

import com.example.sistema.demo.controller.dto.OpcaoComValoresDTO;
import com.example.sistema.demo.controller.dto.ValorOpcaoDTO;
import com.example.sistema.demo.infrastructure.entitys.Opcao;
import com.example.sistema.demo.infrastructure.entitys.ValorOpcao;
import com.example.sistema.demo.infrastructure.repository.OpcaoRepository;
import com.example.sistema.demo.infrastructure.repository.ValorOpcaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/opcoes")
@RequiredArgsConstructor
public class OpcaoController {

    private final OpcaoRepository opcaoRepository;
    private final ValorOpcaoRepository valorOpcaoRepository;

    @GetMapping
    public ResponseEntity<List<OpcaoComValoresDTO>> listarTodasAsOpcoesComValores() {
        List<Opcao> opcoes = opcaoRepository.findAll();
        List<OpcaoComValoresDTO> dtos = opcoes.stream().map(this::convertToDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<OpcaoComValoresDTO> criarOpcao(@RequestBody Map<String, String> payload) {
        Opcao novaOpcao = new Opcao();
        novaOpcao.setNome(payload.get("nome"));
        Opcao opcaoSalva = opcaoRepository.save(novaOpcao);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(opcaoSalva));
    }

    @PostMapping("/{opcaoId}/valores")
    public ResponseEntity<ValorOpcaoDTO> adicionarValor(
            @PathVariable Integer opcaoId,
            @RequestBody Map<String, String> payload) {
        
        return opcaoRepository.findById(opcaoId).map(opcao -> {
            ValorOpcao novoValor = new ValorOpcao();
            novoValor.setOpcao(opcao);
            novoValor.setValor(payload.get("valor"));
            ValorOpcao valorSalvo = valorOpcaoRepository.save(novoValor);

            ValorOpcaoDTO dto = new ValorOpcaoDTO();
            dto.setId(valorSalvo.getId());
            dto.setValor(valorSalvo.getValor());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        }).orElse(ResponseEntity.notFound().build());
    }

    private OpcaoComValoresDTO convertToDto(Opcao opcao) {
        OpcaoComValoresDTO dto = new OpcaoComValoresDTO();
        dto.setId(opcao.getId());
        dto.setNome(opcao.getNome());
        
        List<ValorOpcaoDTO> valoresDto = valorOpcaoRepository.findByOpcao(opcao).stream()
            .map(valorOpcao -> {
                ValorOpcaoDTO valorDto = new ValorOpcaoDTO();
                valorDto.setId(valorOpcao.getId());
                valorDto.setValor(valorOpcao.getValor());
                return valorDto;
            }).collect(Collectors.toList());
        
        dto.setValores(valoresDto);
        return dto;
    }
}