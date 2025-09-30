package br.com.sosescolar.DTO;

import br.com.sosescolar.Enum.TipoDeDenun;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;
import br.com.sosescolar.DTO.AlunoDTO;
import java.time.LocalDateTime;
@Data
public class DenunciaDTO {
    // Campos que serão retornados na resposta
    private Long id;
    private String status;
    private LocalDateTime dataCriacao;
    private AlunoDTO autor;

    // Campos que vêm na requisição POST
    @NotNull
    private TipoDeDenun tipo;
    @NotBlank
    @Size(min = 5, max = 100)
    private String titulo;
    @NotBlank @Size(min = 20)
    private String descricao;
    @NotNull
    private Boolean anonima;
    private Long autorId;
}
