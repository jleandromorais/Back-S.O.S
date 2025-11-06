package br.com.sosescolar.controller;

import br.com.sosescolar.CesarApplication;
import br.com.sosescolar.DTO.DenunciaDTO;
import br.com.sosescolar.Enum.TipoDeDenun;
import br.com.sosescolar.Model.Denuncia;
import br.com.sosescolar.Repository.DenunciaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de Integração para DenunciaController
 * Testam os endpoints da API com o contexto Spring completo
 */
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = CesarApplication.class
)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser
@Transactional
@DisplayName("Testes de Integração - DenunciaController")
class DenunciaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DenunciaRepository denunciaRepository;

    private DenunciaDTO denunciaDTOValida;

    @BeforeEach
    void setUp() {
        denunciaRepository.deleteAll();
        
        denunciaDTOValida = new DenunciaDTO();
        denunciaDTOValida.setTipoDenuncia(TipoDeDenun.BULLYING);
        denunciaDTOValida.setLocalOcorrencia("Sala 201");
        denunciaDTOValida.setDescricaoOcorrencia("Aluno sendo intimidado durante o intervalo");
        denunciaDTOValida.setIdentificacao(false);
        denunciaDTOValida.setDataOcorrencia(true);
    }

    @Test
    @DisplayName("POST /api/denuncias - Deve criar denúncia e retornar 201 CREATED")
    void deveCriarDenunciaERetornar201() throws Exception {
        mockMvc.perform(post("/api/denuncias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(denunciaDTOValida)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.protocolo").isNotEmpty())
                .andExpect(jsonPath("$.situacao").value("Recebida"))
                .andExpect(jsonPath("$.tipoDenuncia").value("BULLYING"))
                .andExpect(jsonPath("$.localOcorrencia").value("Sala 201"))
                .andExpect(jsonPath("$.identificacao").value(false));
    }

    @Test
    @DisplayName("POST /api/denuncias - Deve criar denúncia identificada com nome do aluno")
    void deveCriarDenunciaIdentificadaComNome() throws Exception {
        denunciaDTOValida.setIdentificacao(true);
        denunciaDTOValida.setNomeAluno("Maria Santos");

        mockMvc.perform(post("/api/denuncias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(denunciaDTOValida)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.identificacao").value(true))
                .andExpect(jsonPath("$.nomeAluno").value("Maria Santos"));
    }

    @Test
    @DisplayName("POST /api/denuncias - Deve retornar 400 quando denúncia identificada sem nome")
    void deveRetornar400QuandoDenunciaIdentificadaSemNome() throws Exception {
        denunciaDTOValida.setIdentificacao(true);
        denunciaDTOValida.setNomeAluno(null);

        mockMvc.perform(post("/api/denuncias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(denunciaDTOValida)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/denuncias/{protocolo} - Deve buscar denúncia por protocolo e retornar 200 OK")
    void deveBuscarDenunciaPorProtocoloERetornar200() throws Exception {
        // Criar denúncia primeiro
        Denuncia denuncia = new Denuncia();
        denuncia.setTipoDenuncia(TipoDeDenun.VANDALISMO);
        denuncia.setLocalOcorrencia("Pátio");
        denuncia.setDescricaoOcorrencia("Briga entre alunos");
        denuncia.setIdentificacao(false);
        denuncia.setDataOcorrencia(true);
        denuncia.setProtocolo("20251029-TEST1234");
        denuncia.setSituacao("Recebida");
        denunciaRepository.save(denuncia);

        // Buscar por protocolo
        mockMvc.perform(get("/api/denuncias/20251029-TEST1234"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.protocolo").value("20251029-TEST1234"))
                .andExpect(jsonPath("$.tipoDenuncia").value("VANDALISMO"))
                .andExpect(jsonPath("$.localOcorrencia").value("Pátio"));
    }

    @Test
    @DisplayName("GET /api/denuncias/{protocolo} - Deve retornar 404 quando protocolo não existe")
    void deveRetornar404QuandoProtocoloNaoExiste() throws Exception {
        mockMvc.perform(get("/api/denuncias/PROTOCOLO-INEXISTENTE"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/denuncias - Deve gerar protocolo único para cada denúncia")
    void deveGerarProtocoloUnicoParaCadaDenuncia() throws Exception {
        // Criar primeira denúncia
        String response1 = mockMvc.perform(post("/api/denuncias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(denunciaDTOValida)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        // Criar segunda denúncia
        String response2 = mockMvc.perform(post("/api/denuncias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(denunciaDTOValida)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        DenunciaDTO denuncia1 = objectMapper.readValue(response1, DenunciaDTO.class);
        DenunciaDTO denuncia2 = objectMapper.readValue(response2, DenunciaDTO.class);

        // Verificar que os protocolos são diferentes
        org.junit.jupiter.api.Assertions.assertNotEquals(denuncia1.getProtocolo(), denuncia2.getProtocolo());
    }

    @Test
    @DisplayName("POST /api/denuncias - Deve aceitar diferentes tipos de denúncia")
    void deveAceitarDiferentesTiposDeDenuncia() throws Exception {
        // Testar BULLYING
        denunciaDTOValida.setTipoDenuncia(TipoDeDenun.BULLYING);
        mockMvc.perform(post("/api/denuncias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(denunciaDTOValida)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipoDenuncia").value("BULLYING"));

        // Testar VANDALISMO
        denunciaDTOValida.setTipoDenuncia(TipoDeDenun.VANDALISMO);
        mockMvc.perform(post("/api/denuncias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(denunciaDTOValida)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipoDenuncia").value("VANDALISMO"));

        // Testar ASSEDIO
        denunciaDTOValida.setTipoDenuncia(TipoDeDenun.ASSEDIO);
        mockMvc.perform(post("/api/denuncias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(denunciaDTOValida)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipoDenuncia").value("ASSEDIO"));
    }

    @Test
    @DisplayName("POST /api/denuncias - Deve preservar confidencialidade em denúncias anônimas")
    void devePreservarConfidencialidadeEmDenunciasAnonimas() throws Exception {
        denunciaDTOValida.setIdentificacao(false);
        denunciaDTOValida.setNomeAluno(null);

        mockMvc.perform(post("/api/denuncias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(denunciaDTOValida)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.identificacao").value(false))
                .andExpect(jsonPath("$.nomeAluno").isEmpty());
    }

    @Test
    @DisplayName("GET /api/denuncias/{protocolo} - Deve retornar todos os campos da denúncia")
    void deveRetornarTodosOsCamposDaDenuncia() throws Exception {
        // Criar denúncia
        Denuncia denuncia = new Denuncia();
        denuncia.setTipoDenuncia(TipoDeDenun.DISCRIMINACAO);
        denuncia.setLocalOcorrencia("Biblioteca");
        denuncia.setDescricaoOcorrencia("Comentários discriminatórios");
        denuncia.setIdentificacao(true);
        denuncia.setNomeAluno("Pedro Oliveira");
        denuncia.setDataOcorrencia(true);
        denuncia.setProtocolo("20251029-FULL1234");
        denuncia.setSituacao("Recebida");
        denunciaRepository.save(denuncia);

        // Buscar e verificar todos os campos
        mockMvc.perform(get("/api/denuncias/20251029-FULL1234"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.protocolo").value("20251029-FULL1234"))
                .andExpect(jsonPath("$.tipoDenuncia").value("DISCRIMINACAO"))
                .andExpect(jsonPath("$.localOcorrencia").value("Biblioteca"))
                .andExpect(jsonPath("$.descricaoOcorrencia").value("Comentários discriminatórios"))
                .andExpect(jsonPath("$.identificacao").value(true))
                .andExpect(jsonPath("$.nomeAluno").value("Pedro Oliveira"))
                .andExpect(jsonPath("$.dataOcorrencia").value(true))
                .andExpect(jsonPath("$.situacao").value("Recebida"));
    }
}
