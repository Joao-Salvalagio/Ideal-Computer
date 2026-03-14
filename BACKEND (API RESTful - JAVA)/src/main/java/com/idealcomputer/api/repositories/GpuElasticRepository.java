package com.idealcomputer.api.repositories;

import com.idealcomputer.api.documents.GpuDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

// Interface de acesso aos dados de placas de vídeo (GPUs) no Elasticsearch.
// Resolve o problema de busca e filtragem por desempenho gráfico e custo,
// permitindo que o sistema localize rapidamente componentes com base na
// capacidade de VRAM e requisitos de consumo energético.
@Repository
public interface GpuElasticRepository extends ElasticsearchRepository<GpuDocument, String> {

    // Ao herdar de ElasticsearchRepository, esta interface habilita consultas
    // de alta performance no índice "gpus", facilitando a criação de filtros
    // dinâmicos para o usuário final no frontend Angular.
}