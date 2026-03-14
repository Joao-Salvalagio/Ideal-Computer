package com.idealcomputer.api.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

// Representa o modelo de dados de sistemas de refrigeração dentro do motor de busca Elasticsearch.
// Esta estrutura permite que a API localize rapidamente coolers compatíveis com o processador escolhido,
// garantindo que o motor de recomendações filtre apenas opções que possuam o encaixe físico correto.
@Data
@Document(indexName = "refrigeracoes") // Define o índice específico para o armazenamento de coolers no Elasticsearch.
public class RefrigeracaoDocument {

    // Identificador exclusivo do documento no Elasticsearch.
    @Id
    private String id;

    // Referência ao ID original presente na tabela do PostgreSQL.
    // Utilizado para realizar o vínculo entre o motor de busca e a entidade de domínio oficial.
    @Field(type = FieldType.Long)
    private Long postgresId;

    // Nome comercial do sistema de refrigeração configurado para buscas textuais (Ex: "AK400 Zero Dark").
    // O tipo 'Text' permite encontrar o componente através de termos parciais ou marcas.
    @Field(type = FieldType.Text)
    private String nome;

    // Categoria do sistema (Ex: "Air Cooler", "Water Cooler").
    // O tipo 'Keyword' facilita a filtragem rápida por preferência estética ou técnica do usuário.
    @Field(type = FieldType.Keyword)
    private String tipo;

    // Lista de soquetes suportados configurada para busca textual.
    // Permite que o Elasticsearch verifique se o soquete da placa-mãe/CPU está presente nesta lista.
    @Field(type = FieldType.Text)
    private String soquetesCpuSuportados;

    // Valor do componente utilizado para cálculos de orçamento e ordenação de resultados.
    @Field(type = FieldType.Double)
    private Double preco;
}