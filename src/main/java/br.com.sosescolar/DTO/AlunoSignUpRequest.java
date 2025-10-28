package br.com.sosescolar.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlunoSignUpRequest {
    private String nome;
    private String email;
    private String senha;
    // O campo "matricula" foi removido daqui
}