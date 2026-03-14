package com.idealcomputer.api.controllers;

import com.idealcomputer.api.models.ArmazenamentoModel;
import com.idealcomputer.api.services.ArmazenamentoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Controlador REST responsável por expor os endpoints de unidades de armazenamento.
// Resolve o problema de redundância em APIs de hardware ao herdar toda a estrutura
// de endpoints (GET, POST, PUT, DELETE) da BaseCrudController, mantendo a interface
// padronizada para o consumo pelo frontend Angular.
@RestController
@RequestMapping(value = "/api/armazenamentos")
public class ArmazenamentoController extends BaseCrudController<ArmazenamentoModel, Long, ArmazenamentoService> {

    // Injeção do serviço específico através do construtor.
    // Vincula este controlador à lógica de negócio de Armazenamento, permitindo que a
    // BaseCrudController orquestre as requisições HTTP de forma genérica e eficiente.
    public ArmazenamentoController(ArmazenamentoService service) {
        super(service);
    }

    /* * Nota técnica: Ao herdar da BaseCrudController, este endpoint já suporta:
     * - GET  /api/armazenamentos (Listar todos)
     * - GET  /api/armazenamentos/{id} (Buscar por ID)
     * - POST /api/armazenamentos (Criar novo)
     * - PUT  /api/armazenamentos/{id} (Atualizar existente)
     * - DELETE /api/armazenamentos/{id} (Remover registro)
     */
}