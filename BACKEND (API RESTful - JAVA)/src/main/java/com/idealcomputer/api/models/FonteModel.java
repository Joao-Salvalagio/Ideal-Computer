package com.idealcomputer.api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Representa a fonte de alimentação (PSU) no banco de dados.
// Contém as especificações necessárias para garantir que o sistema receba energia suficiente
// e que o componente caiba fisicamente dentro do gabinete escolhido.
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TB_Fonte")
public class FonteModel implements BaseEntity<Long> {

    // Identificador único da fonte no banco de dados.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_FONTE")
    private Long id;

    // Nome comercial do modelo (Ex: "Corsair RM750e", "XPG Pylon 650W").
    @Column(nullable = false, name = "Nome_FONTE")
    private String nome;

    // Fabricante do componente (Ex: "Corsair", "EVGA", "Cooler Master").
    @Column(nullable = false, name = "Marca_FONTE")
    private String marca;

    // Capacidade máxima de entrega de energia em Watts.
    // Este valor é comparado com a soma do consumo da CPU e GPU para validar a build.
    @Column(nullable = false, name = "Qtd_PotenciaWatts_FONTE")
    private Integer potenciaWatts;

    // Define o padrão de tamanho físico da fonte (Ex: "ATX", "SFX").
    // Informação essencial para verificar a compatibilidade de espaço com o gabinete.
    @Column(nullable = false, name = "Formato_FONTE")
    private String formato;

    // Valor de mercado do componente para o cálculo do custo total da montagem.
    @Column(nullable = false, name = "Preco_FONTE")
    private Double preco;
}