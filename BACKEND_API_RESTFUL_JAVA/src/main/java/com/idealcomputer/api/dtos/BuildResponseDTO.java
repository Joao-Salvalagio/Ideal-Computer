package com.idealcomputer.api.dtos;

import com.idealcomputer.api.models.BuildModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// DTO de resposta para exibição de montagens.
// Resolve o problema de exposição de entidades de banco de dados, transformando
// as Models complexas em estruturas simples de leitura para o frontend.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BuildResponseDTO {

    private Long id;
    private String nomeBuild;

    // Componentes convertidos para DTOs internos para evitar recursão de JSON
    private ComponenteDTO cpu;
    private ComponenteDTO placaMae;
    private ComponenteDTO gpu;
    private ComponenteDTO memoriaRam;
    private ComponenteDTO armazenamento;
    private ComponenteDTO fonte;
    private ComponenteDTO gabinete;
    private ComponenteDTO refrigeracao;

    private String usoPrincipal;
    private String detalhe;
    private String orcamento;
    private BigDecimal precoTotal;

    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    // Construtor de mapeamento: transforma a entidade rica em uma resposta enxuta.
    public BuildResponseDTO(BuildModel build) {
        this.id = build.getId();
        this.nomeBuild = build.getNomeBuild();

        // Mapeamento manual dos componentes (pode ser substituído por ModelMapper no futuro)
        this.cpu = new ComponenteDTO(build.getCpu().getId(), build.getCpu().getNome(), build.getCpu().getMarca(), build.getCpu().getPreco());
        this.placaMae = new ComponenteDTO(build.getPlacaMae().getId(), build.getPlacaMae().getNome(), build.getPlacaMae().getMarca(), build.getPlacaMae().getPreco());

        if (build.getGpu() != null) {
            this.gpu = new ComponenteDTO(build.getGpu().getId(), build.getGpu().getNome(), build.getGpu().getMarca(), build.getGpu().getPreco());
        }

        this.memoriaRam = new ComponenteDTO(build.getMemoriaRam().getId(), build.getMemoriaRam().getNome(), build.getMemoriaRam().getMarca(), build.getMemoriaRam().getPreco());
        this.armazenamento = new ComponenteDTO(build.getArmazenamento().getId(), build.getArmazenamento().getNome(), build.getArmazenamento().getMarca(), build.getArmazenamento().getPreco());
        this.fonte = new ComponenteDTO(build.getFonte().getId(), build.getFonte().getNome(), build.getFonte().getMarca(), build.getFonte().getPreco());
        this.gabinete = new ComponenteDTO(build.getGabinete().getId(), build.getGabinete().getNome(), build.getGabinete().getMarca(), build.getGabinete().getPreco());

        if (build.getRefrigeracao() != null) {
            this.refrigeracao = new ComponenteDTO(build.getRefrigeracao().getId(), build.getRefrigeracao().getNome(), build.getRefrigeracao().getMarca(), build.getRefrigeracao().getPreco());
        }

        this.usoPrincipal = build.getUsoPrincipal();
        this.detalhe = build.getDetalhe();
        this.orcamento = build.getOrcamento();
        this.precoTotal = build.getPrecoTotal();
        this.dataCriacao = build.getDataCriacao();
        this.dataAtualizacao = build.getDataAtualizacao();
    }

    // Sub-DTO para simplificar a exibição dos componentes na UI.
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ComponenteDTO {
        private Long id;
        private String nome;
        private String marca;
        private Double preco;
    }
}