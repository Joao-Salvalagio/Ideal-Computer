package com.idealcomputer.api.models;

// Interface genérica que define o contrato básico para todas as entidades do sistema.
// Resolve o problema de repetição de código, permitindo que componentes genéricos
// (como BaseCrudService) manipulem diferentes objetos através de um identificador comum.
// O parâmetro <ID> define o tipo de dado da chave primária (Ex: Long, String, UUID).
public interface BaseEntity<ID> {

    // Recupera o identificador único da entidade.
    // Essencial para operações de busca, atualização e deleção no banco de dados.
    ID getId();

    // Define o identificador único da entidade.
    // Utilizado principalmente pelo Hibernate/JPA durante a criação de novos registros
    // ou reconstrução de objetos vindos do banco de dados.
    void setId(ID id);
}