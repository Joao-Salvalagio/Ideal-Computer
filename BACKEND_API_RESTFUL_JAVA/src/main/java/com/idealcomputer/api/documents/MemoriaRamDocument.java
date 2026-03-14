package com.idealcomputer.api.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

// Representa o modelo de dados de memória RAM dentro do motor de busca Elasticsearch.
// Esta estrutura permite filtragens ultrarrápidas por geração (DDR) e capacidade,
// otimizando a seleção de hardware compatível sem a necessidade de varreduras no banco relacional.
@Data
@Document(indexName = "memoriasram") // Define o índice específico para o armazenamento de memórias no Elasticsearch.
public class MemoriaRamDocument {

    // Identificador exclusivo do documento no Elasticsearch.
    @Id
    private String id;

    // Referência ao ID original presente na tabela do PostgreSQL.
    // Utilizado para mapear o resultado da busca rápida de volta para a entidade oficial de banco de dados.
    @Field(type = FieldType.Long)
    private Long postgresId;

    // Nome comercial da memória RAM configurado para buscas textuais (Ex: "Fury Beast").
    // O tipo 'Text' permite que o Elasticsearch identifique o componente através de palavras parciais.
    @Field(type = FieldType.Text)
    private String nome;

    // Geração da tecnologia de memória (Ex: "DDR4", "DDR5").
    // O tipo 'Keyword' é crucial aqui para garantir filtros exatos de compatibilidade com a placa-mãe.
    @Field(type = FieldType.Keyword)
    private String tipo;

    // Espaço disponível em Gigabytes.
    // Utilizado para filtros de performance e para atingir os requisitos mínimos de sistema na montagem.
    @Field(type = FieldType.Integer)
    private Integer capacidadeGb;

    // Valor do componente para cálculos de orçamento e classificação por custo.
    @Field(type = FieldType.Double)
    private Double preco;
}