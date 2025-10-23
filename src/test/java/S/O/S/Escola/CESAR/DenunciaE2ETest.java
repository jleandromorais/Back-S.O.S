package S.O.S.Escola.CESAR;

// Imports do seu projeto
import br.com.sosescolar.CesarApplication; // <-- Ótimo, este é o import correto!
import br.com.sosescolar.DTO.DenunciaDTO;
import br.com.sosescolar.Enum.TipoDeDenun;

// Imports do JUnit e Spring
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

// Import para corrigir o erro 403 FORBIDDEN
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.*;

<<<<<<< HEAD
@SpringBootTest(
    classes = br.com.sosescolar.CesarApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
=======
/**
 * CORREÇÃO 1: 'classes = CesarApplication.class'
 * (Isto já deve estar correto, baseado nos seus imports)
 */
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = CesarApplication.class
>>>>>>> 022a2289939e2772ab87bcdfbac84ae27dcc0ee1
)
@ActiveProfiles("test")
/**
 * CORREÇÃO 2: ADICIONAR ESTA ANOTAÇÃO
 * É isto que vai corrigir o erro '403 FORBIDDEN'.
 */
@WithMockUser
class DenunciaE2ETest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void deveGerarProtocoloEPermitirBuscaPorProtocolo() {
        DenunciaDTO novaDenuncia = new DenunciaDTO();
        novaDenuncia.setTipoDenuncia(TipoDeDenun.BULLYING);
        novaDenuncia.setLocalOcorrencia("Sala 201");
        novaDenuncia.setDescricaoOcorrencia("Aluno sendo intimidado durante o intervalo");
        novaDenuncia.setIdentificacao(false);
        novaDenuncia.setDataOcorrencia(true);

        ResponseEntity<DenunciaDTO> respostaCriacao = restTemplate.postForEntity(
                "/api/denuncias",
                novaDenuncia,
                DenunciaDTO.class
        );

        // A asserção agora deve passar (espera 201)
        assertEquals(HttpStatus.CREATED, respostaCriacao.getStatusCode());
        assertNotNull(respostaCriacao.getBody());

        DenunciaDTO denunciaCriada = respostaCriacao.getBody();
        assertNotNull(denunciaCriada.getProtocolo());
        assertEquals("Recebida", denunciaCriada.getSituacao());

        String protocolo = denunciaCriada.getProtocolo();
        System.out.println("Protocolo gerado: " + protocolo);

        ResponseEntity<DenunciaDTO> respostaBusca = restTemplate.getForEntity(
                "/api/denuncias/" + protocolo,
                DenunciaDTO.class
        );

        assertEquals(HttpStatus.OK, respostaBusca.getStatusCode());
        assertNotNull(respostaBusca.getBody());

        DenunciaDTO denunciaEncontrada = respostaBusca.getBody();
        assertEquals(protocolo, denunciaEncontrada.getProtocolo());
        assertEquals(TipoDeDenun.BULLYING, denunciaEncontrada.getTipoDenuncia());
    }

    @Test
    void deveRetornar404ParaProtocoloInexistente() {
        ResponseEntity<DenunciaDTO> resposta = restTemplate.getForEntity(
                "/api/denuncias/PROTOCOLO-FALSO-123",
                DenunciaDTO.class
        );

        // A asserção agora deve passar (espera 404)
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }
}