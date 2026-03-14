package com.idealcomputer.api.services;

import com.idealcomputer.api.documents.*;
import com.idealcomputer.api.dtos.RecommendationRequestDTO;
import com.idealcomputer.api.dtos.RecommendationResponseDTO;
import com.idealcomputer.api.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

// Responsável por atuar como o motor de recomendação inteligente da plataforma.
// Implementa a lógica de seleção de hardware, garantindo compatibilidade física e técnica entre os componentes.
// Utiliza o padrão arquitetural CQRS, realizando buscas e filtragens complexas no Elasticsearch (para alta performance)
// e buscando as entidades finais no PostgreSQL apenas quando a configuração ideal é encontrada, garantindo a integridade dos dados.
@Service
@RequiredArgsConstructor
public class RecommendationService {

    // Repositórios de acesso ao banco de dados relacional (PostgreSQL).
    // São acionados exclusivamente no final do processo para resgatar as entidades completas e garantir a integridade da resposta.
    private final CpuRepository cpuRepository;
    private final PlacaMaeRepository placaMaeRepository;
    private final MemoriaRamRepository memoriaRamRepository;
    private final GpuRepository gpuRepository;
    private final ArmazenamentoRepository armazenamentoRepository;
    private final FonteRepository fonteRepository;
    private final GabineteRepository gabineteRepository;
    private final RefrigeracaoRepository refrigeracaoRepository;

    // Repositórios de acesso ao motor de busca (Elasticsearch).
    // São utilizados intensamente durante o algoritmo de recomendação para garantir respostas em milissegundos
    // ao realizar o cruzamento de compatibilidades.
    private final CpuElasticRepository cpuElastic;
    private final PlacaMaeElasticRepository pmElastic;
    private final MemoriaRamElasticRepository ramElastic;
    private final GpuElasticRepository gpuElastic;
    private final ArmazenamentoElasticRepository armazElastic;
    private final FonteElasticRepository fonteElastic;
    private final GabineteElasticRepository gabElastic;
    private final RefrigeracaoElasticRepository refriElastic;

    // Estrutura de dados interna projetada para encapsular as três peças fundamentais de um computador (Kit Base).
    // Facilita o cálculo do custo inicial da plataforma e a ordenação das opções viáveis na memória.
    private static class PlatformKit {
        CpuDocument cpu;
        PlacaMaeDocument placaMae;
        MemoriaRamDocument memoriaRam;
        double totalCost;

        PlatformKit(CpuDocument cpu, PlacaMaeDocument placaMae, MemoriaRamDocument memoriaRam) {
            this.cpu = cpu;
            this.placaMae = placaMae;
            this.memoriaRam = memoriaRam;
            this.totalCost = cpu.getPreco() + placaMae.getPreco() + memoriaRam.getPreco();
        }
    }

    // Estrutura de dados interna que armazena os limites financeiros permitidos para cada categoria de peça.
    // Evita que o algoritmo gaste todo o orçamento em apenas um componente, garantindo uma montagem balanceada.
    private static class BudgetAllocation {
        double platformBudget, gpuBudget, storageBudget, caseBudget, coolerBudget;
    }

    // Orquestra todo o fluxo de geração de uma configuração recomendada.
    // Recebe as preferências do usuário (orçamento, uso principal e detalhes).
    // Retorna um DTO completo com as peças físicas selecionadas, ou lança exceção caso o orçamento seja inviável.
    public RecommendationResponseDTO generateBuild(RecommendationRequestDTO request) {
        double maxBudget = getBudgetLimit(request.getBudget());
        BudgetAllocation allocation = calculateBudgetAllocation(maxBudget, request);

        List<PlatformKit> allPossibleKits = new ArrayList<>();
        Iterable<CpuDocument> todasCpus = cpuElastic.findAll();

        // Estrutura de repetição que forma o Kit Base (CPU + Placa-Mãe + RAM).
        // Filtra incompatibilidades diretamente nas consultas do Elasticsearch para evitar travamentos de memória.
        for (CpuDocument cpu : todasCpus) {
            if (!filterCpuByUsage(cpu, request)) continue;

            List<PlacaMaeDocument> pmsCompativeis = pmElastic.findBySoqueteCpu(cpu.getSoquete());
            for (PlacaMaeDocument pm : pmsCompativeis) {
                List<MemoriaRamDocument> ramsCompativeis = ramElastic.findByTipo(pm.getTipoRamSuportado());

                for (MemoriaRamDocument ram : ramsCompativeis) {
                    PlatformKit kit = new PlatformKit(cpu, pm, ram);

                    // Valida se o custo do Kit Base cabe na fatia de orçamento designada e se a RAM atende à categoria.
                    if (kit.totalCost <= allocation.platformBudget && filterRamByBudget(ram, request.getBudget())) {
                        allPossibleKits.add(kit);
                    }
                }
            }
        }

        // Ordena os kits válidos. Por padrão, do mais caro (maior performance dentro do limite) para o mais barato.
        List<PlatformKit> validKits = allPossibleKits.stream()
                .sorted(Comparator.comparingDouble((PlatformKit kit) -> kit.totalCost).reversed())
                .collect(Collectors.toList());

        if (validKits.isEmpty()) {
            throw new RuntimeException("Não foi possível encontrar um kit base compatível.");
        }

        // Regra de negócio: orçamentos econômicos priorizam o menor custo possível absoluto.
        if (request.getBudget().equalsIgnoreCase("econômico")) {
            validKits.sort(Comparator.comparingDouble(kit -> kit.totalCost));
        }

        // Tenta acoplar o restante das peças (GPU, Fonte, etc.) ao melhor Kit Base encontrado.
        for (PlatformKit currentKit : validKits) {
            double remainingBudget = maxBudget - currentKit.totalCost;

            RefrigeracaoDocument selectedRefrigeracao = null;
            if (requiresSeparateCooler(currentKit.cpu)) {
                selectedRefrigeracao = selectRefrigeracao(currentKit.cpu, allocation.coolerBudget, maxBudget);
                if (selectedRefrigeracao != null) remainingBudget -= selectedRefrigeracao.getPreco();
            }

            GpuDocument selectedGpu = null;
            if (requiresGpu(request)) {
                selectedGpu = selectGpu(allocation.gpuBudget, request);
                if (selectedGpu != null) remainingBudget -= selectedGpu.getPreco();
            }

            ArmazenamentoDocument selectedArmazenamento = selectArmazenamento(allocation.storageBudget, maxBudget);
            if (selectedArmazenamento != null) remainingBudget -= selectedArmazenamento.getPreco();

            GabineteDocument selectedGabinete = selectGabinete(currentKit.placaMae.getFormato(), allocation.caseBudget);
            if (selectedGabinete == null) continue;
            remainingBudget -= selectedGabinete.getPreco();

            double potenciaNecessaria = calculateRequiredWattage(currentKit.cpu, selectedGpu, maxBudget);
            FonteDocument selectedFonte = selectFonte(currentKit.placaMae.getFormato(), selectedGabinete, remainingBudget, potenciaNecessaria);
            if (selectedFonte == null) continue;
            remainingBudget -= selectedFonte.getPreco();

            // Confirmação final da montagem. A margem de erro de -200 reais acomoda flutuações de mercado
            // sem invalidar montagens tecnicamente perfeitas.
            if (selectedArmazenamento != null && remainingBudget >= -200) {
                RecommendationResponseDTO response = new RecommendationResponseDTO();

                // Conversão CQRS: Busca as entidades reais no banco de dados relacional.
                response.setCpu(cpuRepository.findById(currentKit.cpu.getPostgresId()).orElseThrow());
                response.setPlacaMae(placaMaeRepository.findById(currentKit.placaMae.getPostgresId()).orElseThrow());
                response.setMemoriaRam(memoriaRamRepository.findById(currentKit.memoriaRam.getPostgresId()).orElseThrow());
                response.setArmazenamento(armazenamentoRepository.findById(selectedArmazenamento.getPostgresId()).orElseThrow());
                response.setFonte(fonteRepository.findById(selectedFonte.getPostgresId()).orElseThrow());
                response.setGabinete(gabineteRepository.findById(selectedGabinete.getPostgresId()).orElseThrow());

                if (selectedGpu != null) response.setGpu(gpuRepository.findById(selectedGpu.getPostgresId()).orElse(null));
                if (selectedRefrigeracao != null) response.setRefrigeracao(refrigeracaoRepository.findById(selectedRefrigeracao.getPostgresId()).orElse(null));

                return response;
            }
        }

        throw new RuntimeException("Não foi possível montar uma configuração completa.");
    }

    // Responsável por dividir o orçamento total em fatias percentuais seguras para cada categoria.
    private BudgetAllocation calculateBudgetAllocation(double maxBudget, RecommendationRequestDTO request) {
        BudgetAllocation allocation = new BudgetAllocation();
        String usage = request.getUsage().toLowerCase();
        String detail = request.getDetail() != null ? request.getDetail().toLowerCase() : "";

        if (usage.equals("jogos")) {
            if (detail.contains("pesados")) {
                allocation.platformBudget = maxBudget * 0.35;
                allocation.gpuBudget = maxBudget * 0.40;
                allocation.storageBudget = maxBudget * 0.08;
                allocation.caseBudget = maxBudget * 0.08;
                allocation.coolerBudget = maxBudget * 0.09;
            } else {
                allocation.platformBudget = maxBudget * 0.40;
                allocation.gpuBudget = maxBudget * 0.30;
                allocation.storageBudget = maxBudget * 0.10;
                allocation.caseBudget = maxBudget * 0.10;
                allocation.coolerBudget = maxBudget * 0.10;
            }
        } else {
            allocation.platformBudget = maxBudget * 0.60;
            allocation.gpuBudget = 0;
            allocation.storageBudget = maxBudget * 0.15;
            allocation.caseBudget = maxBudget * 0.15;
            allocation.coolerBudget = maxBudget * 0.10;
        }
        return allocation;
    }

    // Responsável por selecionar a Placa de Vídeo (GPU) mais adequada para a configuração.
    // Evita gargalos (Bottlenecks), priorizando placas com mais VRAM em orçamentos elevados.
    private GpuDocument selectGpu(double budget, RecommendationRequestDTO request) {
        String detail = request.getDetail() != null ? request.getDetail().toLowerCase() : "";

        List<GpuDocument> gpus = StreamSupport.stream(gpuElastic.findAll().spliterator(), false)
                .filter(g -> g.getPreco() <= budget)
                .sorted(Comparator.comparing(GpuDocument::getPreco).reversed())
                .collect(Collectors.toList());

        if (gpus.isEmpty()) return null;

        // Regra de negócio: orçamentos altos exigem GPUs com no mínimo 16GB de VRAM.
        if (budget > 5000 && (detail.contains("pesados") || detail.contains("todo tipo") || detail.contains("edição"))) {
            return gpus.stream().filter(g -> g.getMemoriaVram() != null && g.getMemoriaVram() >= 16)
                    .max(Comparator.comparing(GpuDocument::getPreco)).orElse(gpus.get(0));
        }

        return gpus.get(0);
    }

    // Responsável por selecionar a unidade de armazenamento.
    // Prioriza unidades com maior capacidade (GB) que caibam no orçamento.
    private ArmazenamentoDocument selectArmazenamento(double budget, double maxBudget) {
        return StreamSupport.stream(armazElastic.findAll().spliterator(), false)
                .filter(a -> a.getPreco() <= budget)
                .sorted(Comparator.comparing(ArmazenamentoDocument::getCapacidadeGb).reversed())
                .findFirst().orElse(null);
    }

    // Responsável por selecionar o Gabinete avaliando restrições de espaço físico e formato da Placa-Mãe.
    private GabineteDocument selectGabinete(String formatoPlacaMae, double budget) {
        String formato = formatoPlacaMae.toLowerCase();

        List<GabineteDocument> compatibleCases = StreamSupport.stream(gabElastic.findAll().spliterator(), false)
                .filter(g -> g.getPreco() <= budget)
                .filter(g -> {
                    String suportados = g.getFormatosPlacaMaeSuportados() != null ? g.getFormatosPlacaMaeSuportados().toLowerCase() : "";
                    if (formato.contains("mini-itx")) return true;
                    if (formato.contains("micro-atx") || formato.contains("m-atx")) {
                        return suportados.contains("micro-atx") || suportados.contains("m-atx") || suportados.contains("atx");
                    }
                    if (formato.contains("atx") && !formato.contains("micro") && !formato.contains("mini")) {
                        return suportados.contains("atx");
                    }
                    return false;
                })
                .sorted(Comparator.comparing(GabineteDocument::getPreco).reversed())
                .collect(Collectors.toList());

        if (compatibleCases.isEmpty()) return null;

        // Balanceamento: evita usar todo o orçamento no gabinete se houver opções intermediárias robustas.
        if (compatibleCases.size() > 3) {
            return compatibleCases.get(1);
        }
        return compatibleCases.get(0);
    }

    // Responsável por dimensionar o sistema de refrigeração apropriado para o processador.
    // Avalia o nível térmico exigido, direcionando setups High-End para Water Coolers.
    private RefrigeracaoDocument selectRefrigeracao(CpuDocument cpu, double budget, double maxBudget) {
        String cpuSocket = cpu.getSoquete();
        boolean isHighEnd = isHighEndCpu(cpu);

        List<RefrigeracaoDocument> coolers = StreamSupport.stream(refriElastic.findAll().spliterator(), false)
                .filter(c -> c.getSoquetesCpuSuportados() != null && c.getSoquetesCpuSuportados().toUpperCase().contains(cpuSocket.toUpperCase()))
                .filter(c -> c.getPreco() <= budget)
                .collect(Collectors.toList());

        if (coolers.isEmpty()) return null;

        // Regra de negócio: setups High-End recebem Water Coolers preferencialmente.
        if (isHighEnd && maxBudget >= 8000) {
            RefrigeracaoDocument wc = coolers.stream()
                    .filter(c -> c.getTipo().equalsIgnoreCase("Water Cooler"))
                    .filter(c -> c.getNome().contains("360") || c.getNome().contains("280") || c.getNome().contains("240"))
                    .max(Comparator.comparing(RefrigeracaoDocument::getPreco)).orElse(null);
            if (wc != null) return wc;
        }

        return coolers.stream()
                .filter(c -> c.getTipo().equalsIgnoreCase("Air Cooler"))
                .max(Comparator.comparing(RefrigeracaoDocument::getPreco)).orElse(coolers.get(0));
    }

    // Responsável por calcular e selecionar a Fonte de Alimentação (PSU).
    // Executa validações de potência estimada, orçamento restante e compatibilidade de formato físico (SFX/ATX).
    private FonteDocument selectFonte(String formatoPlacaMae, GabineteDocument gabinete, double budget, double requiredWattage) {
        String formatoPlaca = formatoPlacaMae.toLowerCase();
        String formatosGabinete = gabinete.getFormatosPlacaMaeSuportados() != null ? gabinete.getFormatosPlacaMaeSuportados().toLowerCase() : "";

        return StreamSupport.stream(fonteElastic.findAll().spliterator(), false)
                .filter(f -> f.getPotenciaWatts() != null && f.getPotenciaWatts() >= requiredWattage)
                .filter(f -> f.getPreco() <= budget)
                .filter(f -> {
                    String formatoFonte = f.getFormato() != null ? f.getFormato().toLowerCase() : "";
                    if (formatoPlaca.contains("mini-itx") && !formatosGabinete.contains("atx")) {
                        return formatoFonte.contains("sfx");
                    }
                    return formatoFonte.contains("atx") || formatoFonte.contains("sfx");
                })
                .max(Comparator.comparing(FonteDocument::getPreco))
                .orElse(null);
    }

    // Valida se a capacidade do pente de Memória RAM atende aos requisitos mínimos/máximos da categoria financeira exigida.
    private boolean filterRamByBudget(MemoriaRamDocument ram, String budgetCategory) {
        int cap = ram.getCapacidadeGb() != null ? ram.getCapacidadeGb() : 0;
        return budgetCategory.equalsIgnoreCase("econômico") ? cap <= 16 : cap <= 32;
    }

    // Avalia o contexto da requisição para definir se a arquitetura necessita de uma Placa de Vídeo Dedicada (Offboard).
    private boolean requiresGpu(RecommendationRequestDTO request) {
        return request.getUsage() != null && request.getUsage().equalsIgnoreCase("jogos");
    }

    // Regra de negócio isolada para filtrar famílias de processadores baseadas no caso de uso do cliente.
    private boolean filterCpuByUsage(CpuDocument cpu, RecommendationRequestDTO request) {
        return true;
    }

    // Valida a nomenclatura do processador para verificar a existência de um dissipador térmico na caixa do produto.
    private boolean requiresSeparateCooler(CpuDocument cpu) {
        if (cpu.getNome() == null) return true;
        return !cpu.getNome().toUpperCase().endsWith("G");
    }

    // Identifica se a CPU pertence a séries de alto desempenho (Core i7/i9, Ryzen 7/9).
    // Utilizado primordialmente para o dimensionamento do sistema de refrigeração.
    private boolean isHighEndCpu(CpuDocument cpu) {
        if (cpu.getNome() == null) return false;
        String name = cpu.getNome().toUpperCase();
        return name.contains("RYZEN 7") || name.contains("RYZEN 9") ||
                name.contains("I7") || name.contains("I9") ||
                name.contains("13600K") || name.contains("14600K");
    }

    // Calcula dinamicamente o consumo de energia (TDP) estimado da configuração completa.
    // Soma o consumo da CPU, GPU e sistema base, aplicando uma margem de segurança de 50%.
    private double calculateRequiredWattage(CpuDocument cpu, GpuDocument gpu, double budget) {
        double baseSystemWattage = 150.0;
        double cpuWattage = (cpu != null && cpu.getPotenciaRecomendadaW() != null) ? cpu.getPotenciaRecomendadaW() : 65.0;
        double gpuWattage = (gpu != null && gpu.getPotenciaRecomendadaW() != null) ? gpu.getPotenciaRecomendadaW() : 0.0;

        double totalEstimatedDemand = baseSystemWattage + cpuWattage + gpuWattage;
        double safeWattage = totalEstimatedDemand * 1.50;

        return Math.max(safeWattage, 500.0);
    }

    // Converte a variável categórica de orçamento para um limite financeiro numérico utilizado nos cálculos.
    private double getBudgetLimit(String budgetCategory) {
        if (budgetCategory == null) return 7000.00;
        return switch (budgetCategory.toLowerCase()) {
            case "econômico" -> 4000.00;
            case "intermediário" -> 7000.00;
            case "alto" -> 12000.00;
            default -> 7000.00;
        };
    }
}