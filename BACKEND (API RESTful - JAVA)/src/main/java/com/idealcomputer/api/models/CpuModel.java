package com.idealcomputer.api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Representa a unidade central de processamento (CPU) no banco de dados.
// Contém as especificações técnicas essenciais para validar a compatibilidade com a placa-mãe
// e para o cálculo de dimensionamento da fonte de alimentação do sistema.
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TB_Cpu")
public class CpuModel implements BaseEntity<Long> {

    // Identificador único do processador no banco de dados.
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CPU")
    private Long id;

    // Nome comercial do modelo (Ex: "Core i7-13700K", "Ryzen 7 7800X3D").
    @Column(nullable = false, name = "Nome_CPU")
    private String nome;

    // Fabricante do componente (Ex: "Intel", "AMD").
    @Column(nullable = false, name = "Marca_CPU")
    private String marca;

    // Define o padrão físico de conexão com a placa-mãe (Ex: "LGA1700", "AM5").
    // Este campo é o principal critério de compatibilidade entre o processador e a placa-mãe.
    @Column(nullable = false, name = "Soquete_CPU")
    private String soquete;

    // Valor de mercado do componente para fins de cálculo do orçamento da build.
    @Column(nullable = false, name = "Preco_CPU")
    private Double preco;

    // Estimativa de consumo energético ou exigência térmica do processador em Watts.
    // Utilizado pelo sistema para garantir que a fonte de alimentação suporte o hardware escolhido.
    @Column(nullable = false, name = "Potencia_Recomendada_W_CPU")
    private Integer potenciaRecomendadaW;
}