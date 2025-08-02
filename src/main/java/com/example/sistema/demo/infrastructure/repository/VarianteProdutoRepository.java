package com.example.sistema.demo.infrastructure.repository;

import com.example.sistema.demo.infrastructure.entitys.VarianteProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface VarianteProdutoRepository extends JpaRepository<VarianteProduto, Integer> {
    Optional<VarianteProduto> findBySku(String sku);
}