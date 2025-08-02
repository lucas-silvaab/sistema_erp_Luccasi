package com.example.sistema.demo.infrastructure.repository;

import com.example.sistema.demo.infrastructure.entitys.ValorOpcao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import com.example.sistema.demo.infrastructure.entitys.Opcao;

@Repository
public interface ValorOpcaoRepository extends JpaRepository<ValorOpcao, Integer> {
    List<ValorOpcao> findByOpcao(Opcao opcao); // Adicione este método
}