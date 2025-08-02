package com.example.sistema.demo.infrastructure.repository;

import com.example.sistema.demo.infrastructure.entitys.Opcao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpcaoRepository extends JpaRepository<Opcao, Integer> {
}