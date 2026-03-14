package com.idealcomputer.api.repositories;

import com.idealcomputer.api.models.CpuModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Interface de acesso a dados (DAO) para a entidade CPU no banco relacional.
// Resolve o problema de persistência e gerenciamento do ciclo de vida dos processadores,
// garantindo que as informações técnicas e preços oficiais estejam armazenados de forma íntegra.
@Repository
public interface CpuRepository extends JpaRepository<CpuModel, Long> {

    // Utiliza o Spring Data JPA para fornecer operações fundamentais de escrita e leitura.
    // Enquanto o CpuElasticRepository foca na busca, este repositório é utilizado para
    // cadastros, atualizações de estoque e referências de chaves estrangeiras.
}