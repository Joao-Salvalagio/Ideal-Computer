package com.idealcomputer.api.services;

import com.idealcomputer.api.models.FonteModel;
import com.idealcomputer.api.repositories.FonteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Camada de serviço responsável pela lógica de negócio de unidades de alimentação (Fontes).
// Resolve o problema de gerenciamento de energia e requisitos elétricos da build,
// utilizando a estrutura genérica da BaseCrudService para manter a consistência dos dados.
@Service
public class FonteService extends BaseCrudService<FonteModel, Long, FonteRepository> {

    // Construtor que injeta o repositório JPA via Spring.
    // Vincula este serviço à lógica genérica da classe pai para gerenciar a entidade 'Fonte'.
    @Autowired
    public FonteService(FonteRepository repository) {
        super(repository, "Fonte");
    }

    // Persiste ou atualiza as informações técnicas (Watts, Selo 80 Plus, etc.) no PostgreSQL.
    // A anotação @Transactional assegura que a operação seja concluída de forma atômica.
    @Override
    @Transactional
    public FonteModel save(FonteModel entity) {
        return super.save(entity);
    }

    // Remove uma fonte do banco de dados relacional.
    // A lógica herdada garante a validação prévia da existência do componente.
    @Override
    @Transactional
    public void deleteById(Long id) {
        super.deleteById(id);
    }
}