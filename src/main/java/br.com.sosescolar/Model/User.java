package br.com.sosescolar.Model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    @Column(unique = true, nullable = false)
    private String email;
    private String senha;
    private String role;

    // <-- O CAMPO 'matricula' FOI REMOVIDO DESTA CLASSE -->


    // Métodos de UserDetails

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return switch (this.role) {
            case "ADMIN" -> List.of(
                    new SimpleGrantedAuthority("ROLE_ADMIN"),
                    new SimpleGrantedAuthority("ROLE_USER"),
                    new SimpleGrantedAuthority("ROLE_ALUNO"),
                    new SimpleGrantedAuthority("ROLE_PROFESSOR")
            );
            case "PROFESSOR" -> List.of(
                    new SimpleGrantedAuthority("ROLE_PROFESSOR"),
                    new SimpleGrantedAuthority("ROLE_USER")
            );
            case "ALUNO" -> List.of(new SimpleGrantedAuthority("ROLE_USER"));
            default -> List.of(new SimpleGrantedAuthority("ROLE_USER"));
        };
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    // Métodos de status (todos true por padrão)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}