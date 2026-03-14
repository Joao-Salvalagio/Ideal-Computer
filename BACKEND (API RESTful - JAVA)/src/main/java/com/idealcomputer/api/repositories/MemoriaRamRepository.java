package com.idealcomputer.api.repositories;

import com.idealcomputer.api.models.MemoriaRamModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Interface de acesso a dados (DAO) para a entidade Memória RAM no banco relacional.
// Resolve o problema de persistência e gestão de estoque de módulos de memória,
// assegurando que os dados de geração (DDR) e frequência (MHz) estejam
// disponíveis para garantir a compatibilidade técnica com a placa-mãe.
@Repository
public interface MemoriaRamRepository extends JpaRepository<MemoriaRamModel, Long> {

    // Utiliza o Spring Data JPA para prover operações de CRUD no PostgreSQL.
    // Enquanto o MemoriaRamElasticRepository agiliza a busca por tipo e preço,
    // este repositório é a fonte oficial para o fechamento e registro das Builds.
}