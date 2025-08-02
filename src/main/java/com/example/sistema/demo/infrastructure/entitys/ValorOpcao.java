// Pacote da sua entidade
package com.example.sistema.demo.infrastructure.entitys;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "valores_opcao")
public class ValorOpcao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // A qual opção este valor pertence (Ex: "Cor do Tecido")
    @ManyToOne(optional = false)
    @JoinColumn(name = "opcao_id", referencedColumnName = "id")
    private Opcao opcao;

    // Ex: "Azul Marinho", "Telinha", "Com Braço"
    @Column(name = "valor", nullable = false)
    private String valor;
}