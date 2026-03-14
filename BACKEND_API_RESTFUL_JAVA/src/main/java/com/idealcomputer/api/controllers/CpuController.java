package com.idealcomputer.api.controllers;

import com.idealcomputer.api.models.CpuModel;
import com.idealcomputer.api.services.CpuService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Controlador para gestão de processadores (CPUs).
// Herda todos os endpoints CRUD da BaseCrudController, garantindo uma API
// consistente e previsível para o frontend.
@RestController
@RequestMapping(value = "/api/cpus")
public class CpuController extends BaseCrudController<CpuModel, Long, CpuService> {
    public CpuController(CpuService service) {
        super(service);
    }
}