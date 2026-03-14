package com.idealcomputer.api.repositories;

import com.idealcomputer.api.documents.ArmazenamentoDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

// Interface de acesso aos dados do Elasticsearch para a entidade Armazenamento.
// Resolve o problema de complexidade nas consultas de busca textual, permitindo
// realizar operações de indexação e pesquisa sem a necessidade de escrever queries manuais em JSON.
// Esta interface atua como o motor de busca rápida para o seu frontend Angular.
@Repository
public interface ArmazenamentoElasticRepository extends ElasticsearchRepository<ArmazenamentoDocument, String> {

    // Ao herdar de ElasticsearchRepository, esta interface já ganha automaticamente
    // todos os métodos de CRUD (save, delete, findById) voltados para o motor de busca.
}