package br.com.sosescolar.Model;

import br.com.sosescolar.Model.Aluno;
import br.com.sosescolar.Enum.TipoDeDenun;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Data
@Getter
@Setter
public class Denuncia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // NOVO CAMPO
    @Enumerated(EnumType.STRING) // Salva o nome do enum (ex: "BULLYING") no banco, o que é mais legível
    @Column(nullable = false)
    private TipoDeDenun tipoDenuncia;

    @Column (nullable = false)
    private boolean identificacao;

    @Column (nullable = true)
    private String nomeAluno;

    @Column(nullable = false)
    private String localOcorrencia;

    @Column(columnDefinition = "TEXT", nullable = false)
    private  String descricaoOcorrencia;

    // NOVO CAMPO
    @Column(nullable = false)
    private boolean dataOcorrencia;




    @ManyToOne
    @JoinColumn(name = "aluno_id") // ou o nome da sua coluna
    private Aluno autor; // <-- O nome deste campo deve ser "autor"

    // Um protocolo pode ser gerado após a criação, então pode começar como nulo
    @Column(unique = true, nullable = true)
    private String protocolo;

    //cuidado ao codar

    @Column(nullable = false)
    private String situacao;


    @PrePersist
    protected void onCreate() {
        // Define um status inicial padrão ao criar uma nova denúncia
        if (this.situacao == null) {
            this.situacao = "Recebida";
        }
    }
}