package com.idealcomputer.api.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

// Objeto de transferência para solicitações de recomendação inteligente.
// Resolve o problema de captura de requisitos do usuário, permitindo que o
// backend processe o perfil de uso e a restrição orçamentária para gerar uma build equilibrada.
@Data
public class RecommendationRequestDTO {

    // Finalidade da build (Ex: "Gamer", "Edição de Vídeo", "Office").
    // Essencial para o algoritmo decidir se prioriza GPU, CPU ou Memória RAM.
    @NotBlank(message = "O uso principal do computador é obrigatório (ex: jogos, trabalho).")
    private String usage;

    // Perfil financeiro da build (Ex: "Econômico", "Custo-Benefício", "Entusiasta").
    // Define o teto de gastos que será aplicado nos filtros do Elasticsearch.
    @NotBlank(message = "O orçamento é obrigatório (ex: econômico, alto).")
    private String budget;

    // Informações adicionais fornecidas pelo usuário (Ex: "Quero rodar Cyberpunk em 4K").
    // Campo opcional que pode ser processado para refinar a busca textual no motor de busca.
    private String detail;
}