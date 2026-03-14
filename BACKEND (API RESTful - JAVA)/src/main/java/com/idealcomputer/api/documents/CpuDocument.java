package com.idealcomputer.api.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

// Representa o modelo de dados de processadores (CPUs) dentro do motor de busca Elasticsearch.
// Esta classe otimiza a localização de componentes através de buscas textuais e filtros técnicos,
// evitando consultas pesadas diretamente no banco de dados relacional durante a navegação.
@Data
@Document(indexName = "cpus") // Define o índice (tabela) onde as CPUs serão indexadas no Elastic.
public class CpuDocument {

    // Identificador exclusivo do documento no Elasticsearch.
    @Id
    private String id;

    // Referência direta ao identificador original do banco de dados PostgreSQL.
    // Essencial para vincular o resultado da busca rápida com a entidade completa do banco.
    @Field(type = FieldType.Long)
    private Long postgresId;

    // Nome do processador configurado para busca semântica.
    // O tipo 'Text' permite encontrar o hardware mesmo digitando apenas partes do nome (Ex: "Ryzen").
    @Field(type = FieldType.Text)
    private String nome;

    // Padrão de encaixe físico do processador.
    // O tipo 'Keyword' é usado para filtros exatos e rápidos, garantindo que apenas
    // peças com o soquete idêntico sejam retornadas (Ex: "AM5").
    @Field(type = FieldType.Keyword)
    private String soquete;

    // Preço atualizado do processador para cálculos e filtros de faixa orçamentária.
    @Field(type = FieldType.Double)
    private Double preco;

    // Consumo energético registrado para validar a compatibilidade com fontes de alimentação.
    @Field(type = FieldType.Integer)
    private Integer potenciaRecomendadaW;
}