package br.com.sosescolar.Config; // (O pacote da sua classe)

// --- NOVOS IMPORTS NECESSÁRIOS ---
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;
import static org.springframework.security.config.Customizer.withDefaults;
// --- FIM DOS NOVOS IMPORTS ---

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtRequestFilter securityFilter;

    public SecurityConfig(JwtRequestFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                // 1. Desabilita CSRF (você já tinha)
                .csrf(csrf -> csrf.disable())

                // 2. HABILITA O CORS (ESTA É A CORREÇÃO)
                // Ele vai procurar pelo Bean "corsConfigurationSource" abaixo
                .cors(withDefaults())

                // 3. Define a política de sessão (você já tinha)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 4. Define as regras de autorização (sem alteração)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/professor/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/aluno/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/denuncias").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/denuncias/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/product").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // --- ESTE É O NOVO BEAN DE CONFIGURAÇÃO DO CORS ---
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Permite requisições de qualquer origem (ex: http://localhost:3000)
        configuration.setAllowedOrigins(Arrays.asList("*"));

        // Permite os métodos HTTP
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));

        // Permite todos os cabeçalhos (incluindo Authorization)
        configuration.setAllowedHeaders(Arrays.asList("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplica esta configuração a TODAS as rotas da sua API
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}