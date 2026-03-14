package com.idealcomputer.api.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

// Objeto de transferência para requisições de autenticação e registro.
// Resolve o problema de recepção de credenciais do frontend, centralizando as
// validações de formato e presença de dados antes que eles cheguem à camada de serviço.
@Data
public class AuthRequestDTO {

    // Nome do usuário, utilizado apenas no fluxo de cadastro.
    // Permanece opcional para permitir o reaproveitamento deste DTO na rota de login.
    private String name;

    // Identificador único de acesso.
    // A anotação @Email garante a integridade do formato no padrão 'usuario@dominio.com'.
    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "O formato do e-mail é inválido.")
    private String email;

    // Credencial de segurança do usuário.
    // A restrição de tamanho mínimo é validada aqui para evitar processamento
    // desnecessário de senhas inseguras no servidor.
    @NotBlank(message = "A senha é obrigatória.")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.")
    private String password;
}