package com.idealcomputer.api.services;

import com.idealcomputer.api.models.UserModel;
import com.idealcomputer.api.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

// Camada de serviço responsável pela gestão de usuários e segurança de credenciais.
// Resolve o problema de persistência de perfis e garante que as senhas sejam
// devidamente criptografadas antes de serem armazenadas no PostgreSQL.
@Service
public class UserService extends BaseCrudService<UserModel, Long, UserRepository> {

    private final PasswordEncoder passwordEncoder;

    // Injeção de dependência via construtor.
    // O PasswordEncoder é essencial para transformar senhas simples em hashes BCrypt.
    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        super(repository, "Usuário");
        this.passwordEncoder = passwordEncoder;
    }

    // Localiza um usuário pelo e-mail, principal identificador no fluxo de autenticação.
    // Lança uma ResponseStatusException (404) para facilitar o tratamento de erros na API.
    public UserModel findByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado com o email: " + email));
    }

    // Persiste ou atualiza um usuário, aplicando lógica de hashing de senha.
    // Resolve o problema de segurança ao detectar se a senha precisa ser codificada
    // ou se a senha antiga deve ser mantida em caso de atualizações de perfil parciais.
    @Override
    @Transactional
    public UserModel save(UserModel entity) {
        // Lógica para Atualização (Update)
        if (entity.getId() != null) {
            UserModel userOriginal = findById(entity.getId());

            // Se o campo de senha estiver vazio no DTO de atualização, preservamos o hash atual.
            if (entity.getPassword() == null || entity.getPassword().trim().isEmpty()) {
                entity.setPassword(userOriginal.getPassword());
            } else {
                entity.setPassword(passwordEncoder.encode(entity.getPassword()));
            }
        }
        // Lógica para Novo Cadastro (Create)
        else {
            entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        }

        return super.save(entity);
    }
}