package com.idealcomputer.api.controllers;

import com.idealcomputer.api.models.PlacaMaeModel;
import com.idealcomputer.api.services.PlacaMaeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Controlador para gestão de placas-mãe.
@RestController
@RequestMapping(value = "/api/placas-mae")
public class PlacaMaeController extends BaseCrudController<PlacaMaeModel, Long, PlacaMaeService> {
    public PlacaMaeController(PlacaMaeService service) {
        super(service);
    }
}