// Define o pacote onde esta interface está localizada.
// Este pacote ('infrastructure.repository') sugere que a classe faz parte da camada de infraestrutura,
// responsável pela comunicação com sistemas externos, como o banco de dados.
package com.example.sistema.demo.infrastructure.repository;

// Importa a classe Optional do Java. 'Optional' é um container que pode ou não conter um valor nulo.
// É usado para evitar NullPointerExceptions, tornando o código mais seguro e legível.
import java.util.Optional;

// Importa a interface JpaRepository do Spring Data JPA.
// Ela fornece métodos prontos para operações CRUD (Create, Read, Update, Delete) no banco de dados.
import org.springframework.data.jpa.repository.JpaRepository;

// Importa a anotação @Transactional do Spring.
// Esta anotação é usada para gerenciar transações com o banco de dados.
import org.springframework.transaction.annotation.Transactional;

// Importa a classe de entidade 'Usuario'.
import com.example.sistema.demo.infrastructure.entitys.Usuario;

// Declaração da interface 'UsuarioRepository'.
// Interfaces em Java definem um "contrato" de métodos que uma classe deve implementar.
// No Spring Data JPA, você não precisa criar a implementação; o framework a gera automaticamente.
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
// A interface estende 'JpaRepository', o que significa que ela herda todos os métodos CRUD padrão.
// <Usuario, Integer> são os parâmetros genéricos:
//   - 'Usuario': A classe de entidade que este repositório gerencia.
//   - 'Integer': O tipo da chave primária (ID) da entidade 'Usuario'.

    // Declaração de um método de consulta customizado.
    // O Spring Data JPA interpreta o nome do método e cria a consulta automaticamente (Query Method).
    // "findByEmail" será traduzido para uma consulta SQL como: SELECT * FROM usuario WHERE email = ?
    // Retorna um 'Optional<Usuario>' para tratar de forma segura o caso em que nenhum usuário com o email fornecido é encontrado.
    Optional<Usuario> findByEmail(String email);

    // A anotação @Transactional indica que este método deve ser executado dentro de uma transação do banco de dados.
    // Se ocorrer um erro durante a execução, a transação fará um "rollback", desfazendo qualquer alteração no banco de dados.
    // É necessária para operações de modificação como delete ou update.
    @Transactional
    // Declaração de um método para deletar um usuário pelo email.
    // Assim como 'findByEmail', o Spring Data JPA criará a consulta de exclusão baseada no nome do método.
    // "deletarByEmail" será traduzido para uma instrução como: DELETE FROM usuario WHERE email = ?
    void deleteByEmail(String email);
}