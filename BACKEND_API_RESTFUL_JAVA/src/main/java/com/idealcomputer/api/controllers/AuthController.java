package com.idealcomputer.api.controllers;

import com.idealcomputer.api.dtos.AuthRequestDTO;
import com.idealcomputer.api.dtos.AuthResponseDTO;
import com.idealcomputer.api.enums.UserRole;
import com.idealcomputer.api.models.UserModel;
import com.idealcomputer.api.security.JwtUtil;
import com.idealcomputer.api.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

// Controlador responsável pelos processos de autenticação e registro de usuários.
// Resolve o problema de acesso ao sistema, fornecendo endpoints para criação de contas
// e geração de tokens JWT para sessões seguras no frontend Angular.
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    // Registra um novo usuário com perfil padrão de cliente.
    // Utiliza o Bean Validation (@Valid) para garantir que os dados recebidos
    // cumpram os requisitos de e-mail e tamanho de senha definidos no DTO.
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody AuthRequestDTO request) {
        UserModel newUser = new UserModel();
        newUser.setName(request.getName());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(request.getPassword());
        newUser.setFuncao(UserRole.USUARIO);
        newUser.setCargo("Cliente");

        userService.save(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).body("Usuário registrado com sucesso!");
    }

    // Valida credenciais de acesso e retorna um token JWT em caso de sucesso.
    // Resolve o problema de persistência de sessão stateless, devolvendo os dados
    // necessários para o Angular gerenciar o estado de login do usuário.
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> createAuthenticationToken(@Valid @RequestBody AuthRequestDTO request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            // Tratamento explícito de credenciais inválidas para evitar vazamento de logs de erro internos.
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email ou senha inválidos");
        }

        // Recupera os dados do usuário para incluir informações de perfil na resposta do token.
        UserModel userModel = userService.findByEmail(request.getEmail());

        final String token = jwtUtil.generateToken(userModel);

        return ResponseEntity.ok(new AuthResponseDTO(token, userModel.getEmail(), userModel.getName()));
    }
}