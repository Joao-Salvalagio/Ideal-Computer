package com.idealcomputer.api.controllers;

import com.idealcomputer.api.dtos.RecommendationRequestDTO;
import com.idealcomputer.api.dtos.RecommendationResponseDTO;
import com.idealcomputer.api.services.RecommendationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Controlador responsável por orquestrar as sugestões inteligentes de hardware.
// Resolve o problema de descoberta de produtos para usuários leigos, fornecendo
// um ponto de entrada para que o motor de busca gere builds equilibradas.
@RestController
@RequestMapping(value = "/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    // Processa uma solicitação de recomendação e retorna uma build completa sugerida.
    // Utiliza o Bean Validation (@Valid) para assegurar que as preferências de uso
    // e orçamento foram enviadas corretamente pelo Angular.
    @PostMapping
    public ResponseEntity<RecommendationResponseDTO> generate(@Valid @RequestBody RecommendationRequestDTO request) {
        RecommendationResponseDTO response = recommendationService.generateBuild(request);
        return ResponseEntity.ok(response);
    }
}