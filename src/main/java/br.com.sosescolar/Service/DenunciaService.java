package br.com.sosescolar.Service;

import br.com.sosescolar.DTO.DenunciaDTO;
import br.com.sosescolar.Model.Denuncia;
import br.com.sosescolar.Repository.DenunciaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class DenunciaService {

    @Autowired
    private DenunciaRepository denunciaRepository;

    // O AlunoRepository não é mais necessário, pois não há mais uma entidade Aluno associada.

    @Transactional
    public DenunciaDTO criarDenuncia(DenunciaDTO dto) {
        Denuncia denuncia = new Denunciaa();

        // Mapeia os campos do DTO para o novo Model
        denuncia.setTipoDenuncia(dto.getTipoDenuncia());
        denuncia.setLocalOcorrencia(dto.getLocalOcorrencia());
        denuncia.setDescricaoOcorrencia(dto.getDescricaoOcorrencia());
        denuncia.setIdentificacao(dto.isIdentificacao());
        denuncia.setDataOcorrencia(dto.isDataOcorrencia()); // Mapeando o novo campo booleano

        // Lógica para denúncias identificadas
        if (dto.isIdentificacao()) {
            if (dto.getNomeAluno() == null || dto.getNomeAluno().trim().isEmpty()) {
                throw new IllegalArgumentException("O nome do aluno é obrigatório para denúncias identificadas.");
            }
            denuncia.setNomeAluno(dto.getNomeAluno());
        }

        // Gera um protocolo único antes de salvar
        denuncia.setProtocolo(gerarProtocolo());

        // A situacao inicial "Recebida" será definida pelo @PrePersist no Model

        Denuncia denunciaSalva = denunciaRepository.save(denuncia);
        return toDTO(denunciaSalva);
    }

    /**
     * Gera um número de protocolo único.
     * Exemplo: 20251014-ABC123DE
     */
    private String gerarProtocolo() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String uuid = UUID.randomUUID().toString().toUpperCase().substring(0, 8);
        return timestamp + "-" + uuid;
    }

    /**
     * Converte a entidade Denuncia para DenunciaDTO.
     */
    private DenunciaDTO toDTO(Denuncia denuncia) {
        // Assume-se que o DenunciaDTO foi atualizado para corresponder ao novo Model
        DenunciaDTO dto = new DenunciaDTO();
        dto.setId(denuncia.getId());
        dto.setTipoDenuncia(denuncia.getTipoDenuncia());
        dto.setLocalOcorrencia(denuncia.getLocalOcorrencia());
        dto.setDescricaoOcorrencia(denuncia.getDescricaoOcorrencia());
        dto.setIdentificacao(denuncia.isIdentificacao());
        dto.setNomeAluno(denuncia.getNomeAluno()); // Será null se não for identificada
        dto.setDataOcorrencia(denuncia.isDataOcorrencia());
        dto.setSituacao(denuncia.getSituacao());
        dto.setProtocolo(denuncia.getProtocolo());

        // O campo 'autor' (do tipo AlunoDTO) não existe mais no DTO,
        // foi substituído por 'nomeAluno' (String).

        return dto;
    }
}