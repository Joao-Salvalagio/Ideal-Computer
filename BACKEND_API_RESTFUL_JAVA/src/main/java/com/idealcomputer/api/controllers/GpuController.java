package com.idealcomputer.api.controllers;

import com.idealcomputer.api.models.GpuModel;
import com.idealcomputer.api.services.GpuService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Controlador para gestão de placas de vídeo (GPUs).
@RestController
@RequestMapping(value = "/api/gpus")
public class GpuController extends BaseCrudController<GpuModel, Long, GpuService> {
    public GpuController(GpuService service) {
        super(service);
    }
}