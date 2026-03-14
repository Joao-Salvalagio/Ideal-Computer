package com.idealcomputer.api.controllers;

import com.idealcomputer.api.models.RefrigeracaoModel;
import com.idealcomputer.api.services.RefrigeracaoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Controlador para gestão de sistemas de refrigeração (Coolers/Water Coolers).
@RestController
@RequestMapping(value = "/api/refrigeracoes")
public class RefrigeracaoController extends BaseCrudController<RefrigeracaoModel, Long, RefrigeracaoService> {
    public RefrigeracaoController(RefrigeracaoService service) {
        super(service);
    }
}