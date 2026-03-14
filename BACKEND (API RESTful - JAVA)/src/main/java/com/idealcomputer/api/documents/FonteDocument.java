package com.idealcomputer.api.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

// Representa o modelo de dados de fontes de alimentação dentro do motor de busca Elasticsearch.
// Esta classe permite que o sistema filtre rapidamente fontes por potência e preço,
// garantindo agilidade na geração de recomendações de hardware sem consultas diretas ao banco relacional.
@Data
@Document(indexName = "fontes") // Define o nome do índice (coleção) no Elasticsearch.
public class FonteDocument {

    // Identificador exclusivo do documento no Elasticsearch (String/UUID).
    @Id
    private String id;

    // Referência ao ID original da tabela no PostgreSQL.
    // Utilizado para recuperar a entidade completa após a filtragem no motor de busca.
    @Field(type = FieldType.Long)
    private Long postgresId;

    // Nome da fonte configurado para buscas textuais (Ex: "Corsair RM750").
    // O tipo 'Text' permite que o usuário encontre o produto buscando por termos parciais.
    @Field(type = FieldType.Text)
    private String nome;

    // Capacidade máxima de energia em Watts.
    // Campo fundamental para filtros de faixa (Range), garantindo que a fonte recomendada suporte o consumo da build.
    @Field(type = FieldType.Integer)
    private Integer potenciaWatts;

    // Padrão físico da fonte (Ex: "ATX", "SFX").
    // O tipo 'Keyword' é ideal para filtros exatos de compatibilidade com o gabinete.
    @Field(type = FieldType.Keyword)
    private String formato;

    // Valor do componente utilizado para ordenação e controle de teto orçamentário.
    @Field(type = FieldType.Double)
    private Double preco;
}