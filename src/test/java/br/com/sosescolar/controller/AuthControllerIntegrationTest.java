package br.com.sosescolar.controller;

import br.com.sosescolar.DTO.AuthenticationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Teste 2: Verificar se a autenticação falha com credenciais inválidas
    @Test
    void deveRetornar401ComCredenciaisInvalidas() throws Exception {
        // Arrange
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("usuario@inexistente.com");
        request.setSenha("senhaerrada");

        String authJson = objectMapper.writeValueAsString(request);

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authJson))
                .andExpect(status().isUnauthorized()); // Espera-se 401 Unauthorized
    }
}
