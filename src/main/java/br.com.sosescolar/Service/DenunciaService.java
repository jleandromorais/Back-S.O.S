package br.com.sosescolar.Service;

import br.com.sosescolar.DTO.DenunciaDTO;
import br.com.sosescolar.Model.Denuncia;
import br.com.sosescolar.Repository.DenunciaRepository;
import jakarta.transaction.Transactional; // Import necessário
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class DenunciaService {

    @Autowired
    private DenunciaRepository denunciaRepository;

    @Transactional
    public DenunciaDTO criarDenuncia(DenunciaDTO dto) {
        Denuncia denuncia = new Denuncia();

        denuncia.setTipoDenuncia(dto.getTipoDenuncia());
        denuncia.setLocalOcorrencia(dto.getLocalOcorrencia());
        denuncia.setDescricaoOcorrencia(dto.getDescricaoOcorrencia());
        denuncia.setIdentificacao(dto.isIdentificacao());
        denuncia.setDataOcorrencia(dto.isDataOcorrencia());

        if (dto.isIdentificacao()) {
            if (dto.getNomeAluno() == null || dto.getNomeAluno().trim().isEmpty()) {
                throw new IllegalArgumentException("O nome do aluno é obrigatório para denúncias identificadas.");
            }
            denuncia.setNomeAluno(dto.getNomeAluno());
        }

        denuncia.setProtocolo(gerarProtocolo());
        // A situacao inicial "Recebida" será definida pelo @PrePersist no Model

        Denuncia denunciaSalva = denunciaRepository.save(denuncia);
        return toDTO(denunciaSalva);
    }

    /**
     * Gera um número de protocolo único.
     */
    private String gerarProtocolo() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String uuid = UUID.randomUUID().toString().toUpperCase().substring(0, 8);
        return timestamp + "-" + uuid;
    }

    /**
     * Busca uma denúncia pelo protocolo.
     */
    public DenunciaDTO buscarPorProtocolo(String protocolo) {
        Denuncia denuncia = denunciaRepository.findByProtocolo(protocolo)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Denúncia não encontrada com o protocolo: " + protocolo));
        return toDTO(denuncia);
    }

    /**
     * Atualiza a situação de uma denúncia com base no protocolo.
     */
    @Transactional
    public DenunciaDTO atualizarSituacao(String protocolo, String novoStatus) {
        // 1. Encontra a denúncia existente
        Denuncia denuncia = denunciaRepository.findByProtocolo(protocolo)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Denúncia não encontrada com o protocolo: " + protocolo));

        // 2. Atualiza o campo situacao
        denuncia.setSituacao(novoStatus);

        // 3. Salva a denúncia atualizada
        Denuncia denunciaAtualizada = denunciaRepository.save(denuncia);

        // 4. Retorna o DTO atualizado
        return toDTO(denunciaAtualizada);
    }

    /**
     * Converte a entidade Denuncia para DenunciaDTO.
     */
    private DenunciaDTO toDTO(Denuncia denuncia) {
        DenunciaDTO dto = new DenunciaDTO();
        dto.setId(denuncia.getId());
        dto.setTipoDenuncia(denuncia.getTipoDenuncia());
        dto.setLocalOcorrencia(denuncia.getLocalOcorrencia());
        dto.setDescricaoOcorrencia(denuncia.getDescricaoOcorrencia());
        dto.setIdentificacao(denuncia.isIdentificacao());
        dto.setNomeAluno(denuncia.getNomeAluno());
        dto.setDataOcorrencia(denuncia.isDataOcorrencia());
        dto.setSituacao(denuncia.getSituacao());
        dto.setProtocolo(denuncia.getProtocolo());
        return dto;
    }
}