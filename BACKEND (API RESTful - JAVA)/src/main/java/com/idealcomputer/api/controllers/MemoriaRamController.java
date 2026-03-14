package com.idealcomputer.api.controllers;

import com.idealcomputer.api.models.MemoriaRamModel;
import com.idealcomputer.api.services.MemoriaRamService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Controlador para gestão de módulos de memória RAM.
@RestController
@RequestMapping(value = "/api/memorias-ram")
public class MemoriaRamController extends BaseCrudController<MemoriaRamModel, Long, MemoriaRamService> {
    public MemoriaRamController(MemoriaRamService service) {
        super(service);
    }
}