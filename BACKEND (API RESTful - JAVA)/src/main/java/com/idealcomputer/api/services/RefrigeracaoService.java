package com.idealcomputer.api.services;

import com.idealcomputer.api.models.RefrigeracaoModel;
import com.idealcomputer.api.repositories.RefrigeracaoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Camada de serviço responsável pela lógica de negócio de sistemas de arrefecimento (Coolers/Water Coolers).
// Resolve o problema de manutenção do inventário de refrigeração, garantindo que os dados
// de compatibilidade térmica e de soquete estejam íntegros através da BaseCrudService.
@Service
public class RefrigeracaoService extends BaseCrudService<RefrigeracaoModel, Long, RefrigeracaoRepository> {

    // Construtor que injeta o repositório JPA via Spring.
    // Vincula este serviço à lógica genérica da classe pai para gerenciar a entidade 'Refrigeração'.
    @Autowired
    public RefrigeracaoService(RefrigeracaoRepository repository) {
        super(repository, "Refrigeração");
    }

    // Persiste ou atualiza os dados técnicos (Tipo, TDP suportado, Ruído) no PostgreSQL.
    // A anotação @Transactional assegura a atomicidade da operação de escrita.
    @Override
    @Transactional
    public RefrigeracaoModel save(RefrigeracaoModel entity) {
        return super.save(entity);
    }

    // Remove um sistema de refrigeração do banco de dados relacional.
    // A lógica herdada valida se o componente existe antes de tentar a exclusão.
    @Override
    @Transactional
    public void deleteById(Long id) {
        super.deleteById(id);
    }
}