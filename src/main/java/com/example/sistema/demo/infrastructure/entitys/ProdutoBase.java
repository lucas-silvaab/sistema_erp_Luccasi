package com.example.sistema.demo.infrastructure.entitys;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "produtos_base")
public class ProdutoBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "codigo_base", nullable = false, unique = true)
    private String codigoBase;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "detalhes_descricao", columnDefinition = "TEXT")
    private String detalhesDescricao;

    @Column(name = "categoria")
    private String categoria;

    @Column(name = "sub_categoria")
    private String subCategoria;

    @JsonManagedReference
    @OneToMany(mappedBy = "produtoBase", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<VarianteProduto> variantes;
}