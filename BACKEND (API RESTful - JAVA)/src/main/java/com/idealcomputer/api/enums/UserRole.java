package com.idealcomputer.api.enums;

// Define os níveis de acesso (roles) do sistema.
// Utilizado pelo Spring Security para controle de autorização, resolvendo o problema
// de restrição de endpoints com base nos privilégios da conta autenticada.
public enum UserRole {

    // Cliente padrão. Possui acesso restrito às funcionalidades de consumo da API,
    // como gerar recomendações de hardware e salvar builds personalizadas.
    USUARIO,

    // Mantenedor do sistema. Possui acesso irrestrito a todos os endpoints,
    // permitindo operações críticas de gerenciamento (CRUD completo) de hardware e usuários.
    ADMINISTRADOR
}