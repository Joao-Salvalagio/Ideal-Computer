package com.idealcomputer.api.repositories;

import com.idealcomputer.api.documents.PlacaMaeDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Interface de acesso aos dados de placas-mãe no Elasticsearch.
// Resolve o problema de filtragem por arquitetura, permitindo que o sistema localize
// rapidamente placas compatíveis com o soquete do processador selecionado,
// garantindo o funcionamento físico da build.
@Repository
public interface PlacaMaeElasticRepository extends ElasticsearchRepository<PlacaMaeDocument, String> {

    // Recupera uma lista de placas-mãe com base no padrão de encaixe do processador (Ex: "AM4", "LGA1700").
    // O uso de tipos 'Keyword' no mapeamento do documento garante que a busca seja exata,
    // evitando erros de compatibilidade entre gerações diferentes de hardware.
    List<PlacaMaeDocument> findBySoqueteCpu(String soqueteCpu);
}