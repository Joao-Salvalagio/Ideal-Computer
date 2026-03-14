package com.idealcomputer.api.repositories;

import com.idealcomputer.api.models.RefrigeracaoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Interface de acesso a dados (DAO) para a entidade Refrigeração no banco relacional.
// Resolve o problema de persistência definitiva dos sistemas de arrefecimento,
// garantindo que as especificações técnicas de dissipação térmica e dimensões
// estejam disponíveis para a validação da integridade da build.
@Repository
public interface RefrigeracaoRepository extends JpaRepository<RefrigeracaoModel, Long> {

    // Utiliza o Spring Data JPA para gerenciar o ciclo de vida dos componentes de refrigeração.
    // Garante que, ao salvar uma build, o sistema tenha uma referência sólida para um hardware
    // existente no banco de dados principal.
}