package com.idealcomputer.api.config;

import com.idealcomputer.api.services.ElasticSyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Classe de configuração executada automaticamente durante a inicialização da aplicação.
// Resolve o problema de consistência de dados, garantindo que o motor de busca (Elasticsearch)
// inicie sempre com os dados mais recentes copiados do banco de dados principal (PostgreSQL).
@Slf4j // Cria automaticamente uma ferramenta de log (fornecida pelo Lombok) para registrar mensagens no console.
@Configuration // Informa ao Spring que esta classe contém definições de componentes que devem ser carregados.
public class SyncRunnerConfig {

    // Cria um comando (CommandLineRunner) que o Spring executa logo após o servidor iniciar.
    // Depende do ElasticSyncService para realizar a lógica de cópia dos dados.
    @Bean
    public CommandLineRunner runInitialSync(ElasticSyncService syncService) {
        return args -> {

            // Registra um aviso no terminal informando que o processo começou.
            log.info("⏳ Iniciando a carga de dados Postgres -> Elasticsearch...");

            // Aciona o serviço para buscar os registros no banco e enviar ao Elasticsearch.
            syncService.syncAll();
        };
    }
}