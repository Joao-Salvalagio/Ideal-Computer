package com.idealcomputer.api.controllers;

import com.idealcomputer.api.services.ElasticSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Controlador REST responsável por expor endpoints para o gerenciamento manual do Elasticsearch.
// Tem o objetivo de permitir que administradores ou rotinas automatizadas acionem a sincronização
// de dados do PostgreSQL para o motor de busca sem a necessidade de reinicializar a aplicação.
@RestController
@RequestMapping("/api/elastic-sync")
@RequiredArgsConstructor
public class ElasticSyncController {

    // Dependência injetada do serviço de sincronização, contendo a lógica de extração e indexação.
    private final ElasticSyncService elasticSyncService;

    // Método responsável por receber a requisição HTTP POST e iniciar a sincronização massiva de todas as entidades.
    // Retorna um status HTTP 200 (OK) assim que o processo de indexação no Elasticsearch for finalizado com sucesso.
    @PostMapping("/all")
    public ResponseEntity<String> syncAllData() {
        elasticSyncService.syncAll();
        return ResponseEntity.ok("Sincronização massiva concluída com sucesso.");
    }
}