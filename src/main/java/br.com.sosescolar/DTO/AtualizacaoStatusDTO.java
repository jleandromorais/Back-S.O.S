package br.com.sosescolar.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AtualizacaoStatusDTO {

    @NotBlank(message = "O novo status não pode estar em branco")
    private String novoStatus;
}
