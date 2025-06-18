// Define o pacote onde esta classe de entidade está localizada.
// O pacote 'infrastructure.entitys' indica que esta classe faz parte da camada de infraestrutura
// e representa uma entidade do banco de dados (um modelo de dados).
package com.example.sistema.demo.infrastructure.entitys;

// Importa todas as anotações necessárias da especificação Jakarta Persistence API (JPA).
// JPA é uma especificação Java que descreve como gerenciar dados relacionais em aplicações Java.
import jakarta.persistence.*;
// Importa anotações da biblioteca Lombok.
// Lombok é uma biblioteca que ajuda a reduzir a quantidade de código clichê (boilerplate)
// como getters, setters, construtores, etc., gerando-os automaticamente em tempo de compilação.
import lombok.*;

// --- Anotações do Lombok ---

// @Getter: Gera automaticamente os métodos 'get' para todos os campos da classe.
// Ex: public Integer getId() { ... }, public String getEmail() { ... }
@Getter
// @Setter: Gera automaticamente os métodos 'set' para todos os campos da classe.
// Ex: public void setId(Integer id) { ... }, public void setEmail(String email) { ... }
@Setter
// @AllArgsConstructor: Gera um construtor que aceita todos os campos como argumentos.
// Ex: public Usuario(Integer id, String email, String nome) { ... }
@AllArgsConstructor
// @NoArgsConstructor: Gera um construtor vazio (sem argumentos).
// JPA exige um construtor sem argumentos para criar instâncias da entidade.
// Ex: public Usuario() { ... }
@NoArgsConstructor
// @Builder: Aplica o padrão de projeto "Builder".
// Facilita a criação de objetos de forma mais legível e flexível.
// Ex: Usuario.builder().email("teste@email.com").nome("Teste").build();
@Builder

// --- Anotações do Jakarta Persistence (JPA) ---

// @Table: Especifica o nome da tabela no banco de dados que esta classe irá mapear.
// Se não for especificado, o JPA usaria o nome da classe ("Usuario") como nome da tabela por padrão.
@Table(name = "usuario")
// @Entity: Marca esta classe como uma entidade JPA.
// Isso informa ao provedor de persistência (como o Hibernate) que objetos desta classe
// podem ser persistidos no banco de dados.
@Entity
public class Usuario {

    // @Id: Marca o campo 'id' como a chave primária (primary key) da tabela.
    // Toda entidade JPA deve ter uma chave primária.
    @Id
    // @GeneratedValue: Configura a estratégia de geração automática da chave primária.
    // 'strategy = GenerationType.AUTO' deixa o provedor JPA (Hibernate) decidir a melhor estratégia
    // (pode ser uma sequência, uma coluna de auto incremento, etc.), dependendo do banco de dados usado.
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    // @Column: Mapeia este campo para uma coluna na tabela do banco de dados.
    // 'name = "email"': Define o nome da coluna como "email".
    // 'unique = true': Adiciona uma restrição (constraint) de unicidade a esta coluna,
    // garantindo que não pode haver dois usuários com o mesmo email no banco de dados.
    @Column(name = "email", unique = true)
    private String email;

    // @Column: Mapeia o campo 'nome' para a coluna "nome" na tabela.
    // Como o nome do atributo é o mesmo da coluna, '@Column(name = "nome")' é opcional aqui,
    // mas é uma boa prática para clareza.
    @Column(name = "nome")
    private String nome;
    @Column(name = "senha")
    private String senha;
}