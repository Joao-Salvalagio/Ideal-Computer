package com.idealcomputer.api.repositories;

import com.idealcomputer.api.documents.FonteDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

// Interface de acesso aos dados de fontes de alimentação no Elasticsearch.
// Resolve o problema de busca e filtragem dinâmica por potência e formato,
// permitindo que o motor de recomendações localize rapidamente unidades de energia
// que atendam aos requisitos de consumo (Watts) da build atual.
@Repository
public interface FonteElasticRepository extends ElasticsearchRepository<FonteDocument, String> {

    // Ao herdar de ElasticsearchRepository, esta interface permite buscas performáticas
    // no índice "fontes", facilitando a validação de compatibilidade física com o
    // gabinete e energética com os componentes internos.
}