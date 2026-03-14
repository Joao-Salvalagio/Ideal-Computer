package com.idealcomputer.api.services;

import com.idealcomputer.api.dtos.BuildRequestDTO;
import com.idealcomputer.api.dtos.BuildResponseDTO;
import com.idealcomputer.api.models.*;
import com.idealcomputer.api.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

// Camada de serviço mestre responsável pela orquestração das montagens de computadores.
// Resolve o problema de integridade referencial entre múltiplos componentes de hardware,
// garantindo que cada build salva possua componentes válidos, preços calculados e vínculo com o usuário.
@Service
@RequiredArgsConstructor
public class BuildService {

    private final BuildRepository buildRepository;
    private final UserRepository userRepository;
    private final CpuRepository cpuRepository;
    private final PlacaMaeRepository placaMaeRepository;
    private final GpuRepository gpuRepository;
    private final MemoriaRamRepository memoriaRamRepository;
    private final ArmazenamentoRepository armazenamentoRepository;
    private final FonteRepository fonteRepository;
    private final GabineteRepository gabineteRepository;
    private final RefrigeracaoRepository refrigeracaoRepository;

    // Transforma um DTO de requisição em uma entidade persistida, validando todos os componentes.
    // Resolve o problema de conversão de tipos e centraliza o cálculo financeiro da montagem.
    @Transactional
    public BuildResponseDTO salvarBuild(BuildRequestDTO dto, String emailUsuario) {
        UserModel usuario = userRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Busca exaustiva de componentes (Fail-fast: lança exceção no primeiro erro encontrado)
        CpuModel cpu = cpuRepository.findById(dto.getIdCpu())
                .orElseThrow(() -> new RuntimeException("CPU não encontrada"));
        PlacaMaeModel placaMae = placaMaeRepository.findById(dto.getIdPlacaMae())
                .orElseThrow(() -> new RuntimeException("Placa-mãe não encontrada"));
        GpuModel gpu = dto.getIdGpu() != null ? gpuRepository.findById(dto.getIdGpu()).orElse(null) : null;
        MemoriaRamModel memoriaRam = memoriaRamRepository.findById(dto.getIdMemoriaRam())
                .orElseThrow(() -> new RuntimeException("Memória RAM não encontrada"));
        ArmazenamentoModel armazenamento = armazenamentoRepository.findById(dto.getIdArmazenamento())
                .orElseThrow(() -> new RuntimeException("Armazenamento não encontrado"));
        FonteModel fonte = fonteRepository.findById(dto.getIdFonte())
                .orElseThrow(() -> new RuntimeException("Fonte não encontrada"));
        GabineteModel gabinete = gabineteRepository.findById(dto.getIdGabinete())
                .orElseThrow(() -> new RuntimeException("Gabinete não encontrado"));
        RefrigeracaoModel refrigeracao = dto.getIdRefrigeracao() != null ? refrigeracaoRepository.findById(dto.getIdRefrigeracao()).orElse(null) : null;

        BigDecimal precoTotal = calcularPrecoTotal(cpu, placaMae, gpu, memoriaRam, armazenamento, fonte, gabinete, refrigeracao);

        BuildModel build = BuildModel.builder()
                .nomeBuild(dto.getNomeBuild())
                .usuario(usuario)
                .cpu(cpu)
                .placaMae(placaMae)
                .gpu(gpu)
                .memoriaRam(memoriaRam)
                .armazenamento(armazenamento)
                .fonte(fonte)
                .gabinete(gabinete)
                .refrigeracao(refrigeracao)
                .usoPrincipal(dto.getUsoPrincipal())
                .detalhe(dto.getDetalhe())
                .orcamento(dto.getOrcamento())
                .precoTotal(precoTotal)
                .build();

        return new BuildResponseDTO(buildRepository.save(build));
    }

    // Gerencia a listagem de builds do usuário com suporte a filtros e paginação.
    // Resolve o problema de performance no frontend ao permitir carregamento sob demanda via Angular.
    @Transactional(readOnly = true)
    public Page<BuildResponseDTO> listarMinhasBuilds(String emailUsuario, String nome, Pageable pageable) {
        UserModel usuario = userRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Page<BuildModel> builds;
        if (nome != null && !nome.isBlank()) {
            builds = buildRepository.findByUsuarioAndNomeBuildContainingIgnoreCase(usuario, nome, pageable);
        } else {
            builds = buildRepository.findByUsuarioOrderByDataCriacaoDesc(usuario, pageable);
        }

        return builds.map(BuildResponseDTO::new);
    }

    // Localiza uma build específica validando a propriedade do recurso.
    // Garante que um usuário não consiga visualizar ou manipular montagens de terceiros.
    @Transactional(readOnly = true)
    public BuildResponseDTO buscarBuildPorId(Long id, String emailUsuario) {
        BuildModel build = buildRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Build não encontrada"));

        if (!build.getUsuario().getEmail().equals(emailUsuario)) {
            throw new RuntimeException("Acesso negado: esta build não pertence ao usuário");
        }

        return new BuildResponseDTO(build);
    }

    // Executa a remoção lógica/física da build do banco de dados.
    @Transactional
    public void deletarBuild(Long id, String emailUsuario) {
        BuildModel build = buildRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Build não encontrada"));

        if (!build.getUsuario().getEmail().equals(emailUsuario)) {
            throw new RuntimeException("Acesso negado: não é possível deletar build de outro usuário");
        }

        buildRepository.delete(build);
    }

    // Lógica interna para consolidação de valores financeiros.
    // Resolve a conversão de precisão entre os tipos Double (unidades) e BigDecimal (financeiro).
    private BigDecimal calcularPrecoTotal(CpuModel cpu, PlacaMaeModel placaMae, GpuModel gpu,
                                          MemoriaRamModel memoriaRam, ArmazenamentoModel armazenamento,
                                          FonteModel fonte, GabineteModel gabinete,
                                          RefrigeracaoModel refrigeracao) {
        BigDecimal total = BigDecimal.ZERO;
        total = total.add(BigDecimal.valueOf(cpu.getPreco()));
        total = total.add(BigDecimal.valueOf(placaMae.getPreco()));
        if (gpu != null) total = total.add(BigDecimal.valueOf(gpu.getPreco()));
        total = total.add(BigDecimal.valueOf(memoriaRam.getPreco()));
        total = total.add(BigDecimal.valueOf(armazenamento.getPreco()));
        total = total.add(BigDecimal.valueOf(fonte.getPreco()));
        total = total.add(BigDecimal.valueOf(gabinete.getPreco()));
        if (refrigeracao != null) total = total.add(BigDecimal.valueOf(refrigeracao.getPreco()));
        return total;
    }
}