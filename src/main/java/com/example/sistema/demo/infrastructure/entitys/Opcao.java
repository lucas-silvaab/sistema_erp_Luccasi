// Pacote da sua entidade
package com.example.sistema.demo.infrastructure.entitys;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "opcoes")
public class Opcao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Ex: "Cor do Tecido", "Tipo de Encosto", "Estrutura do Braço"
    @Column(name = "nome", nullable = false, unique = true)
    private String nome;
}