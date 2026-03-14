package com.idealcomputer.api.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

// Representa o modelo de dados de placas-mãe dentro do motor de busca Elasticsearch.
// Esta classe é o coração da filtragem de compatibilidade no motor de busca,
// pois armazena os principais "elos" de ligação (soquete, RAM e formato) de forma otimizada para consultas.
@Data
@Document(indexName = "placasmae") // Define o índice específico para o armazenamento de placas-mãe no Elasticsearch.
public class PlacaMaeDocument {

    // Identificador exclusivo do documento no Elasticsearch.
    @Id
    private String id;

    // Referência ao ID original presente na tabela do PostgreSQL.
    // Permite que, após encontrar a placa ideal via busca rápida, o sistema recupere o registro oficial.
    @Field(type = FieldType.Long)
    private Long postgresId;

    // Nome comercial da placa-mãe configurado para buscas textuais (Ex: "TUF Gaming B550M").
    // O tipo 'Text' possibilita a localização do hardware através de termos parciais ou modelos.
    @Field(type = FieldType.Text)
    private String nome;

    // Padrão de encaixe do processador.
    // O tipo 'Keyword' é vital aqui para realizar o "match" exato com o processador no motor de busca.
    @Field(type = FieldType.Keyword)
    private String soqueteCpu;

    // Geração de memória aceita pela placa (Ex: "DDR4").
    // O tipo 'Keyword' garante que a filtragem por compatibilidade de RAM seja precisa e performática.
    @Field(type = FieldType.Keyword)
    private String tipoRamSuportado;

    // Tamanho físico da placa (Ex: "ATX", "Micro-ATX").
    // Utilizado para filtrar placas que caibam nos gabinetes retornados pelo Elasticsearch.
    @Field(type = FieldType.Keyword)
    private String formato;

    // Valor da placa-mãe para o controle de teto orçamentário da build.
    @Field(type = FieldType.Double)
    private Double preco;
}