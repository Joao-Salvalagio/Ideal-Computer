package com.idealcomputer.api.services;

import com.idealcomputer.api.models.CpuModel;
import com.idealcomputer.api.repositories.CpuRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Camada de serviço responsável pela lógica de negócio de processadores (CPUs).
// Resolve o problema de manutenção de registros de hardware e herda as operações
// fundamentais de CRUD da BaseCrudService, garantindo padronização e reuso de código.
@Service
public class CpuService extends BaseCrudService<CpuModel, Long, CpuRepository> {

    // Construtor que injeta o repositório JPA via Spring.
    // Vincula este serviço à lógica genérica da classe pai para gerenciar a entidade 'CPU'.
    @Autowired
    public CpuService(CpuRepository repository) {
        super(repository, "Processador (CPU)");
    }

    // Persiste ou atualiza os dados técnicos e orçamentários de um processador no PostgreSQL.
    // A anotação @Transactional garante a atomicidade da operação de escrita.
    @Override
    @Transactional
    public CpuModel save(CpuModel entity) {
        return super.save(entity);
    }

    // Remove um processador do banco de dados relacional.
    // A lógica herdada valida a existência do ID antes de tentar a exclusão definitiva.
    @Override
    @Transactional
    public void deleteById(Long id) {
        super.deleteById(id);
    }
}