package com.idealcomputer.api.controllers;

import com.idealcomputer.api.models.UserModel;
import com.idealcomputer.api.services.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Controlador para gestão administrativa de usuários.
// Herda a estrutura de CRUD genérico, permitindo que administradores listem,
// atualizem ou removam perfis através de uma interface padronizada.
@RestController
@RequestMapping("/api/usuarios")
public class UserController extends BaseCrudController<UserModel, Long, UserService> {
    public UserController(UserService service) {
        super(service);
    }
}