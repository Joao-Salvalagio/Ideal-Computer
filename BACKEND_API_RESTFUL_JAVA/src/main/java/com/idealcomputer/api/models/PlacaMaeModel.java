package com.idealcomputer.api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Representa a placa-mãe (Motherboard) no banco de dados.
// É o componente central de interconexão, responsável por ditar a compatibilidade
// de quase todas as outras peças (Processador, RAM, Gabinete).
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TB_Placa_Mae")
public class PlacaMaeModel implements BaseEntity<Long> {

    // Identificador único da placa-mãe no banco de dados.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PLACAMAE")
    private Long id;

    // Nome comercial do modelo (Ex: "ASUS TUF Gaming B550M-Plus", "MSI MAG B650 Tomahawk").
    @Column(nullable = false, name = "Nome_PLACAMAE")
    private String nome;

    // Fabricante do componente (Ex: "ASUS", "Gigabyte", "MSI", "ASRock").
    @Column(nullable = false, name = "Marca_PLACAMAE")
    private String marca;

    // Define qual padrão de encaixe de processador a placa possui (Ex: "AM4", "LGA1700").
    // Este campo deve ser idêntico ao do CpuModel para que a montagem seja válida.
    @Column(nullable = false, name = "Soquete_CPU_PLACAMAE")
    private String soqueteCpu;

    // Define a tecnologia de memória aceita (Ex: "DDR4", "DDR5").
    // Este campo deve ser idêntico ao do MemoriaRamModel para garantir a compatibilidade física.
    @Column(nullable = false, name = "Tipo_RAM_Suportado_PLACAMAE")
    private String tipoRamSuportado;

    // Define o tamanho físico da placa (Ex: "ATX", "Micro-ATX", "Mini-ITX").
    // Utilizado para validar se a placa-mãe cabe dentro do GabineteModel escolhido.
    @Column(nullable = false, name = "Formato_PLACAMAE")
    private String formato;

    // Valor de mercado do componente para o cálculo do custo total da montagem.
    @Column(nullable = false, name = "Preco_PLACAMAE")
    private Double preco;
}