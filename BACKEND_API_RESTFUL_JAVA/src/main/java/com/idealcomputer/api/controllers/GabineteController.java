package com.idealcomputer.api.controllers;

import com.idealcomputer.api.models.GabineteModel;
import com.idealcomputer.api.services.GabineteService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Controlador para gestão de gabinetes.
@RestController
@RequestMapping(value = "/api/gabinetes")
public class GabineteController extends BaseCrudController<GabineteModel, Long, GabineteService> {
    public GabineteController(GabineteService service) {
        super(service);
    }
}