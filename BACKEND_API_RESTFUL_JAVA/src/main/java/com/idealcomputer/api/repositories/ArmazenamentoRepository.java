package com.idealcomputer.api.repositories;

import com.idealcomputer.api.models.ArmazenamentoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Interface de acesso a dados (DAO) para a entidade Armazenamento no banco relacional.
// Resolve o problema de persistência de dados, permitindo que a aplicação realize
// operações de CRUD (Criar, Ler, Atualizar e Deletar) no PostgreSQL de forma abstrata.
// Esta classe é a "fonte da verdade" para os dados de hardware no sistema.
@Repository
public interface ArmazenamentoRepository extends JpaRepository<ArmazenamentoModel, Long> {

    // Ao herdar de JpaRepository, a interface herda automaticamente métodos como
    // save(), findAll(), findById() e delete(), eliminando a necessidade de escrever SQL manualmente.
}