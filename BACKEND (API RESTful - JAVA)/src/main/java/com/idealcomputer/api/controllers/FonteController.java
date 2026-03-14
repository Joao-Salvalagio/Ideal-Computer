package com.idealcomputer.api.controllers;

import com.idealcomputer.api.models.FonteModel;
import com.idealcomputer.api.services.FonteService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Controlador para gestão de unidades de alimentação (Fontes).
@RestController
@RequestMapping(value = "/api/fontes")
public class FonteController extends BaseCrudController<FonteModel, Long, FonteService> {
    public FonteController(FonteService service) {
        super(service);
    }
}