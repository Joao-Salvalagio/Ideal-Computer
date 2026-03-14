package com.idealcomputer.api.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

// Intercepta e trata todos os erros gerados pela API de forma centralizada.
// A anotação @RestControllerAdvice escuta exceções lançadas em qualquer controle (Controller).
// Resolve o problema de respostas inconsistentes, garantindo que o frontend sempre receba
// o mesmo formato de erro (ApiErrorDTO), não importando onde a falha ocorreu.
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Captura falhas de validação nos dados enviados pelo usuário (ex: campos vazios, senhas curtas).
    // Acionado automaticamente quando um DTO anotado com @Valid falha.
    // Retorna o status HTTP 400 (Bad Request).
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorDTO> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {

        // Extrai os erros internos do Spring e transforma em uma lista simplificada de campos e mensagens.
        List<ApiErrorDTO.FieldError> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> ApiErrorDTO.FieldError.builder()
                        .field(error.getField())
                        .message(error.getDefaultMessage())
                        .build())
                .collect(Collectors.toList());

        // Monta a resposta de erro utilizando o padrão Builder, tornando o código mais limpo.
        ApiErrorDTO errorDTO = ApiErrorDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Erro de Validação")
                .message("Um ou mais campos estão inválidos. Verifique a lista de erros e tente novamente.")
                .path(request.getRequestURI())
                .fieldErrors(fieldErrors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
    }

    // Captura problemas de integridade no banco de dados, como tentar salvar dados duplicados.
    // Evita que erros complexos de SQL cheguem ao usuário, traduzindo para uma mensagem legível.
    // Retorna o status HTTP 409 (Conflict).
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorDTO> handleDataIntegrityViolationException(DataIntegrityViolationException ex, HttpServletRequest request) {

        // Analisa a mensagem de erro do banco para identificar a causa da violação.
        // Garante que o usuário receba uma mensagem coerente com o dado que gerou o conflito.
        String mensagemErro = "Conflito de dados: um registro com essas informações já existe no sistema.";
        if (ex.getMessage() != null && ex.getMessage().toLowerCase().contains("email")) {
            mensagemErro = "O e-mail fornecido já está em uso. Por favor, utilize outro e-mail.";
        }

        ApiErrorDTO errorDTO = ApiErrorDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error("Conflito de Dados")
                .message(mensagemErro)
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDTO);
    }

    // Captura exceções manuais relacionadas a regras de negócio que não foram atendidas.
    // Repassa a mensagem exata criada na camada de serviço (ex: "Orçamento insuficiente").
    // Retorna o status HTTP 400 (Bad Request).
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiErrorDTO> handleBusinessException(RuntimeException ex, HttpServletRequest request) {

        ApiErrorDTO errorDTO = ApiErrorDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Regra de Negócio")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
    }

    // Rede de segurança (Fallback) para capturar qualquer erro não previsto (bugs na aplicação).
    // Evita a exposição de informações sensíveis do servidor (Stack Trace) para o exterior.
    // Retorna o status HTTP 500 (Internal Server Error).
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorDTO> handleGenericException(Exception ex, HttpServletRequest request) {

        ApiErrorDTO errorDTO = ApiErrorDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Erro Interno do Servidor")
                .message("Ocorreu um erro inesperado. Por favor, contate o suporte.")
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
    }
}