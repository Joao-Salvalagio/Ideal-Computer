package com.idealcomputer.api.repositories;

import com.idealcomputer.api.documents.RefrigeracaoDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

// Interface de acesso aos dados de sistemas de refrigeração no Elasticsearch.
// Resolve o problema de busca por compatibilidade de montagem, permitindo que o
// sistema filtre Air Coolers ou Water Coolers que suportem o soquete da CPU escolhida.
@Repository
public interface RefrigeracaoElasticRepository extends ElasticsearchRepository<RefrigeracaoDocument, String> {

    // Ao herdar de ElasticsearchRepository, esta interface habilita buscas performáticas
    // por tipo de refrigeração e compatibilidade física, otimizando o fluxo de
    // recomendação de hardware sem sobrecarregar o banco relacional.
}