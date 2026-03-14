package com.idealcomputer.api.repositories;

import com.idealcomputer.api.models.BuildModel;
import com.idealcomputer.api.models.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Interface de acesso a dados para a entidade Build.
// Resolve o problema de recuperação de montagens através de filtros e paginação,
// garantindo que o banco de dados entregue apenas a fatia de dados necessária (performance).
@Repository
public interface BuildRepository extends JpaRepository<BuildModel, Long> {

    // Recupera uma página de builds de um usuário específico, ordenadas cronologicamente.
    // Resolve o problema de performance ao carregar grandes listas de montagens no frontend.
    Page<BuildModel> findByUsuarioOrderByDataCriacaoDesc(UserModel usuario, Pageable pageable);

    // Realiza a busca paginada por nome de build dentro do perfil de um usuário.
    // O uso de 'ContainingIgnoreCase' permite buscas parciais (like %nome%) e insensíveis a maiúsculas.
    Page<BuildModel> findByUsuarioAndNomeBuildContainingIgnoreCase(UserModel usuario, String nomeBuild, Pageable pageable);
}