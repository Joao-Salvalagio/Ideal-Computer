package com.idealcomputer.api.services;

import com.idealcomputer.api.models.GabineteModel;
import com.idealcomputer.api.repositories.GabineteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Camada de serviço responsável pela lógica de negócio de gabinetes.
// Resolve o problema de armazenamento e validação física da build, utilizando a
// estrutura da BaseCrudService para gerenciar o catálogo de modelos suportados.
@Service
public class GabineteService extends BaseCrudService<GabineteModel, Long, GabineteRepository> {

    // Construtor que injeta o repositório JPA via Spring.
    // Vincula este serviço à lógica genérica da classe pai para gerenciar a entidade 'Gabinete'.
    @Autowired
    public GabineteService(GabineteRepository repository) {
        super(repository, "Gabinete");
    }

    // Persiste ou atualiza as especificações do gabinete (dimensões, suporte a placas-mãe) no PostgreSQL.
    // A anotação @Transactional assegura a integridade da operação de escrita.
    @Override
    @Transactional
    public GabineteModel save(GabineteModel entity) {
        return super.save(entity);
    }

    // Remove um gabinete do banco de dados relacional.
    // A lógica herdada garante que a remoção só ocorra após a validação da existência do registro.
    @Override
    @Transactional
    public void deleteById(Long id) {
        super.deleteById(id);
    }
}