package com.idealcomputer.api.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

// Representa o modelo de dados de placas de vídeo (GPUs) dentro do motor de busca Elasticsearch.
// Esta estrutura é otimizada para consultas de alto desempenho, permitindo que o sistema
// localize GPUs por nome, capacidade de memória e exigência energética de forma quase instantânea.
@Data
@Document(indexName = "gpus") // Define o índice específico para o armazenamento de GPUs no Elasticsearch.
public class GpuDocument {

    // Identificador exclusivo do documento no Elasticsearch (String/UUID).
    @Id
    private String id;

    // Referência ao ID original presente no banco de dados relacional (PostgreSQL).
    // Fundamental para reconstruir a build completa utilizando a entidade oficial do banco.
    @Field(type = FieldType.Long)
    private Long postgresId;

    // Nome comercial da GPU configurado para buscas textuais (Ex: "RTX 4060 Ti").
    // O tipo 'Text' permite que o Elasticsearch lide com termos parciais e relevância de busca.
    @Field(type = FieldType.Text)
    private String nome;

    // Quantidade de VRAM disponível. Utilizado para filtrar placas capazes de rodar
    // jogos ou aplicações em resoluções específicas (Ex: 4K exige mais VRAM).
    @Field(type = FieldType.Integer)
    private Integer memoriaVram;

    // Consumo energético recomendado para o funcionamento estável da placa.
    // Utilizado pelo motor de busca para filtrar opções que a fonte escolhida consiga alimentar.
    @Field(type = FieldType.Integer)
    private Integer potenciaRecomendadaW;

    // Valor do componente para controle de orçamento e ordenação de resultados por custo-benefício.
    @Field(type = FieldType.Double)
    private Double preco;
}