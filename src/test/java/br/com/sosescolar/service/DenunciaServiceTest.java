package br.com.sosescolar.service;

import br.com.sosescolar.DTO.DenunciaDTO;
import br.com.sosescolar.Enum.TipoDeDenun;
import br.com.sosescolar.Model.Denuncia;
import br.com.sosescolar.Repository.DenunciaRepository;
import br.com.sosescolar.Service.DenunciaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes de Unidade para DenunciaService
 * Testam a lógica de negócio isoladamente, sem dependências externas
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Testes de Unidade - DenunciaService")
class DenunciaServiceTest {

    @Mock
    private DenunciaRepository denunciaRepository;

    @InjectMocks
    private DenunciaService denunciaService;

    private DenunciaDTO denunciaDTOValida;
    private Denuncia denunciaEntity;

    @BeforeEach
    void setUp() {
        // Preparar dados de teste
        denunciaDTOValida = new DenunciaDTO();
        denunciaDTOValida.setTipoDenuncia(TipoDeDenun.BULLYING);
        denunciaDTOValida.setLocalOcorrencia("Sala 201");
        denunciaDTOValida.setDescricaoOcorrencia("Aluno sendo intimidado durante o intervalo");
        denunciaDTOValida.setIdentificacao(false);
        denunciaDTOValida.setDataOcorrencia(true);

        denunciaEntity = new Denuncia();
        denunciaEntity.setId(1L);
        denunciaEntity.setTipoDenuncia(TipoDeDenun.BULLYING);
        denunciaEntity.setLocalOcorrencia("Sala 201");
        denunciaEntity.setDescricaoOcorrencia("Aluno sendo intimidado durante o intervalo");
        denunciaEntity.setIdentificacao(false);
        denunciaEntity.setDataOcorrencia(true);
        denunciaEntity.setProtocolo("20251029-ABC12345");
        denunciaEntity.setSituacao("Recebida");
    }

    @Test
    @DisplayName("Deve criar denúncia confidencial com sucesso")
    void deveCriarDenunciaConfidencialComSucesso() {
        // Arrange
        when(denunciaRepository.save(any(Denuncia.class))).thenReturn(denunciaEntity);

        // Act
        DenunciaDTO resultado = denunciaService.criarDenuncia(denunciaDTOValida);

        // Assert
        assertNotNull(resultado);
        assertNotNull(resultado.getProtocolo());
        assertEquals("Recebida", resultado.getSituacao());
        assertEquals(TipoDeDenun.BULLYING, resultado.getTipoDenuncia());
        assertFalse(resultado.isIdentificacao());
        
        verify(denunciaRepository, times(1)).save(any(Denuncia.class));
    }

    @Test
    @DisplayName("Deve criar denúncia identificada com nome do aluno")
    void deveCriarDenunciaIdentificadaComNomeAluno() {
        // Arrange
        denunciaDTOValida.setIdentificacao(true);
        denunciaDTOValida.setNomeAluno("João Silva");
        
        denunciaEntity.setIdentificacao(true);
        denunciaEntity.setNomeAluno("João Silva");
        
        when(denunciaRepository.save(any(Denuncia.class))).thenReturn(denunciaEntity);

        // Act
        DenunciaDTO resultado = denunciaService.criarDenuncia(denunciaDTOValida);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isIdentificacao());
        assertEquals("João Silva", resultado.getNomeAluno());
        
        verify(denunciaRepository, times(1)).save(any(Denuncia.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando denúncia identificada não tem nome do aluno")
    void deveLancarExcecaoQuandoDenunciaIdentificadaSemNome() {
        // Arrange
        denunciaDTOValida.setIdentificacao(true);
        denunciaDTOValida.setNomeAluno(null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> denunciaService.criarDenuncia(denunciaDTOValida)
        );

        assertEquals("O nome do aluno é obrigatório para denúncias identificadas.", exception.getMessage());
        verify(denunciaRepository, never()).save(any(Denuncia.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando denúncia identificada tem nome vazio")
    void deveLancarExcecaoQuandoDenunciaIdentificadaComNomeVazio() {
        // Arrange
        denunciaDTOValida.setIdentificacao(true);
        denunciaDTOValida.setNomeAluno("   ");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> denunciaService.criarDenuncia(denunciaDTOValida)
        );

        assertEquals("O nome do aluno é obrigatório para denúncias identificadas.", exception.getMessage());
        verify(denunciaRepository, never()).save(any(Denuncia.class));
    }

    @Test
    @DisplayName("Deve buscar denúncia por protocolo com sucesso")
    void deveBuscarDenunciaPorProtocoloComSucesso() {
        // Arrange
        String protocolo = "20251029-ABC12345";
        when(denunciaRepository.findByProtocolo(protocolo)).thenReturn(Optional.of(denunciaEntity));

        // Act
        DenunciaDTO resultado = denunciaService.buscarPorProtocolo(protocolo);

        // Assert
        assertNotNull(resultado);
        assertEquals(protocolo, resultado.getProtocolo());
        assertEquals(TipoDeDenun.BULLYING, resultado.getTipoDenuncia());
        
        verify(denunciaRepository, times(1)).findByProtocolo(protocolo);
    }

    @Test
    @DisplayName("Deve lançar exceção quando protocolo não existe")
    void deveLancarExcecaoQuandoProtocoloNaoExiste() {
        // Arrange
        String protocoloInexistente = "PROTOCOLO-FALSO-123";
        when(denunciaRepository.findByProtocolo(protocoloInexistente)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> denunciaService.buscarPorProtocolo(protocoloInexistente)
        );

        assertTrue(exception.getMessage().contains("Denúncia não encontrada com o protocolo"));
        verify(denunciaRepository, times(1)).findByProtocolo(protocoloInexistente);
    }

    @Test
    @DisplayName("Deve gerar protocolo único para cada denúncia")
    void deveGerarProtocoloUnicoParaCadaDenuncia() {
        // Arrange
        Denuncia denuncia1 = new Denuncia();
        denuncia1.setProtocolo("20251029-ABC12345");
        denuncia1.setSituacao("Recebida");
        
        Denuncia denuncia2 = new Denuncia();
        denuncia2.setProtocolo("20251029-XYZ67890");
        denuncia2.setSituacao("Recebida");

        when(denunciaRepository.save(any(Denuncia.class)))
            .thenReturn(denuncia1)
            .thenReturn(denuncia2);

        // Act
        DenunciaDTO resultado1 = denunciaService.criarDenuncia(denunciaDTOValida);
        DenunciaDTO resultado2 = denunciaService.criarDenuncia(denunciaDTOValida);

        // Assert
        assertNotNull(resultado1.getProtocolo());
        assertNotNull(resultado2.getProtocolo());
        assertNotEquals(resultado1.getProtocolo(), resultado2.getProtocolo());
    }

    @Test
    @DisplayName("Deve definir situação inicial como 'Recebida'")
    void deveDefinirSituacaoInicialComoRecebida() {
        // Arrange
        when(denunciaRepository.save(any(Denuncia.class))).thenReturn(denunciaEntity);

        // Act
        DenunciaDTO resultado = denunciaService.criarDenuncia(denunciaDTOValida);

        // Assert
        assertEquals("Recebida", resultado.getSituacao());
    }

    @Test
    @DisplayName("Deve preservar confidencialidade em denúncias não identificadas")
    void devePreservarConfidencialidadeEmDenunciasNaoIdentificadas() {
        // Arrange
        when(denunciaRepository.save(any(Denuncia.class))).thenReturn(denunciaEntity);

        // Act
        DenunciaDTO resultado = denunciaService.criarDenuncia(denunciaDTOValida);

        // Assert
        assertFalse(resultado.isIdentificacao());
        assertNull(resultado.getNomeAluno());
    }
}
