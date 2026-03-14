package com.idealcomputer.api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Representa o sistema de refrigeração (Air Cooler ou Water Cooler) no banco de dados.
// Este componente é essencial para manter as temperaturas do processador em níveis seguros,
// sendo um item chave para garantir a durabilidade e o desempenho do hardware.
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TB_Refrigeracao")
public class RefrigeracaoModel implements BaseEntity<Long> {

    // Identificador único do sistema de refrigeração no banco de dados.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_REFRIGERACAO")
    private Long id;

    // Nome comercial do modelo (Ex: "DeepCool AK400", "Corsair iCUE H150i").
    @Column(nullable = false, name = "Nome_REFRIGERACAO")
    private String nome;

    // Fabricante do componente (Ex: "Cooler Master", "Noctua", "Lian Li").
    @Column(nullable = false, name = "Marca_REFRIGERACAO")
    private String marca;

    // Define a tecnologia utilizada (Ex: "Air Cooler", "Water Cooler").
    // Informação importante para o usuário decidir com base no espaço do gabinete e estética.
    @Column(nullable = false, name = "Tipo_REFRIGERACAO")
    private String tipo;

    // Lista os padrões de encaixe de processador compatíveis (Ex: "LGA1700, AM4, AM5").
    // Este campo é utilizado para validar se o cooler pode ser instalado na placa-mãe escolhida.
    @Column(nullable = false, name = "Soquetes_cpu_suportados_REFRIGERACAO")
    private String soquetesCpuSuportados;

    // Valor de mercado do componente para o cálculo do custo total da montagem.
    @Column(nullable = false, name = "Preco_REFRIGERACAO")
    private Double preco;
}