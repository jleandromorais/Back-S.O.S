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
            autorDto.setMatricula(denuncia.getAutor().getMatricula());
            autorDto.setEmail(denuncia.getAutor().getEmail());
            autorDto.setSenha(null);
            dto.setAutor(autorDto);
        }

        return dto;
    }
}