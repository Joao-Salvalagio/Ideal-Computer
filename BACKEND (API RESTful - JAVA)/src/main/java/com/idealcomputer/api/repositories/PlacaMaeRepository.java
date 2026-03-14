package com.idealcomputer.api.repositories;

import com.idealcomputer.api.models.PlacaMaeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Interface de acesso a dados (DAO) para a entidade Placa-Mãe no banco relacional.
// Resolve o problema de persistência do "componente central" da build,
// assegurando que os vínculos de compatibilidade entre CPU, RAM e Gabinete
// sejam validados através de chaves estrangeiras íntegras no PostgreSQL.
@Repository
public interface PlacaMaeRepository extends JpaRepository<PlacaMaeModel, Long> {

    // Utiliza o Spring Data JPA para gerenciar o ciclo de vida das placas-mãe.
    // Enquanto o repositório Elastic foca na agilidade de encontrar a placa compatível,
    // este repositório garante que o registro oficial contenha todas as especificações
    // técnicas necessárias para o fechamento de um pedido ou montagem.
}