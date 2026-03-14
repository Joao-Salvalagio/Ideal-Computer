package com.idealcomputer.api.controllers;

import com.idealcomputer.api.dtos.BuildRequestDTO;
import com.idealcomputer.api.dtos.BuildResponseDTO;
import com.idealcomputer.api.services.BuildService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

// Controlador responsável por gerenciar as montagens customizadas dos usuários.
// Resolve o problema de segurança e isolamento de dados, garantindo que cada usuário
// acesse e manipule apenas suas próprias configurações de hardware.
@RestController
@RequestMapping("/api/builds")
@RequiredArgsConstructor
public class BuildController {

    private final BuildService buildService;

    // Cria uma nova build vinculada ao usuário autenticado.
    // Utiliza o contexto do Spring Security para capturar o e-mail do autor de forma segura.
    @PostMapping
    public ResponseEntity<BuildResponseDTO> salvarBuild(
            @Valid @RequestBody BuildRequestDTO dto,
            Authentication authentication) {

        String emailUsuario = authentication.getName();
        BuildResponseDTO response = buildService.salvarBuild(dto, emailUsuario);

        // Retorna o status 201 Created com o header Location apontando para o novo recurso.
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }

    // Retorna a lista de builds do usuário atual com suporte a paginação e filtros.
    // Essencial para a escalabilidade da tela "Minhas Builds" no frontend Angular.
    @GetMapping("/my-builds")
    public ResponseEntity<Page<BuildResponseDTO>> listarMinhasBuilds(
            @RequestParam(required = false) String nome,
            @PageableDefault(size = 10) Pageable pageable,
            Authentication authentication) {

        String emailUsuario = authentication.getName();
        return ResponseEntity.ok(buildService.listarMinhasBuilds(emailUsuario, nome, pageable));
    }

    // Recupera os detalhes de uma build específica, validando a posse do recurso.
    @GetMapping("/{id}")
    public ResponseEntity<BuildResponseDTO> buscarBuildPorId(
            @PathVariable Long id,
            Authentication authentication) {

        String emailUsuario = authentication.getName();
        return ResponseEntity.ok(buildService.buscarBuildPorId(id, emailUsuario));
    }

    // Remove uma build permanentemente do perfil do usuário.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarBuild(
            @PathVariable Long id,
            Authentication authentication) {

        String emailUsuario = authentication.getName();
        buildService.deletarBuild(id, emailUsuario);

        return ResponseEntity.noContent().build();
    }
}