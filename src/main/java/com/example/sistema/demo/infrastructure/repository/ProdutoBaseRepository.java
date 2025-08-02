package com.example.sistema.demo.infrastructure.repository;

import com.example.sistema.demo.infrastructure.entitys.ProdutoBase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoBaseRepository extends JpaRepository<ProdutoBase, Integer> {
}