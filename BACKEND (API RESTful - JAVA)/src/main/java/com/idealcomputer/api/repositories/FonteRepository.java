package com.idealcomputer.api.repositories;

import com.idealcomputer.api.models.FonteModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Interface de acesso a dados (DAO) para a entidade Fonte no banco relacional.
// Resolve o problema de armazenamento definitivo e integridade das especificações de energia,
// sendo a fonte primária de dados para o cadastro e gestão de estoque de PSUs (Power Supply Units).
@Repository
public interface FonteRepository extends JpaRepository<FonteModel, Long> {

    // Utiliza o Spring Data JPA para gerenciar o ciclo de vida das fontes no PostgreSQL.
    // Enquanto o repositório Elastic foca na experiência de busca do usuário, este
    // garante que cada Build salva esteja vinculada a um registro de hardware válido e persistente.
}