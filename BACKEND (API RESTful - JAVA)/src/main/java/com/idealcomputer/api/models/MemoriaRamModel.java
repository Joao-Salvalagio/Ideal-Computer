package com.idealcomputer.api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Representa os módulos de memória RAM no banco de dados.
// Contém as especificações técnicas necessárias para garantir a compatibilidade com a placa-mãe
// e para definir a performance multitarefa do sistema montado.
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TB_Memoria_Ram")
public class MemoriaRamModel implements BaseEntity<Long> {

    // Identificador único da memória RAM no banco de dados.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_RAM")
    private Long id;

    // Nome comercial do modelo (Ex: "Kingston Fury Beast", "Corsair Vengeance LPX").
    @Column(nullable = false, name = "Nome_RAM")
    private String nome;

    // Fabricante do componente (Ex: "G.Skill", "Crucial", "ADATA").
    @Column(nullable = false, name = "Marca_RAM")
    private String marca;

    // Quantidade de memória em Gigabytes de um único módulo ou kit (Ex: 8, 16, 32).
    @Column(nullable = false, name = "Capacidade_GB_RAM")
    private Integer capacidadeGb;

    // Geração da tecnologia de memória (Ex: "DDR4", "DDR5").
    // Este campo é essencial para validar se a placa-mãe suporta fisicamente este pente de memória.
    @Column(nullable = false, name = "Tipo_DDR_RAM")
    private String tipo;

    // Velocidade de operação da memória em Megahertz (Ex: 3200, 5200, 6000).
    @Column(nullable = false, name = "Frequencia_MHZ_RAM")
    private Integer frequenciaMhz;

    // Valor de mercado do componente para o cálculo do orçamento total da build.
    @Column(nullable = false, name = "Preco_RAM")
    private Double preco;
}