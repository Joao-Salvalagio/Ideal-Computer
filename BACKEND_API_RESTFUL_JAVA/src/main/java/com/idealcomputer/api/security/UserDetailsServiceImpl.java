package com.idealcomputer.api.security;

import com.idealcomputer.api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// Serviço integrado ao Spring Security responsável por buscar os dados de autenticação.
// Resolve o problema de conectar o mecanismo de login do framework com a base de dados oficial.
@Service
@RequiredArgsConstructor // Gera automaticamente o construtor para injeção de dependências (campos final).
public class UserDetailsServiceImpl implements UserDetailsService {

    // Dependência utilizada para acessar a tabela de usuários no banco de dados.
    private final UserRepository userRepository;

    // Método acionado internamente pelo Spring Security durante o processo de autenticação ou validação de token.
    // Localiza o usuário através do e-mail fornecido.
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // Realiza a busca do usuário no banco de dados.
        // Como UserModel agora implementa a interface UserDetails nativamente,
        // o objeto retornado pelo banco pode ser devolvido diretamente ao framework,
        // eliminando a necessidade de conversão manual e preservando todos os atributos originais em memória.
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o e-mail: " + email));
    }
}