package com.idealcomputer.api.models;

import com.idealcomputer.api.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

// Entidade que representa a tabela de usuários no banco de dados.
// Ao implementar a interface UserDetails, a classe passa a "falar a língua" do Spring Security,
// resolvendo o problema de ter que converter dados de banco para dados de autenticação.
// Isso permite que o usuário autenticado completo (com ID, nome, cargo) fique disponível
// na memória (SecurityContext) durante toda a requisição.
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TB_Usuarios")
public class UserModel implements BaseEntity<Long>, UserDetails {

    // Identificador único do usuário no banco de dados.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_USUARIO")
    private Long id;

    // Nome de exibição do usuário.
    @Column(name = "Nome_USUARIO",  nullable = false)
    private String name;

    // E-mail do usuário. Funciona como o identificador principal (login) para o sistema.
    // A restrição 'unique = true' garante que não existam contas duplicadas.
    @Column(name = "Email_USUARIO",  nullable = false, unique = true)
    private String email;

    // Senha criptografada do usuário.
    // O Lombok gera automaticamente o método getPassword() necessário para o UserDetails.
    @Column(name = "Senha_USUARIO", nullable = false)
    private String password;

    // Texto livre descrevendo a ocupação ou título do usuário (Ex: "Cliente", "Desenvolvedor").
    @Column(name = "Cargo_USUARIO",  nullable = false)
    private String cargo;

    // Define o nível de acesso real do usuário no sistema (Ex: USUARIO ou ADMINISTRADOR).
    @Enumerated(EnumType.STRING)
    @Column(name = "Funcao_USUARIO", nullable = false)
    private UserRole funcao;

    // --- Métodos obrigatórios da interface UserDetails do Spring Security ---

    // Informa ao Spring Security quais são os privilégios/roles deste usuário.
    // Converte a 'funcao' do banco de dados em uma autoridade compreensível pelo framework.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.funcao.name()));
    }

    // Informa ao Spring Security qual campo é utilizado como "login".
    // Neste sistema, o e-mail assume o papel de username.
    @Override
    public String getUsername() {
        return this.email;
    }

    // Indica se a conta do usuário expirou (útil para sistemas com assinaturas com prazo de validade).
    // Retorna true por padrão, pois essa regra de negócio não é utilizada atualmente.
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // Indica se a conta está bloqueada (útil para bloquear usuários após múltiplas tentativas de login falhas).
    // Retorna true por padrão, mantendo a conta sempre desbloqueada.
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // Indica se as credenciais (senha) expiraram (útil para forçar a troca de senha a cada X meses).
    // Retorna true por padrão, indicando que a senha nunca expira.
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // Indica se o usuário está ativo no sistema (útil para "soft delete" ou banimentos temporários).
    // Retorna true por padrão, mantendo o usuário ativo.
    @Override
    public boolean isEnabled() {
        return true;
    }
}