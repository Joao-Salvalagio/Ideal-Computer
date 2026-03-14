package com.idealcomputer.api.security;

import com.idealcomputer.api.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

// Classe central de configuração de segurança da aplicação.
// Responsável por ditar as regras do Spring Security, definindo quais rotas são públicas,
// quais exigem autenticação e quais exigem privilégios de administrador.
// Resolve o problema de proteção da API contra acessos não autorizados e configura a
// comunicação segura com aplicações externas (CORS).
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // Dependência do filtro personalizado criado para interceptar e validar tokens JWT.
    private final JwtAuthFilter jwtAuthFilter;

    // Define o algoritmo responsável por criptografar e verificar as senhas do sistema.
    // O BCryptPasswordEncoder é o padrão de mercado, aplicando "salt" (dados aleatórios)
    // nas senhas para evitar ataques de força bruta e garantindo que a senha original
    // nunca seja salva em texto limpo no banco de dados.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Expõe o gerenciador de autenticação nativo do Spring Security.
    // Utilizado pelo controlador de login (AuthController) para processar o e-mail e senha
    // recebidos e verificar se correspondem a um usuário válido no banco de dados.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Configura a política de CORS (Cross-Origin Resource Sharing).
    // Resolve o problema de bloqueio de requisições imposto pelos navegadores quando uma
    // aplicação web (frontend) tenta acessar uma API hospedada em um domínio/porta diferente.
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Permite explicitamente que a aplicação Angular (rodando na porta 4200) acesse a API.
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));

        // Define quais métodos HTTP (verbos) o frontend tem permissão para executar.
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

        // Autoriza o envio de cabeçalhos essenciais, como o token de autenticação e o formato JSON.
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        // Permite o tráfego de credenciais (cookies, tokens de autorização) na requisição cruzada.
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplica essas regras de liberação para absolutamente todos os endpoints da API ("/**").
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // Configura a esteira de filtros de segurança (SecurityFilterChain) que toda requisição deve atravessar.
    // Aqui é definido o comportamento global da segurança HTTP, desde desativação de proteções
    // desnecessárias até o mapeamento detalhado de permissões por URL.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Ativa a configuração de CORS definida no método corsConfigurationSource().
                .cors(Customizer.withDefaults())

                // Desativa a proteção CSRF (Cross-Site Request Forgery).
                // Como a API utiliza tokens JWT (stateless) em vez de cookies de sessão,
                // essa proteção padrão do Spring se torna desnecessária e causaria bloqueios indevidos.
                .csrf(csrf -> csrf.disable())

                // Inicia o bloco de configuração de autorização de rotas.
                .authorizeHttpRequests(auth -> auth

                        // Rotas públicas: Liberadas para qualquer usuário, sem necessidade de token.
                        // Usadas para login, cadastro inicial e visualização de recomendações.
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/recommendations/**").permitAll()

                        // Rotas de documentação: Liberadas para permitir o acesso ao Swagger UI.
                        .requestMatchers("/v3/api-docs", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/error").permitAll()

                        // Rotas administrativas: Restritas exclusivamente para usuários com nível de acesso superior.
                        // Exigem um token válido E que a role vinculada seja "ADMINISTRADOR".
                        // Utiliza o Enum de forma dinâmica para evitar erros de digitação (Hardcoded strings).
                        .requestMatchers("/api/usuarios/**").hasAuthority(UserRole.ADMINISTRADOR.name())
                        .requestMatchers("/api/cpus/**").hasAuthority(UserRole.ADMINISTRADOR.name())
                        .requestMatchers("/api/gpus/**").hasAuthority(UserRole.ADMINISTRADOR.name())
                        .requestMatchers("/api/placas-mae/**").hasAuthority(UserRole.ADMINISTRADOR.name())
                        .requestMatchers("/api/memorias-ram/**").hasAuthority(UserRole.ADMINISTRADOR.name())
                        .requestMatchers("/api/armazenamentos/**").hasAuthority(UserRole.ADMINISTRADOR.name())
                        .requestMatchers("/api/fontes/**").hasAuthority(UserRole.ADMINISTRADOR.name())
                        .requestMatchers("/api/gabinetes/**").hasAuthority(UserRole.ADMINISTRADOR.name())
                        .requestMatchers("/api/refrigeracoes/**").hasAuthority(UserRole.ADMINISTRADOR.name())
                        .requestMatchers("/api/elastic-sync/**").hasAuthority(UserRole.ADMINISTRADOR.name())

                        // Regra de fallback (segurança por padrão):
                        // Qualquer outra rota não listada acima exigirá, no mínimo, um token de autenticação válido.
                        .anyRequest().authenticated()
                )

                // Altera o gerenciamento de sessões para o modo "STATELESS" (sem estado).
                // Informa ao Spring que ele não deve guardar sessões na memória do servidor,
                // obrigando que toda requisição envie o token JWT para ser validada de forma independente.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Registra o filtro personalizado (JwtAuthFilter) na esteira de segurança.
                // O filtro é posicionado estrategicamente antes do filtro padrão de login do Spring,
                // garantindo que o token JWT seja interceptado e processado logo no início da requisição.
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}