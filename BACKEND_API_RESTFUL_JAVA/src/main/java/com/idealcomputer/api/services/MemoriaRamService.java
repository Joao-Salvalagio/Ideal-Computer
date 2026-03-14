package com.idealcomputer.api.services;

import com.idealcomputer.api.models.MemoriaRamModel;
import com.idealcomputer.api.repositories.MemoriaRamRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Camada de serviço responsável pela lógica de negócio de memórias RAM.
// Resolve o problema de compatibilidade de geração e frequência, utilizando a
// estrutura da BaseCrudService para manter o inventário de memória sempre atualizado.
@Service
public class MemoriaRamService extends BaseCrudService<MemoriaRamModel, Long, MemoriaRamRepository> {

    // Construtor que injeta o repositório JPA via Spring.
    // Vincula este serviço à lógica genérica da classe pai para gerenciar a entidade 'Memória RAM'.
    @Autowired
    public MemoriaRamService(MemoriaRamRepository repository) {
        super(repository, "Memória RAM");
    }

    // Persiste ou atualiza os dados técnicos (Capacidade, DDR, Frequência) no PostgreSQL.
    // A anotação @Transactional garante que a alteração seja refletida com segurança no banco.
    @Override
    @Transactional
    public MemoriaRamModel save(MemoriaRamModel entity) {
        return super.save(entity);
    }

    // Remove um módulo de memória do banco de dados relacional.
    // A lógica herdada impede a deleção de registros inexistentes através da validação prévia.
    @Override
    @Transactional
    public void deleteById(Long id) {
        super.deleteById(id);
    }
}