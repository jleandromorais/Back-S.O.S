package br.com.sosescolar.service;

import br.com.sosescolar.DTO.DenunciaDTO;
import br.com.sosescolar.Enum.TipoDeDenun;
import br.com.sosescolar.Model.Denuncia;
import br.com.sosescolar.Repository.DenunciaRepository;
import br.com.sosescolar.Service.DenunciaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class DenunciaServiceTest {

    @Mock
    private DenunciaRepository denunciaRepository;

    @InjectMocks
    private DenunciaService denunciaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Teste 1: Verificar se a criação de uma denúncia gera um protocolo
    @Test
    void deveGerarProtocoloAoCriarDenuncia() {
        // Arrange
        DenunciaDTO inputDto = new DenunciaDTO();
        inputDto.setTipoDenuncia(TipoDeDenun.BULLYING);
        inputDto.setDescricaoOcorrencia("Teste de protocolo");
        inputDto.setIdentificacao(false);
        inputDto.setDataOcorrencia(true); // Adicionado para evitar erro de validação

        Denuncia mockDenuncia = new Denuncia();
        mockDenuncia.setProtocolo("PROTOCOLO-TESTE-123");

        when(denunciaRepository.save(any(Denuncia.class))).thenReturn(mockDenuncia);

        // Act
        DenunciaDTO resultDto = denunciaService.criarDenuncia(inputDto);

        // Assert
        assertNotNull(resultDto.getProtocolo());
        assertEquals("PROTOCOLO-TESTE-123", resultDto.getProtocolo());
    }
}
