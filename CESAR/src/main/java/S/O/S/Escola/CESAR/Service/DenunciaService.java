// Em S.O.S.Escola.CESAR.Service.DenunciaService.java

package S.O.S.Escola.CESAR.Service;

import S.O.S.Escola.CESAR.DTO.AlunoDTO;
import S.O.S.Escola.CESAR.DTO.DenunciaDTO;
import S.O.S.Escola.CESAR.Model.Aluno;
import S.O.S.Escola.CESAR.Model.Denuncia;
import S.O.S.Escola.CESAR.Repository.AlunoRepository;
import S.O.S.Escola.CESAR.Repository.DenunciaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List; // Importar List
import java.util.stream.Collectors; // Importar Collectors

@Service
public class DenunciaService {

    @Autowired
    private DenunciaRepository denunciaRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Transactional
    public DenunciaDTO criarDenuncia(DenunciaDTO dto) {
        Denuncia denuncia = new Denuncia();
        denuncia.setTipo(dto.getTipo());
        denuncia.setTitulo(dto.getTitulo());
        denuncia.setDescricao(dto.getDescricao());
        denuncia.setAnonima(dto.getAnonima());

        if (!dto.getAnonima()) {
            if (dto.getAutorId() == null) {
                throw new IllegalArgumentException("ID do autor é obrigatório.");
            }
            Aluno autor = alunoRepository.findById(dto.getAutorId())
                    .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado!"));
            denuncia.setAutor(autor);
        }

        Denuncia denunciaSalva = denunciaRepository.save(denuncia);
        return toDTO(denunciaSalva);
    }

    // --- NOVOS MÉTODOS PARA GET ---

    /**
     * Busca todas as denúncias salvas no banco de dados.
     * @return Uma lista de DenunciaDTO.
     */
    public List<DenunciaDTO> listarTodas() {
        // 1. Busca a lista de ENTIDADES Denuncia do banco
        List<Denuncia> denuncias = denunciaRepository.findAll();

        // 2. Transforma cada ENTIDADE da lista em um DTO e retorna
        return denuncias.stream()
                .map(this::toDTO) // Usa o método toDTO que você já criou!
                .collect(Collectors.toList());
    }

    /**
     * Busca uma única denúncia pelo seu ID.
     * @param id O ID da denúncia a ser buscada.
     * @return O DenunciaDTO correspondente.
     * @throws EntityNotFoundException se a denúncia não for encontrada.
     */
    public DenunciaDTO buscarPorId(Long id) {
        // 1. Busca a ENTIDADE Denuncia pelo ID. Se não encontrar, lança uma exceção.
        Denuncia denuncia = denunciaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Denúncia não encontrada com o ID: " + id));

        // 2. Converte a entidade encontrada para DTO e retorna
        return toDTO(denuncia);
    }

    // --- SEU MÉTODO HELPER toDTO ---

    private DenunciaDTO toDTO(Denuncia denuncia) {
        DenunciaDTO dto = new DenunciaDTO();
        dto.setId(denuncia.getId());
        dto.setTipo(denuncia.getTipo());
        dto.setTitulo(denuncia.getTitulo());
        dto.setDescricao(denuncia.getDescricao());
        dto.setAnonima(denuncia.isAnonima());
        dto.setStatus(denuncia.getStatus());
        dto.setDataCriacao(denuncia.getDataCriacao());
        dto.setAutorId(null);

        if (denuncia.getAutor() != null) {
            AlunoDTO autorDto = new AlunoDTO();
            autorDto.setId(denuncia.getAutor().getId());
            autorDto.setNomeCompleto(denuncia.getAutor().getNomeCompleto());
            autorDto.setEmail(denuncia.getAutor().getEmail());
            autorDto.setSenha(null);
            dto.setAutor(autorDto);
            dto.setAutorId(denuncia.getAutor().getId());
        }

        return dto;
    }
}