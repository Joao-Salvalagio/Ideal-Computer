package com.idealcomputer.api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Representa a unidade de processamento gráfico (GPU/Placa de Vídeo) no banco de dados.
// Contém as especificações essenciais para definir o desempenho visual da build,
// validar a compatibilidade energética e calcular o custo total da montagem.
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TB_Gpu")
public class GpuModel implements BaseEntity<Long> {

    // Identificador único da placa de vídeo no banco de dados.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_GPU")
    private Long id;

    // Nome comercial do modelo (Ex: "GeForce RTX 4070", "Radeon RX 7800 XT").
    @Column(nullable = false, name = "Nome_GPU")
    private String nome;

    // Fabricante do chip gráfico (Ex: "NVIDIA", "AMD").
    @Column(nullable = false, name = "Marca_GPU")
    private String marca;

    // Quantidade de memória de vídeo dedicada em Gigabytes (VRAM).
    // Informação fundamental para determinar a capacidade da placa em lidar com altas resoluções e texturas pesadas.
    @Column(nullable = false, name = "MemoriaVRAM_GPU")
    private Integer memoriaVram;

    // Valor de mercado do componente para o cálculo do orçamento total da build.
    @Column(nullable = false, name = "Preco_GPU")
    private Double preco;

    // Estimativa de consumo energético da placa de vídeo em Watts.
    // Como a GPU é geralmente o componente que mais consome energia, este campo é vital
    // para o cálculo de dimensionamento da fonte de alimentação (PSU).
    @Column(nullable = false, name = "Potencia_Recomendada_W_GPU")
    private Integer potenciaRecomendadaW;
}