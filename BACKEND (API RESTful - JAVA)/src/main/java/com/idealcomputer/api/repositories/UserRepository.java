package com.idealcomputer.api.repositories;

import com.idealcomputer.api.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Interface de acesso a dados para a entidade Usuário.
// Resolve o problema de autenticação e gestão de perfis, sendo o elo fundamental
// entre o módulo de segurança (Spring Security) e o banco de dados PostgreSQL.
@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {

    // Recupera um usuário através do seu e-mail único.
    // Essencial para o processo de login e geração de tokens JWT, onde o e-mail
    // atua como o principal identificador (username) do sistema.
    Optional<UserModel> findByEmail(String email);

    // Realiza buscas flexíveis por nome ou e-mail, ignorando maiúsculas e minúsculas.
    // Resolve o problema de gerenciamento administrativo, permitindo que moderadores
    // localizem contas de usuários de forma eficiente na tela de administração.
    List<UserModel> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String name, String email);
}