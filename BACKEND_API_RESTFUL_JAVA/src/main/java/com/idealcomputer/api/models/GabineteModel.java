package com.idealcomputer.api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Representa o gabinete (case) no banco de dados.
// Além de ser o componente estético, é responsável por ditar os limites físicos da build,
// definindo quais tamanhos de placa-mãe e outros componentes podem ser instalados.
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TB_Gabinete")
public class GabineteModel implements BaseEntity<Long> {

    // Identificador único do gabinete no banco de dados.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_GABINETE")
    private Long id;

    // Nome comercial do modelo (Ex: "NZXT H5 Flow", "Corsair 4000D").
    @Column(nullable = false, name = "Nome_GABINETE")
    private String nome;

    // Fabricante do componente (Ex: "Lian Li", "Cooler Master", "Pichau").
    @Column(nullable = false, name = "Marca_GABINETE")
    private String marca;

    // Lista os padrões de placa-mãe que cabem no gabinete (Ex: "ATX, Micro-ATX, Mini-ITX").
    // Este campo é vital para validar se a placa-mãe escolhida poderá ser fixada corretamente.
    @Column(nullable = false, name = "Formatos_de_placa_mae_suportados_GABINETE")
    private String formatosPlacaMaeSuportados;

    // Valor de mercado do componente para o cálculo do orçamento da build.
    @Column(nullable = false, name = "Preco_GABINETE")
    private Double preco;
}