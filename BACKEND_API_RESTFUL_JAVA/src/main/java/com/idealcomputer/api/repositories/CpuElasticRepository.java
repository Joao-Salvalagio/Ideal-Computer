package com.idealcomputer.api.repositories;

import com.idealcomputer.api.documents.CpuDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Interface de acesso aos dados de processadores no Elasticsearch.
// Resolve o problema de filtragem técnica e busca textual de alto desempenho,
// permitindo que o sistema localize CPUs compatíveis com base em critérios de
// encaixe físico (soquete) e restrições financeiras.
@Repository
public interface CpuElasticRepository extends ElasticsearchRepository<CpuDocument, String> {

    // Recupera uma lista de processadores que atendem simultaneamente ao critério de
    // compatibilidade de soquete e ao teto orçamentário definido.
    // Essencial para o motor de recomendações validar a build de acordo com a placa-mãe escolhida.
    List<CpuDocument> findBySoqueteAndPrecoLessThanEqual(String soquete, Double precoMax);

    // Realiza uma busca textual otimizada no índice de CPUs.
    // O uso de 'Containing' permite que o Elasticsearch identifique modelos mesmo que o usuário
    // digite apenas fragmentos do nome (Ex: "13700" para encontrar "Core i7-13700K").
    List<CpuDocument> findByNomeContaining(String nome);
}