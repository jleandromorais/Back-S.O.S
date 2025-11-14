package br.com.sosescolar.controller;

import br.com.sosescolar.DTO.DenunciaDTO;
import br.com.sosescolar.Enum.TipoDeDenun;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser // Adicionado para simular um usuário autenticado (necessário para o Controller)
public class DenunciaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Teste 3: Verificar se o endpoint POST de criação de denúncia retorna 201 (CREATED)
    @Test
    void deveCriarDenunciaERetornarStatus201() throws Exception {
        // Arrange
        DenunciaDTO denunciaDTO = new DenunciaDTO();
        denunciaDTO.setTipoDenuncia(TipoDeDenun.BULLYING);
        denunciaDTO.setDescricaoOcorrencia("Teste de integração simples");
        denunciaDTO.setLocalOcorrencia("Pátio da escola");
        denunciaDTO.setIdentificacao(false);
        denunciaDTO.setDataOcorrencia(true);

        String denunciaJson = objectMapper.writeValueAsString(denunciaDTO);

        // Act & Assert
        mockMvc.perform(post("/api/denuncias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(denunciaJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.protocolo").exists());
    }
}
