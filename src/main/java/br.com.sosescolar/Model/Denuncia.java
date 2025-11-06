package br.com.sosescolar.Model;

import br.com.sosescolar.Model.Aluno;
import br.com.sosescolar.Enum.TipoDeDenun;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

// --- NOVO IMPORT ---
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Data
@Getter
@Setter
public class Denuncia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
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

    @Column(nullable = false)
    private boolean dataOcorrencia;

    @ManyToOne
    @JoinColumn(name = "aluno_id")
    private Aluno autor;

    @Column(unique = true, nullable = true)
    private String protocolo;

    @Column(nullable = false)
    private String situacao;

    // --- NOVO CAMPO PARA FILTRO DE DATA ---
    @CreationTimestamp // Define automaticamente a data e hora no momento da criação
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao;
    // --- FIM DO NOVO CAMPO ---

    @PrePersist
    protected void onCreate() {
        // Define um status inicial padrão ao criar uma nova denúncia
        if (this.situacao == null) {
            this.situacao = "Recebida";
        }
        // A dataCriacao agora é tratada automaticamente pelo @CreationTimestamp
    }
}