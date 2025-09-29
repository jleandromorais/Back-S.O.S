package S.O.S.Escola.CESAR.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AlunoDTO {

    private Long id;

    @NotBlank(message = "O nome completo é obrigatório.")
    private String nomeCompleto;

    @NotBlank(message = "A matrícula é obrigatória.")
    private String matricula;

    @Email(message = "O email deve ser válido.")
    @NotBlank(message = "O email é obrigatório.")
    private String email;

    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.")
    private String senha;
}
