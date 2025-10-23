package br.com.sosescolar.CESAR;

import br.com.sosescolar.DTO.DenunciaDTO;
import br.com.sosescolar.Enum.TipoDeDenun;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
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

        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }
}