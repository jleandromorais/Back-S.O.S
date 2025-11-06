package br.com.sosescolar.Controller;

// --- Imports Adicionados ---
import br.com.sosescolar.DTO.AtualizacaoStatusDTO;
import br.com.sosescolar.DTO.DenunciaDTO;
import br.com.sosescolar.Service.DenunciaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
// --- Fim dos Imports Adicionados ---

import br.com.sosescolar.Service.RelatorioProfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.sosescolar.Model.Denuncia;

import java.util.List;

@RestController
@RequestMapping("api/relatorio")
public class RelatorioProfController {

    @Autowired
    private RelatorioProfService relatorioProfService;

    // --- Injeção Adicionada ---
    @Autowired
    private DenunciaService denunciaService;

    @GetMapping("/prof")
    @PreAuthorize("hasRole('PROFESSOR')") // Protege o endpoint de lista
    public List<Denuncia> getRelatorioProf() {
        return relatorioProfService.getRelatorio(); //
    }

    // --- NOVO ENDPOINT DE DETALHES ---
    /**
     * Endpoint seguro para a Equipe Pedagógica (Professor)
     * ver os detalhes de uma denúncia específica pelo protocolo.
     */
    @GetMapping("/denuncia/{protocolo}")
    @PreAuthorize("hasRole('PROFESSOR')") // Apenas PROFESSOR pode aceder
    public ResponseEntity<DenunciaDTO> getDenunciaDetalhada(@PathVariable String protocolo) {
        try {
            DenunciaDTO denuncia = denunciaService.buscarPorProtocolo(protocolo);
            return ResponseEntity.ok(denuncia);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // --- NOVO ENDPOINT DE ATUALIZAÇÃO ---
    /**
     * Endpoint seguro para a Equipe Pedagógica (Professor)
     * atualizar o status de uma denúncia (ex: "Em Andamento", "Finalizada").
     */
    @PatchMapping("/denuncia/{protocolo}/status")
    @PreAuthorize("hasRole('PROFESSOR')") // Apenas PROFESSOR pode atualizar
    public ResponseEntity<DenunciaDTO> atualizarStatusDenuncia(
            @PathVariable String protocolo,
            @Valid @RequestBody AtualizacaoStatusDTO dto) {

        try {
            DenunciaDTO denunciaAtualizada = denunciaService.atualizarSituacao(protocolo, dto.getNovoStatus());
            return ResponseEntity.ok(denunciaAtualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}