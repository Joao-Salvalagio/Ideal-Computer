package com.idealcomputer.api.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

// Representa o modelo de dados armazenado no motor de busca Elasticsearch.
// Esta classe é um espelho otimizado da entidade ArmazenamentoModel, permitindo
// que a API realize buscas complexas e filtragens de hardware com alta performance,
// sem sobrecarregar o banco de dados principal.
@Data
@Document(indexName = "armazenamentos") // Define o nome do "índice" (equivalente à tabela) no Elasticsearch.
public class ArmazenamentoDocument {

    // Identificador único do documento dentro do Elasticsearch (geralmente uma String/UUID).
    @Id
    private String id;

    // Guarda o ID original do banco de dados relacional (PostgreSQL).
    // Fundamental para manter o vínculo entre o motor de busca e a fonte da verdade,
    // permitindo localizar a entidade original caso uma operação de escrita seja necessária.
    @Field(type = FieldType.Long)
    private Long postgresId;

    // Nome do componente configurado para busca textual.
    // O tipo 'Text' permite que o Elasticsearch realize buscas por termos parciais ou aproximados.
    @Field(type = FieldType.Text)
    private String nome;

    // Tipo de tecnologia (Ex: SSD, HDD).
    // O tipo 'Keyword' é otimizado para filtros exatos e agrupamentos, sem processamento de texto.
    @Field(type = FieldType.Keyword)
    private String tipo;

    // Espaço disponível em Gigabytes. Utilizado para filtros de faixa de valores (Range).
    @Field(type = FieldType.Integer)
    private Integer capacidadeGb;

    // Valor do componente. Utilizado para ordenação e filtros de orçamento nas recomendações.
    @Field(type = FieldType.Double)
    private Double preco;
}