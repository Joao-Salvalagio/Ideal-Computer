package com.idealcomputer.api.repositories;

import com.idealcomputer.api.documents.GabineteDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

// Interface de acesso aos dados de gabinetes no Elasticsearch.
// Resolve o problema de busca por compatibilidade física e estética, permitindo
// que o sistema localize modelos que suportem os formatos de placa-mãe (ATX, ITX, etc.)
// e caibam no orçamento de forma extremamente performática.
@Repository
public interface GabineteElasticRepository extends ElasticsearchRepository<GabineteDocument, String> {

    // Ao herdar de ElasticsearchRepository, esta interface habilita consultas
    // complexas sobre o índice "gabinetes", facilitando a filtragem por dimensões
    // e suporte a componentes internos sem onerar o banco de dados relacional.
}