package com.idealcomputer.api.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

// Representa o modelo de dados de gabinetes dentro do motor de busca Elasticsearch.
// Esta estrutura permite buscas rápidas por nome e filtragem por compatibilidade física,
// otimizando o processo de recomendação de hardware sem a necessidade de consultas constantes ao PostgreSQL.
@Data
@Document(indexName = "gabinetes") // Define o índice (coleção) específico para gabinetes no Elasticsearch.
public class GabineteDocument {

    // Identificador exclusivo do documento no Elasticsearch.
    @Id
    private String id;

    // Referência ao ID original da tabela no banco de dados relacional.
    // Essencial para recuperar a entidade completa (GabineteModel) após a filtragem no motor de busca.
    @Field(type = FieldType.Long)
    private Long postgresId;

    // Nome comercial do gabinete configurado para buscas textuais (Ex: "NZXT H5 Flow").
    // O tipo 'Text' permite que o usuário encontre o produto através de termos parciais ou aproximados.
    @Field(type = FieldType.Text)
    private String nome;

    // Lista de padrões de placa-mãe aceitos, configurada para busca textual.
    // Permite que o motor de busca valide rapidamente se uma placa-mãe (ex: Micro-ATX) cabe no gabinete.
    @Field(type = FieldType.Text)
    private String formatosPlacaMaeSuportados;

    // Valor do componente utilizado para cálculos de teto orçamentário e ordenação por preço.
    @Field(type = FieldType.Double)
    private Double preco;
}