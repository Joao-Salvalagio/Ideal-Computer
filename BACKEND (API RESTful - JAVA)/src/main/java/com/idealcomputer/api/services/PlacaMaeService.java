package com.idealcomputer.api.services;

import com.idealcomputer.api.models.PlacaMaeModel;
import com.idealcomputer.api.repositories.PlacaMaeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Camada de serviço responsável pela lógica de negócio de placas-mãe.
// Resolve o problema de gerenciamento do componente central da build, utilizando a
// estrutura da BaseCrudService para garantir a integridade dos dados de suporte e conectividade.
@Service
public class PlacaMaeService extends BaseCrudService<PlacaMaeModel, Long, PlacaMaeRepository> {

    // Construtor que injeta o repositório JPA via Spring.
    // Vincula este serviço à lógica genérica da classe pai para gerenciar a entidade 'Placa-mãe'.
    @Autowired
    public PlacaMaeService(PlacaMaeRepository repository) {
        super(repository, "Placa-mãe");
    }

    // Persiste ou atualiza os dados estruturais (Soquete, Chipset, Slots de RAM) no PostgreSQL.
    // A anotação @Transactional garante a consistência atômica da operação no banco relacional.
    @Override
    @Transactional
    public PlacaMaeModel save(PlacaMaeModel entity) {
        return super.save(entity);
    }

    // Remove uma placa-mãe do banco de dados relacional.
    // A lógica herdada assegura que o registro exista antes de processar a exclusão.
    @Override
    @Transactional
    public void deleteById(Long id) {
        super.deleteById(id);
    }
}