package com.idealcomputer.api.exceptions;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

// Padroniza o formato das respostas de erro da API.
// Garante que todas as falhas retornem a mesma estrutura de dados,
// facilitando a leitura e o tratamento dos problemas pelo frontend.
@Getter
@Setter
@Builder // Permite criar instâncias desta classe de forma mais limpa e fluida.
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorDTO {

    // Data e hora exata em que o erro aconteceu.
    private LocalDateTime timestamp;

    // Código de status HTTP (Ex: 400, 404, 500).
    private Integer status;

    // Nome do erro retornado (Ex: "Bad Request", "Not Found").
    private String error;

    // Mensagem principal explicando o motivo da falha.
    private String message;

    // Caminho do endpoint que foi chamado e gerou o erro (Ex: "/api/usuarios").
    private String path;

    // Guarda uma lista de erros de validação específicos.
    // Útil para informar exatamente quais dados de uma requisição foram enviados de forma incorreta.
    private List<FieldError> fieldErrors;

    // Representa o erro de um único campo.
    // Usado para apontar exatamente onde está o problema dentro de um envio de dados.
    @Getter
    @Setter
    @Builder // Permite instanciar o erro de campo facilmente.
    @AllArgsConstructor
    public static class FieldError {

        // Nome do campo que está com dado inválido (Ex: "senha").
        private String field;

        // Motivo pelo qual o campo foi rejeitado (Ex: "A senha deve ter no mínimo 8 caracteres").
        private String message;
    }
}