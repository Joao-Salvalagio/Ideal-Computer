package com.idealcomputer.api.repositories;

import com.idealcomputer.api.models.GpuModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Interface de acesso a dados (DAO) para a entidade GPU no banco relacional.
// Resolve o problema de persistência das unidades de processamento gráfico,
// garantindo a integridade dos dados de consumo (Watts) e memória (VRAM)
// necessários para validar o equilíbrio técnico da build.
@Repository
public interface GpuRepository extends JpaRepository<GpuModel, Long> {

    // Utiliza o Spring Data JPA para gerenciar o ciclo de vida das placas de vídeo.
    // Enquanto o GpuElasticRepository provê velocidade na busca por performance,
    // este repositório assegura que a build salva aponte para um hardware
    // validado e existente na base de dados principal.
}