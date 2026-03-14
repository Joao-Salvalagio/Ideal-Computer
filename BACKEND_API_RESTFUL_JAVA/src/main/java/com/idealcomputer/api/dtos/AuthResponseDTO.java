package com.idealcomputer.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Objeto de resposta enviado após uma autenticação bem-sucedida.
// Resolve o problema de entrega de credenciais e dados de perfil inicial para o frontend,
// permitindo que a aplicação cliente armazene o token e personalize a experiência do usuário.
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDTO {

    // Token JWT (JSON Web Token) gerado pelo servidor.
    // Deve ser incluído no cabeçalho 'Authorization' das requisições subsequentes do Angular.
    private String token;

    // E-mail do usuário autenticado, utilizado para identificação de sessões.
    private String email;

    // Nome completo ou apelido do usuário para exibição imediata na interface (Navbar/Perfil).
    private String name;
}