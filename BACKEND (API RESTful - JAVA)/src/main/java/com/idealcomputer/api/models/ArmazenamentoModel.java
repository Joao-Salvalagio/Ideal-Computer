package com.idealcomputer.api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Representa a tabela de dispositivos de armazenamento (HDs e SSDs) no banco de dados.
// Esta entidade armazena as especificações técnicas necessárias para validar a compatibilidade
// e realizar o cálculo de custo total durante a montagem de um computador.
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TB_Armazenamento")
public class ArmazenamentoModel implements BaseEntity<Long> {

    // Identificador único do dispositivo de armazenamento.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Armazenamento")
    private Long id;

    // Nome comercial ou modelo do produto (Ex: "Samsung 980 Pro").
    @Column(nullable = false, name = "Nome_ARMAZENAMENTO")
    private String nome;

    // Fabricante do componente (Ex: "Kingston", "Crucial", "Western Digital").
    @Column(nullable = false, name = "Marca_ARMAZENAMENTO")
    private String marca;

    // Tecnologia do dispositivo (Ex: "SSD NVMe", "SSD SATA", "HDD").
    // Informação crucial para determinar o desempenho e o tipo de conexão na placa-mãe.
    @Column(nullable = false, name = "Tipo_ARMAZENAMENTO")
    private String tipo;

    // Espaço total disponível em Gigabytes (Ex: 500, 1000, 2000).
    @Column(nullable = false, name = "Capacidade_GB_ARMAZENAMENTO")
    private Integer capacidadeGb;

    // Valor de mercado do componente.
    // Utilizado pelo motor de recomendações para filtrar builds que caibam no orçamento do usuário.
    @Column(nullable = false, name = "Preco_ARMAZENAMENTO")
    private Double preco;
}