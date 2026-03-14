package com.idealcomputer.api.repositories;

import com.idealcomputer.api.models.GabineteModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Interface de acesso a dados (DAO) para a entidade Gabinete no banco relacional.
// Resolve o problema de armazenamento definitivo da estrutura física da build,
// garantindo que as especificações de compatibilidade com placas-mãe sejam
// mantidas com integridade referencial no PostgreSQL.
@Repository
public interface GabineteRepository extends JpaRepository<GabineteModel, Long> {

    // Utiliza o Spring Data JPA para fornecer as operações de persistência necessárias.
    // Enquanto o GabineteElasticRepository lida com a experiência de busca e filtros,
    // este repositório é responsável por manter a "fonte da verdade" para o estoque de hardware.
}