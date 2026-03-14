package com.idealcomputer.api.repositories;

import com.idealcomputer.api.documents.MemoriaRamDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Interface de acesso aos dados de memória RAM no Elasticsearch.
// Resolve o problema de filtragem por compatibilidade de geração (DDR) e capacidade,
// permitindo que o sistema ofereça apenas pentes de memória que correspondam
// tecnicamente às especificações da placa-mãe selecionada.
@Repository
public interface MemoriaRamElasticRepository extends ElasticsearchRepository<MemoriaRamDocument, String> {

    // Recupera uma lista de memórias filtradas pelo padrão tecnológico (Ex: "DDR4", "DDR5").
    // O tipo 'Keyword' no documento permite que o Elasticsearch realize este filtro
    // de forma exata e extremamente veloz durante a montagem da build.
    List<MemoriaRamDocument> findByTipo(String tipo);
}