package br.com.sosescolar.Controller;

import br.com.sosescolar.Config.JwtUtil;
import br.com.sosescolar.DTO.AuthenticationRequest;
import br.com.sosescolar.DTO.AuthenticationResponse;
import br.com.sosescolar.DTO.ProfessorSignUpRequest;
import br.com.sosescolar.Service.AuthService;
import br.com.sosescolar.Service.MyUserDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final MyUserDetailsService userDetailsService;
    private final AuthService authService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
                          MyUserDetailsService userDetailsService, AuthService authService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.authService = authService;
    }

    @PostMapping("/professor/register")
    public ResponseEntity<?> registerProfessor(@RequestBody ProfessorSignUpRequest signUpRequest) {
        try {
            authService.registerProfessor(signUpRequest);
            return ResponseEntity.ok("Professor registrado com sucesso!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/professor/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getSenha())
            );
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Email ou senha incorretos");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}