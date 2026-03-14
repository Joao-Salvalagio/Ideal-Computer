package com.idealcomputer.api.services;

import com.idealcomputer.api.models.GpuModel;
import com.idealcomputer.api.repositories.GpuRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Camada de serviço responsável pela lógica de negócio de placas de vídeo (GPUs).
// Resolve o problema de gerenciamento de hardware gráfico, utilizando a estrutura
// da BaseCrudService para garantir operações de persistência seguras e padronizadas.
@Service
public class GpuService extends BaseCrudService<GpuModel, Long, GpuRepository> {

    // Construtor que injeta o repositório JPA via Spring.
    // Vincula este serviço à lógica genérica da classe pai para gerenciar a entidade 'GPU'.
    @Autowired
    public GpuService(GpuRepository repository) {
        super(repository, "GPU");
    }

    // Persiste ou atualiza os dados técnicos (VRAM, Consumo) e preços no PostgreSQL.
    // A anotação @Transactional garante a atomicidade da operação.
    @Override
    @Transactional
    public GpuModel save(GpuModel entity) {
        return super.save(entity);
    }

    // Remove uma GPU do banco de dados relacional.
    // A lógica herdada valida a existência do ID antes de efetuar a exclusão definitiva.
    @Override
    @Transactional
    public void deleteById(Long id) {
        super.deleteById(id);
    }
}