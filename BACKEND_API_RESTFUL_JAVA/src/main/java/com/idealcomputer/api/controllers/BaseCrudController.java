package com.idealcomputer.api.controllers;

import com.idealcomputer.api.models.BaseEntity;
import com.idealcomputer.api.services.BaseCrudService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Classe base abstrata para controladores REST, centralizando os endpoints de CRUD.
 * Resolve o problema de repetição de lógica de entrada/saída HTTP, garantindo que todos
 * os componentes de hardware sigam as mesmas convenções de rotas e códigos de status.
 *
 * @param <T>  Tipo da Entidade (deve implementar BaseEntity)
 * @param <ID> Tipo do Identificador (ex: Long)
 * @param <S>  Tipo do Serviço associado
 */
public abstract class BaseCrudController<
        T extends BaseEntity<ID>,
        ID,
        S extends BaseCrudService<T, ID, ?>> {

    protected final S service;

    public BaseCrudController(S service) {
        this.service = service;
    }

    // --- ENDPOINTS GENÉRICOS ---

    // Lista todos os registros da entidade.
    @GetMapping
    public ResponseEntity<List<T>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    // Busca um registro específico pelo seu identificador único.
    @GetMapping(value = "/{id}")
    public ResponseEntity<T> findById(@PathVariable ID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    // Cria um novo registro e retorna o código 201 (Created) com a URI de localização.
    @PostMapping
    public ResponseEntity<T> create(@RequestBody T entity) {
        T newEntity = service.save(entity);
        // Gera o Header 'Location' apontando para o novo recurso criado.
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(newEntity.getId()).toUri();
        return ResponseEntity.created(uri).body(newEntity);
    }

    // Atualiza um registro existente com base no ID fornecido na URL.
    @PutMapping(value = "/{id}")
    public ResponseEntity<T> update(@PathVariable ID id, @RequestBody T entity) {
        entity.setId(id); // Garante que o ID da URL tenha precedência sobre o corpo da requisição.
        T updatedEntity = service.save(entity);
        return ResponseEntity.ok(updatedEntity);
    }

    // Remove um registro e retorna o código 204 (No Content).
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable ID id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}