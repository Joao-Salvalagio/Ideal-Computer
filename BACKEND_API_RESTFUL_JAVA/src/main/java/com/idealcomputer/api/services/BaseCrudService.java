package com.idealcomputer.api.services;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Classe base abstrata para centralizar a lógica de operações CRUD (Criar, Ler, Atualizar e Deletar).
 * Resolve o problema de duplicação de código em serviços de hardware, fornecendo uma implementação
 * padronizada que utiliza Generics para se adaptar a qualquer Model e Repository do sistema.
 * * @param <T>  Tipo da Entidade (Ex: CpuModel)
 * @param <ID> Tipo do Identificador (Ex: Long)
 * @param <R>  Tipo do Repositório, que deve estender JpaRepository
 */
public abstract class BaseCrudService<T, ID, R extends JpaRepository<T, ID>> {

    // Repositório injetado pelas classes filhas. Definido como 'protected' para permitir
    // que serviços específicos acessem métodos customizados do repositório se necessário.
    protected final R repository;

    // Nome amigável da entidade para facilitar a geração de mensagens de erro claras.
    private final String entityName;

    // Construtor obrigatório para as classes que herdam desta base, garantindo a
    // inicialização do repositório e do contexto da entidade.
    public BaseCrudService(R repository, String entityName) {
        this.repository = repository;
        this.entityName = entityName;
    }

    // --- MÉTODOS DE CRUD GENÉRICOS ---

    /**
     * Recupera todos os registros da entidade do banco de dados relacional.
     * @return Lista contendo todas as instâncias da entidade <T>.
     */
    public List<T> findAll() {
        return repository.findAll();
    }

    /**
     * Localiza uma entidade específica através do seu identificador.
     * Resolve o problema de tratamento de erros ao lançar uma exceção padronizada
     * caso o ID informado não exista na base de dados.
     * * @param id Identificador da entidade.
     * @return A entidade encontrada.
     * @throws RuntimeException Caso a busca não retorne resultados.
     */
    public T findById(ID id) {
        Optional<T> entity = repository.findById(id);
        return entity.orElseThrow(() -> new RuntimeException(this.entityName + " com ID " + id + " não encontrado."));
    }

    /**
     * Persiste as alterações ou cria um novo registro no banco de dados.
     * @param entity Instância da entidade a ser salva.
     * @return A entidade persistida com seu estado atualizado (incluindo ID gerado).
     */
    public T save(T entity) {
        return repository.save(entity);
    }

    /**
     * Remove permanentemente um registro do sistema após validar sua existência.
     * @param id Identificador da entidade a ser removida.
     */
    public void deleteById(ID id) {
        // Reutiliza o método findById para garantir que o erro 404/Exception ocorra
        // antes de tentar uma deleção inválida.
        findById(id);
        repository.deleteById(id);
    }
}