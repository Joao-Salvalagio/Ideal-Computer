package com.idealcomputer.api.services;

import com.idealcomputer.api.models.ArmazenamentoModel;
import com.idealcomputer.api.repositories.ArmazenamentoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Camada de serviço responsável pela lógica de negócio de unidades de armazenamento (SSD/HDD).
// Resolve o problema de redundância de código ao herdar comportamentos genéricos da BaseCrudService,
// garantindo que as operações fundamentais de persistência sejam consistentes em todo o sistema.
@Service
public class ArmazenamentoService extends BaseCrudService<ArmazenamentoModel, Long, ArmazenamentoRepository> {

    // Construtor que injeta o repositório específico via Spring.
    // O uso do 'super' vincula este serviço à implementação genérica da classe pai,
    // permitindo que o sistema saiba exatamente qual repositório e nome de entidade utilizar.
    @Autowired
    public ArmazenamentoService(ArmazenamentoRepository repository) {
        super(repository, "Armazenamento");
    }

    // Persiste um novo registro ou atualiza um existente no PostgreSQL.
    // A anotação @Transactional garante a atomicidade da operação, assegurando que
    // o estado do banco de dados seja preservado em caso de falhas durante a escrita.
    @Override
    @Transactional
    public ArmazenamentoModel save(ArmazenamentoModel entity) {
        return super.save(entity);
    }

    // Remove uma unidade de armazenamento do banco de dados relacional pelo seu ID.
    // Resolve o problema de integridade ao utilizar a lógica de verificação de existência
    // herdada da BaseCrudService antes de efetuar a exclusão definitiva.
    @Override
    @Transactional
    public void deleteById(Long id) {
        super.deleteById(id);
    }
}