package com.idealcomputer.api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Representa uma configuração completa de computador (Build) salva por um usuário.
// Esta é a entidade central do sistema, pois consolida todos os componentes de hardware
// escolhidos e os vincula a um perfil, permitindo o histórico e a persistência das montagens.
@Entity
@Table(name = "tb_builds")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder // Facilita a criação de instâncias complexas com múltiplos componentes.
public class BuildModel {

    // Identificador único da build salva.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_build")
    private Long id;

    // Título personalizado dado pelo usuário à sua montagem (Ex: "PC Gamer 2024").
    @Column(name = "nome_build", nullable = false, length = 255)
    private String nomeBuild;

    // Vincula a build ao seu proprietário.
    // Utiliza FetchType.LAZY para carregar os dados do usuário apenas quando necessário,
    // otimizando a performance das consultas ao banco de dados.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private UserModel usuario;

    // --- Relacionamentos com Componentes de Hardware ---
    // Cada campo abaixo representa a escolha de uma peça específica para a montagem.
    // O uso de chaves estrangeiras (@JoinColumn) garante a integridade dos dados.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cpu", nullable = false)
    private CpuModel cpu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_placa_mae", nullable = false)
    private PlacaMaeModel placaMae;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_gpu") // Pode ser nulo caso o processador possua vídeo integrado.
    private GpuModel gpu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_memoria_ram", nullable = false)
    private MemoriaRamModel memoriaRam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_armazenamento", nullable = false)
    private ArmazenamentoModel armazenamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_fonte", nullable = false)
    private FonteModel fonte;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_gabinete", nullable = false)
    private GabineteModel gabinete;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_refrigeracao") // Opcional dependendo do gabinete ou cooler box.
    private RefrigeracaoModel refrigeracao;

    // --- Metadados da Build ---
    // Armazenam as intenções e filtros aplicados pelo usuário no momento da recomendação.

    // Finalidade principal da máquina (Ex: "Jogos", "Trabalho").
    @Column(name = "uso_principal", length = 50)
    private String usoPrincipal;

    // Nível de intensidade do uso (Ex: "Pesados", "Leves").
    @Column(name = "detalhe", length = 100)
    private String detalhe;

    // Categoria de custo definida no momento da geração (Ex: "Econômico").
    @Column(name = "orcamento", length = 50)
    private String orcamento;

    // Somatório dos preços de todos os componentes vinculados no momento do salvamento.
    @Column(name = "preco_total", precision = 10, scale = 2)
    private BigDecimal precoTotal;

    // --- Controle de Temporalidade ---

    // Registra o momento em que a build foi salva pela primeira vez.
    @Column(name = "data_criacao", updatable = false)
    private LocalDateTime dataCriacao;

    // Registra a última vez que a build sofreu alguma alteração.
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    // Método disparado automaticamente pelo Hibernate antes de inserir o registro.
    @PrePersist
    protected void onCreate() {
        this.dataCriacao = LocalDateTime.now();
        this.dataAtualizacao = LocalDateTime.now();
    }

    // Método disparado automaticamente pelo Hibernate antes de atualizar o registro.
    @PreUpdate
    protected void onUpdate() {
        this.dataAtualizacao = LocalDateTime.now();
    }
}