package com.idealcomputer.api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Filtro de segurança executado uma única vez a cada requisição HTTP recebida pela API.
// Faz parte da camada de segurança (Spring Security).
// Resolve o problema de autenticação stateless (sem estado), capturando o token JWT
// enviado pelo cliente e validando a identidade do usuário antes de liberar o acesso aos endpoints.
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    // Dependência utilizada para manipular, extrair dados e validar a assinatura do token JWT.
    private final JwtUtil jwtUtil;

    // Dependência utilizada para buscar os dados oficiais do usuário no banco de dados.
    private final UserDetailsService userDetailsService;

    // Método principal do filtro. Inspeciona a requisição, tenta realizar a autenticação
    // e decide se o fluxo deve continuar.
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // Verifica a presença e o formato correto do cabeçalho de autorização.
        // Caso o token não seja enviado ou não utilize o padrão "Bearer ", a requisição segue adiante sem autenticação.
        // A decisão de bloquear ou permitir o acesso "anônimo" fica a cargo da configuração central de rotas (SecurityConfig).
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Isola a string do token removendo o prefixo "Bearer " (os 7 primeiros caracteres).
            // Em seguida, extrai o e-mail do usuário embutido dentro do token.
            jwt = authHeader.substring(7);
            userEmail = jwtUtil.extractUsername(jwt);

            // Verifica se um e-mail foi encontrado e se o contexto de segurança atual ainda não possui uma autenticação ativa.
            // Isso evita reprocessamento desnecessário caso a requisição já tenha sido autenticada em outra etapa.
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // Busca as informações completas do usuário (incluindo suas permissões/roles) no banco de dados.
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                // Valida matematicamente o token contra os dados do usuário e verifica sua data de expiração.
                if (jwtUtil.validateToken(jwt, userDetails)) {

                    // Cria um objeto de autenticação oficial do Spring Security, contendo as credenciais e as permissões aprovadas.
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    // Anexa detalhes técnicos adicionais da requisição (como endereço IP e sessão) ao objeto de autenticação.
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    // Registra o usuário como oficialmente autenticado no contexto de segurança da aplicação.
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            // Libera o fluxo da requisição, permitindo que ela alcance o próximo filtro de segurança ou o endpoint final.
            filterChain.doFilter(request, response);

        } catch (Exception ex) {
            // Captura falhas de manipulação do token (como token expirado, assinatura inválida ou malformado).
            // Interrompe o fluxo da requisição imediatamente e devolve o status HTTP 401 (Não Autorizado),
            // evitando que o erro estoure na aplicação e gere um status 500.
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Token invalido ou expirado\"}");
            response.getWriter().flush();
        }
    }
}