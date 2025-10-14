package br.com.sosescolar.DTO;

import br.com.sosescolar.Enum.TipoDeDenun;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO (Data Transfer Object) para representar os dados de uma denúncia.
 * Usado tanto para receber dados na criação de uma denúncia (input)
 * quanto para enviar dados como resposta (output).
 */
@Data
public class DenunciaDTO {

    // --- CAMPOS DE SAÍDA (gerados pelo servidor) ---

    /**
     * ID único da denúncia.
     */
    private Long id;

    /**
     * Protocolo único gerado para acompanhamento.
     */
    private String protocolo;

    /**
     * Situação atual da denúncia (ex: "Recebida", "Em Análise").
     */
    private String situacao;


    // --- CAMPOS DE ENTRADA (enviados pelo cliente na criação) ---

    /**
     * O tipo de problema que está sendo reportado.
     */
    @NotNull(message = "O tipo da denúncia não pode ser nulo.")
    private TipoDeDenun tipoDenuncia;

    /**
     * Local onde o fato ocorreu.
     */
    @NotBlank(message = "O local da ocorrência é obrigatório.")
    @Size(min = 5, max = 200, message = "O local da ocorrência deve ter entre 5 e 200 caracteres.")
    private String localOcorrencia;

    /**
     * Descrição detalhada do que aconteceu.
     */
    @NotBlank(message = "A descrição da ocorrência é obrigatória.")
    @Size(min = 20, message = "A descrição deve ter no mínimo 20 caracteres.")
    private String descricaoOcorrencia;

    /**
     * Indica se o denunciante deseja se identificar.
     * Se true, o campo 'nomeAluno' deve ser preenchido.
     */
    @NotNull(message = "O campo 'identificacao' é obrigatório.")
    private boolean identificacao;

    /**
     * Nome do aluno que faz a denúncia.
     * Este campo só é obrigatório se 'identificacao' for true.
     * A validação dessa regra de negócio é feita na camada de serviço (Service).
     */
    private String nomeAluno;

    /**
     * Campo booleano do seu novo model.
     */
    @NotNull(message = "O campo 'dataOcorrencia' é obrigatório.")
    private boolean dataOcorrencia;

}