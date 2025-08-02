package com.example.sistema.demo.infrastructure.entitys;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "variantes_produto")
public class VarianteProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "sku", nullable = false, unique = true)
    private String sku;

    @Column(name = "preco")
    private BigDecimal preco;

    @Column(name = "estoque")
    private Integer estoque;

    @JsonBackReference
    @ManyToOne(optional = false)
    @JoinColumn(name = "produto_base_id", referencedColumnName = "id")
    private ProdutoBase produtoBase;

    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
      name = "configuracao_variante",
      joinColumns = @JoinColumn(name = "variante_id"),
      inverseJoinColumns = @JoinColumn(name = "valor_opcao_id"))
    private Set<ValorOpcao> configuracao = new HashSet<>();
}