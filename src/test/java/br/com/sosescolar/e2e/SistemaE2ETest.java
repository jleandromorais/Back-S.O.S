package br.com.sosescolar.e2e;

import br.com.sosescolar.CesarApplication;
import br.com.sosescolar.DTO.DenunciaDTO;
import br.com.sosescolar.Enum.TipoDeDenun;
import br.com.sosescolar.Repository.DenunciaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes End-to-End (E2E) do Sistema S.O.S Escola
 * Testam fluxos completos do sistema, simulando o uso real
 */
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = CesarApplication.class
)
@ActiveProfiles("test")
@WithMockUser
@Transactional
@DisplayName("Testes E2E - Sistema S.O.S Escola")
class SistemaE2ETest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private DenunciaRepository denunciaRepository;

    @BeforeEach
    void setUp() {
        denunciaRepository.deleteAll();
    }

    @Test
    @DisplayName("Fluxo completo: Aluno cria denúncia confidencial e recebe protocolo")
    void fluxoCompletoDenunciaConfidencial() {
        // CENÁRIO: Aluno quer fazer uma denúncia confidencial de bullying
        
        // PASSO 1: Aluno preenche formulário de denúncia
        DenunciaDTO novaDenuncia = new DenunciaDTO();
        novaDenuncia.setTipoDenuncia(TipoDeDenun.BULLYING);
        novaDenuncia.setLocalOcorrencia("Sala 201");
        novaDenuncia.setDescricaoOcorrencia("Aluno sendo intimidado durante o intervalo");
        novaDenuncia.setIdentificacao(false); // Denúncia confidencial
        novaDenuncia.setDataOcorrencia(true);

        // PASSO 2: Sistema recebe e processa a denúncia
        ResponseEntity<DenunciaDTO> respostaCriacao = restTemplate.postForEntity(
            "/api/denuncias",
            novaDenuncia,
            DenunciaDTO.class
        );

        // VERIFICAÇÃO: Denúncia foi criada com sucesso
        assertEquals(HttpStatus.CREATED, respostaCriacao.getStatusCode());
        assertNotNull(respostaCriacao.getBody());

        DenunciaDTO denunciaCriada = respostaCriacao.getBody();
        
        // PASSO 3: Aluno recebe protocolo único
        assertNotNull(denunciaCriada.getProtocolo(), "Protocolo deve ser gerado");
        assertTrue(denunciaCriada.getProtocolo().matches("\\d{8}-[A-Z0-9]{8}"), 
                   "Protocolo deve seguir o formato YYYYMMDD-XXXXXXXX");
        
        // VERIFICAÇÃO: Situação inicial é "Recebida"
        assertEquals("Recebida", denunciaCriada.getSituacao());
        
        // VERIFICAÇÃO: Confidencialidade é preservada
        assertFalse(denunciaCriada.isIdentificacao());
        assertNull(denunciaCriada.getNomeAluno());

        // PASSO 4: Aluno pode consultar sua denúncia usando o protocolo
        String protocolo = denunciaCriada.getProtocolo();
        ResponseEntity<DenunciaDTO> respostaBusca = restTemplate.getForEntity(
            "/api/denuncias/" + protocolo,
            DenunciaDTO.class
        );

        // VERIFICAÇÃO: Denúncia é encontrada e dados estão corretos
        assertEquals(HttpStatus.OK, respostaBusca.getStatusCode());
        assertNotNull(respostaBusca.getBody());
        
        DenunciaDTO denunciaEncontrada = respostaBusca.getBody();
        assertEquals(protocolo, denunciaEncontrada.getProtocolo());
        assertEquals(TipoDeDenun.BULLYING, denunciaEncontrada.getTipoDenuncia());
        assertEquals("Sala 201", denunciaEncontrada.getLocalOcorrencia());
    }

    @Test
    @DisplayName("Fluxo completo: Aluno cria denúncia identificada com seu nome")
    void fluxoCompletoDenunciaIdentificada() {
        // CENÁRIO: Aluno quer fazer uma denúncia identificada
        
        // PASSO 1: Aluno preenche formulário com identificação
        DenunciaDTO novaDenuncia = new DenunciaDTO();
        novaDenuncia.setTipoDenuncia(TipoDeDenun.ASSEDIO);
        novaDenuncia.setLocalOcorrencia("Pátio da escola");
        novaDenuncia.setDescricaoOcorrencia("Presenciei agressão física entre alunos");
        novaDenuncia.setIdentificacao(true); // Denúncia identificada
        novaDenuncia.setNomeAluno("Carlos Eduardo Silva");
        novaDenuncia.setDataOcorrencia(true);

        // PASSO 2: Sistema recebe e processa a denúncia
        ResponseEntity<DenunciaDTO> respostaCriacao = restTemplate.postForEntity(
            "/api/denuncias",
            novaDenuncia,
            DenunciaDTO.class
        );

        // VERIFICAÇÃO: Denúncia foi criada com sucesso
        assertEquals(HttpStatus.CREATED, respostaCriacao.getStatusCode());
        DenunciaDTO denunciaCriada = respostaCriacao.getBody();
        
        // VERIFICAÇÃO: Nome do aluno foi registrado
        assertTrue(denunciaCriada.isIdentificacao());
        assertEquals("Carlos Eduardo Silva", denunciaCriada.getNomeAluno());
        
        // PASSO 3: Verificar que a denúncia pode ser consultada
        String protocolo = denunciaCriada.getProtocolo();
        ResponseEntity<DenunciaDTO> respostaBusca = restTemplate.getForEntity(
            "/api/denuncias/" + protocolo,
            DenunciaDTO.class
        );

        assertEquals(HttpStatus.OK, respostaBusca.getStatusCode());
        assertEquals("Carlos Eduardo Silva", respostaBusca.getBody().getNomeAluno());
    }

    @Test
    @DisplayName("Fluxo de erro: Tentativa de criar denúncia identificada sem nome")
    void fluxoErroDenunciaIdentificadaSemNome() {
        // CENÁRIO: Aluno tenta criar denúncia identificada mas esquece de informar o nome
        
        DenunciaDTO denunciaInvalida = new DenunciaDTO();
        denunciaInvalida.setTipoDenuncia(TipoDeDenun.ASSEDIO);
        denunciaInvalida.setLocalOcorrencia("Corredor");
        denunciaInvalida.setDescricaoOcorrencia("Assédio moral por parte de colega");
        denunciaInvalida.setIdentificacao(true); // Quer se identificar
        denunciaInvalida.setNomeAluno(null); // Mas não informou o nome
        denunciaInvalida.setDataOcorrencia(true);

        // PASSO: Sistema tenta processar a denúncia (usando String para capturar mensagem de erro)
        ResponseEntity<String> resposta = restTemplate.postForEntity(
            "/api/denuncias",
            denunciaInvalida,
            String.class
        );

        // VERIFICAÇÃO: Sistema rejeita a denúncia
        assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
        assertNotNull(resposta.getBody());
        assertTrue(resposta.getBody().contains("obrigatório"), 
                   "Mensagem de erro deve indicar que o nome é obrigatório");
    }

    @Test
    @DisplayName("Fluxo de erro: Busca por protocolo inexistente")
    void fluxoErroBuscaProtocoloInexistente() {
        // CENÁRIO: Usuário tenta buscar denúncia com protocolo inválido
        
        String protocoloInexistente = "20251029-INVALIDO";
        
        // PASSO: Sistema tenta buscar a denúncia
        ResponseEntity<DenunciaDTO> resposta = restTemplate.getForEntity(
            "/api/denuncias/" + protocoloInexistente,
            DenunciaDTO.class
        );

        // VERIFICAÇÃO: Sistema retorna 404 Not Found
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }

    @Test
    @DisplayName("Fluxo completo: Múltiplas denúncias com protocolos únicos")
    void fluxoMultiplasDenunciasComProtocolosUnicos() {
        // CENÁRIO: Vários alunos fazem denúncias simultaneamente
        
        // PASSO 1: Criar primeira denúncia
        DenunciaDTO denuncia1 = criarDenunciaDTO(TipoDeDenun.BULLYING, "Sala 101");
        ResponseEntity<DenunciaDTO> resposta1 = restTemplate.postForEntity(
            "/api/denuncias", denuncia1, DenunciaDTO.class
        );
        
        // PASSO 2: Criar segunda denúncia
        DenunciaDTO denuncia2 = criarDenunciaDTO(TipoDeDenun.VANDALISMO, "Sala 202");
        ResponseEntity<DenunciaDTO> resposta2 = restTemplate.postForEntity(
            "/api/denuncias", denuncia2, DenunciaDTO.class
        );
        
        // PASSO 3: Criar terceira denúncia
        DenunciaDTO denuncia3 = criarDenunciaDTO(TipoDeDenun.DISCRIMINACAO, "Sala 303");
        ResponseEntity<DenunciaDTO> resposta3 = restTemplate.postForEntity(
            "/api/denuncias", denuncia3, DenunciaDTO.class
        );

        // VERIFICAÇÃO: Todas foram criadas com sucesso
        assertEquals(HttpStatus.CREATED, resposta1.getStatusCode());
        assertEquals(HttpStatus.CREATED, resposta2.getStatusCode());
        assertEquals(HttpStatus.CREATED, resposta3.getStatusCode());

        // VERIFICAÇÃO: Cada denúncia tem protocolo único
        String protocolo1 = resposta1.getBody().getProtocolo();
        String protocolo2 = resposta2.getBody().getProtocolo();
        String protocolo3 = resposta3.getBody().getProtocolo();

        assertNotEquals(protocolo1, protocolo2);
        assertNotEquals(protocolo2, protocolo3);
        assertNotEquals(protocolo1, protocolo3);

        // PASSO 4: Verificar que todas podem ser consultadas individualmente
        ResponseEntity<DenunciaDTO> busca1 = restTemplate.getForEntity(
            "/api/denuncias/" + protocolo1, DenunciaDTO.class
        );
        ResponseEntity<DenunciaDTO> busca2 = restTemplate.getForEntity(
            "/api/denuncias/" + protocolo2, DenunciaDTO.class
        );
        ResponseEntity<DenunciaDTO> busca3 = restTemplate.getForEntity(
            "/api/denuncias/" + protocolo3, DenunciaDTO.class
        );

        assertEquals(HttpStatus.OK, busca1.getStatusCode());
        assertEquals(HttpStatus.OK, busca2.getStatusCode());
        assertEquals(HttpStatus.OK, busca3.getStatusCode());

        assertEquals("Sala 101", busca1.getBody().getLocalOcorrencia());
        assertEquals("Sala 202", busca2.getBody().getLocalOcorrencia());
        assertEquals("Sala 303", busca3.getBody().getLocalOcorrencia());
    }

    @Test
    @DisplayName("Fluxo completo: Diferentes tipos de denúncia são aceitos")
    void fluxoDiferentesTiposDenuncia() {
        // CENÁRIO: Sistema deve aceitar todos os tipos de denúncia definidos
        
        TipoDeDenun[] tiposDenuncia = TipoDeDenun.values();
        
        for (TipoDeDenun tipo : tiposDenuncia) {
            DenunciaDTO denuncia = criarDenunciaDTO(tipo, "Local teste");
            
            ResponseEntity<DenunciaDTO> resposta = restTemplate.postForEntity(
                "/api/denuncias", denuncia, DenunciaDTO.class
            );
            
            // VERIFICAÇÃO: Cada tipo é aceito
            assertEquals(HttpStatus.CREATED, resposta.getStatusCode(), 
                        "Tipo " + tipo + " deve ser aceito");
            assertEquals(tipo, resposta.getBody().getTipoDenuncia());
        }
    }

    @Test
    @DisplayName("Fluxo de verificação: Protocolo mantém formato consistente")
    void fluxoVerificacaoFormatoProtocolo() {
        // CENÁRIO: Verificar que o formato do protocolo é consistente
        
        DenunciaDTO denuncia = criarDenunciaDTO(TipoDeDenun.BULLYING, "Sala 101");
        ResponseEntity<DenunciaDTO> resposta = restTemplate.postForEntity(
            "/api/denuncias", denuncia, DenunciaDTO.class
        );

        String protocolo = resposta.getBody().getProtocolo();
        
        // VERIFICAÇÃO: Formato YYYYMMDD-XXXXXXXX
        assertNotNull(protocolo);
        assertTrue(protocolo.matches("\\d{8}-[A-Z0-9]{8}"),
                  "Protocolo deve ter formato YYYYMMDD-XXXXXXXX");
        
        // VERIFICAÇÃO: Data no protocolo é válida (ano atual)
        String dataParte = protocolo.substring(0, 4);
        int ano = Integer.parseInt(dataParte);
        assertTrue(ano >= 2024 && ano <= 2030, "Ano no protocolo deve ser válido");
    }

    // Método auxiliar para criar DTOs de teste
    private DenunciaDTO criarDenunciaDTO(TipoDeDenun tipo, String local) {
        DenunciaDTO dto = new DenunciaDTO();
        dto.setTipoDenuncia(tipo);
        dto.setLocalOcorrencia(local);
        dto.setDescricaoOcorrencia("Descrição de teste para " + tipo);
        dto.setIdentificacao(false);
        dto.setDataOcorrencia(true);
        return dto;
    }
}
