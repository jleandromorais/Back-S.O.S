package br.com.sosescolar.Config; // Ou onde quer que seu filtro esteja

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    // Você precisa de um serviço que busque os detalhes do usuário
    // (pode ser AlunoService ou ProfessorService, desde que implementem UserDetailsService)
    @Autowired
    private UserDetailsService userDetailsService;

    // Lista de rotas públicas que o filtro deve IGNORAR
    private static final List<String> PUBLIC_PATHS = Arrays.asList(
            "/api/auth/login",
            "/api/auth/professor/register",
            "/api/auth/aluno/register",
            "/api/denuncias" // Ignora POST /api/denuncias
    );

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        final String requestURI = request.getRequestURI();
        final String method = request.getMethod();

        // --- ESTA É A LÓGICA DE CORREÇÃO ---

        // 1. Verifica se é uma rota pública da lista
        boolean isPublic = PUBLIC_PATHS.stream().anyMatch(requestURI::equals);

        // 2. Verifica o caso especial do GET /api/denuncias/{protocolo}
        if (!isPublic && requestURI.startsWith("/api/denuncias/") && method.equals("GET")) {
            isPublic = true;
        }

        // 3. Se for pública, pula toda a lógica de validação
        if (isPublic) {
            filterChain.doFilter(request, response);
            return;
        }

        // --- FIM DA LÓGICA DE CORREÇÃO ---


        // Se não for pública, o filtro continua a validação (seu código original)
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return; // Sai se não houver token
        }

        jwt = authHeader.substring(7);

        try {
            username = jwtUtil.extractUsername(jwt);
        } catch (Exception e) {
            // Se o token for inválido (expirado, assinatura errada), rejeita
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token JWT inválido ou expirado.");
            return;
        }


        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}