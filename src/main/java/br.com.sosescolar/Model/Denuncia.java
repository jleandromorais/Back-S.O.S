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

    private LocalDateTime dataCriacao;

    private String status;

    // NOVO CAMPO
    @Column(nullable = false)
    private boolean anonima;

    @ManyToOne(fetch = FetchType.LAZY)
    // ALTERAÇÃO CRÍTICA: nullable agora é true para permitir denúncias anônimas (sem autor)
    @JoinColumn(name = "aluno_id", nullable = true)
    private Aluno autor;

    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
        status = "Recebida";
    }
}