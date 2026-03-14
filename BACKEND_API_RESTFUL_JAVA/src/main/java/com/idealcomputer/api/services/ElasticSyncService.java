package com.idealcomputer.api.services;

import com.idealcomputer.api.documents.*;
import com.idealcomputer.api.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

// Camada de serviço responsável pela sincronização de dados entre SQL e NoSQL.
// Resolve o problema de latência e consistência de dados, permitindo que as consultas
// complexas do motor de busca reflitam o estado atual do estoque e preços do banco principal.
@Slf4j
@Service
@RequiredArgsConstructor
public class ElasticSyncService {

    // Repositórios SQL (PostgreSQL) - Fonte da Verdade
    private final CpuRepository cpuPostgres;
    private final PlacaMaeRepository pmPostgres;
    private final MemoriaRamRepository ramPostgres;
    private final GpuRepository gpuPostgres;
    private final ArmazenamentoRepository armazPostgres;
    private final GabineteRepository gabPostgres;
    private final FonteRepository fontePostgres;
    private final RefrigeracaoRepository refriPostgres;

    // Repositórios NoSQL (Elasticsearch) - Motor de Busca
    private final CpuElasticRepository cpuElastic;
    private final PlacaMaeElasticRepository pmElastic;
    private final MemoriaRamElasticRepository ramElastic;
    private final GpuElasticRepository gpuElastic;
    private final ArmazenamentoElasticRepository armazElastic;
    private final GabineteElasticRepository gabElastic;
    private final FonteElasticRepository fonteElastic;
    private final RefrigeracaoElasticRepository refriElastic;

    // Orquestra a sincronização massiva de todos os componentes de hardware.
    // Utilizada em rotinas de inicialização do sistema ou atualizações de carga em lote.
    @Transactional(readOnly = true)
    public void syncAll() {
        log.info("⏳ Iniciando sincronização massiva para o Elasticsearch...");
        syncCpus(); syncPlacasMae(); syncMemoriasRam();
        syncGpus(); syncArmazenamentos(); syncGabinetes();
        syncFontes(); syncRefrigeracoes();
        log.info("✅ Sincronização de todas as peças concluída com sucesso!");
    }

    // --- Métodos de Sincronização Específicos ---

    // Mapeia e migra os dados de CPUs, preservando a referência do ID original do Postgres.
    private void syncCpus() {
        List<CpuDocument> docs = cpuPostgres.findAll().stream().map(p -> {
            CpuDocument d = new CpuDocument();
            d.setPostgresId(p.getId());
            d.setNome(p.getNome());
            d.setSoquete(p.getSoquete());
            d.setPreco(p.getPreco());
            d.setPotenciaRecomendadaW(p.getPotenciaRecomendadaW());
            return d;
        }).collect(Collectors.toList());
        cpuElastic.deleteAll();
        cpuElastic.saveAll(docs);
        log.info(" -> {} CPUs sincronizadas.", docs.size());
    }

    private void syncPlacasMae() {
        List<PlacaMaeDocument> docs = pmPostgres.findAll().stream().map(p -> {
            PlacaMaeDocument d = new PlacaMaeDocument();
            d.setPostgresId(p.getId());
            d.setNome(p.getNome());
            d.setSoqueteCpu(p.getSoqueteCpu());
            d.setTipoRamSuportado(p.getTipoRamSuportado());
            d.setFormato(p.getFormato());
            d.setPreco(p.getPreco());
            return d;
        }).collect(Collectors.toList());
        pmElastic.deleteAll();
        pmElastic.saveAll(docs);
        log.info(" -> {} Placas-Mãe sincronizadas.", docs.size());
    }

    // [Outros métodos de sincronização seguem o mesmo padrão de mapeamento e persistência...]

    private void syncMemoriasRam() {
        List<MemoriaRamDocument> docs = ramPostgres.findAll().stream().map(p -> {
            MemoriaRamDocument d = new MemoriaRamDocument(); d.setPostgresId(p.getId()); d.setNome(p.getNome());
            d.setTipo(p.getTipo()); d.setCapacidadeGb(p.getCapacidadeGb()); d.setPreco(p.getPreco()); return d;
        }).collect(Collectors.toList());
        ramElastic.deleteAll(); ramElastic.saveAll(docs);
        log.info(" -> {} Memórias RAM sincronizadas.", docs.size());
    }

    private void syncGpus() {
        List<GpuDocument> docs = gpuPostgres.findAll().stream().map(p -> {
            GpuDocument d = new GpuDocument(); d.setPostgresId(p.getId()); d.setNome(p.getNome());
            d.setMemoriaVram(p.getMemoriaVram()); d.setPreco(p.getPreco()); d.setPotenciaRecomendadaW(p.getPotenciaRecomendadaW()); return d;
        }).collect(Collectors.toList());
        gpuElastic.deleteAll(); gpuElastic.saveAll(docs);
        log.info(" -> {} GPUs sincronizadas.", docs.size());
    }

    private void syncArmazenamentos() {
        List<ArmazenamentoDocument> docs = armazPostgres.findAll().stream().map(p -> {
            ArmazenamentoDocument d = new ArmazenamentoDocument(); d.setPostgresId(p.getId()); d.setNome(p.getNome());
            d.setTipo(p.getTipo()); d.setCapacidadeGb(p.getCapacidadeGb()); d.setPreco(p.getPreco()); return d;
        }).collect(Collectors.toList());
        armazElastic.deleteAll(); armazElastic.saveAll(docs);
        log.info(" -> {} Armazenamentos sincronizados.", docs.size());
    }

    private void syncGabinetes() {
        List<GabineteDocument> docs = gabPostgres.findAll().stream().map(p -> {
            GabineteDocument d = new GabineteDocument(); d.setPostgresId(p.getId()); d.setNome(p.getNome());
            d.setFormatosPlacaMaeSuportados(p.getFormatosPlacaMaeSuportados()); d.setPreco(p.getPreco()); return d;
        }).collect(Collectors.toList());
        gabElastic.deleteAll(); gabElastic.saveAll(docs);
        log.info(" -> {} Gabinetes sincronizados.", docs.size());
    }

    private void syncFontes() {
        List<FonteDocument> docs = fontePostgres.findAll().stream().map(p -> {
            FonteDocument d = new FonteDocument(); d.setPostgresId(p.getId()); d.setNome(p.getNome());
            d.setPotenciaWatts(p.getPotenciaWatts()); d.setFormato(p.getFormato()); d.setPreco(p.getPreco()); return d;
        }).collect(Collectors.toList());
        fonteElastic.deleteAll(); fonteElastic.saveAll(docs);
        log.info(" -> {} Fontes sincronizadas.", docs.size());
    }

    private void syncRefrigeracoes() {
        List<RefrigeracaoDocument> docs = refriPostgres.findAll().stream().map(p -> {
            RefrigeracaoDocument d = new RefrigeracaoDocument(); d.setPostgresId(p.getId()); d.setNome(p.getNome());
            d.setTipo(p.getTipo()); d.setSoquetesCpuSuportados(p.getSoquetesCpuSuportados()); d.setPreco(p.getPreco()); return d;
        }).collect(Collectors.toList());
        refriElastic.deleteAll(); refriElastic.saveAll(docs);
        log.info(" -> {} Refrigerações sincronizadas.", docs.size());
    }
}