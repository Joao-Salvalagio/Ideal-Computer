package com.idealcomputer.api.security;

import com.idealcomputer.api.models.UserModel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

// Classe utilitária responsável pela manipulação de tokens JWT (JSON Web Token).
// Resolve o problema de criação, validação e extração de dados de credenciais.
// Essencial para o funcionamento do modelo stateless, permitindo que o servidor confie
// nas requisições do cliente sem armazenar sessões na memória.
// Depende da biblioteca 'jjwt' para as operações criptográficas.
@Component
public class JwtUtil {

    // Chave secreta usada para assinar e verificar a autenticidade dos tokens.
    private final SecretKey SECRET_KEY;

    // Tempo de vida útil do token em milissegundos.
    private final long EXPIRATION_TIME;

    // Construtor que injeta configurações externas (application.properties).
    // Transforma a string do segredo em uma chave criptográfica HMAC-SHA compatível com a biblioteca jjwt.
    public JwtUtil(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration}") long expiration) {
        this.SECRET_KEY = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.EXPIRATION_TIME = expiration;
    }

    // Gera um novo token JWT contendo informações úteis do usuário (Claims).
    // Embutir dados como nome e nível de acesso no token evita que o frontend precise
    // fazer requisições extras ao banco de dados para montar a interface gráfica.
    public String generateToken(UserModel userModel) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("nome", userModel.getName());
        claims.put("cargo", userModel.getCargo());
        claims.put("funcao", userModel.getFuncao().name());

        return createToken(claims, userModel.getEmail());
    }

    // Constrói fisicamente o token JWT.
    // Define o corpo de dados (claims), o identificador principal (subject - e-mail),
    // as datas de emissão e expiração, e aplica a assinatura digital usando o algoritmo HS256.
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    // Valida a integridade e a vigência do token.
    // Confirma se o e-mail embutido no token corresponde ao e-mail do usuário processado no momento
    // e garante que a data de expiração ainda não foi atingida.
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Extrai o identificador principal (e-mail) criptografado dentro do token.
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extrai a data e hora exata em que o token perderá a validade.
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Compara a data de expiração do token com a data atual do sistema.
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Método genérico responsável por isolar e retornar uma informação específica (Claim) do token.
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Descriptografa o token por completo utilizando a chave secreta da aplicação.
    // Se o token tiver sido adulterado por terceiros, a assinatura matemática falhará nesta etapa.
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}