package br.com.sosescolar.Controller;

// --- Imports Adicionados ---
import br.com.sosescolar.DTO.AtualizacaoStatusDTO;
import br.com.sosescolar.DTO.DenunciaDTO;
import br.com.sosescolar.Enum.TipoDeDenun; // NOVO
import br.com.sosescolar.Service.DenunciaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*; // Alterado para incluir @RequestParam
// --- Fim dos Imports Adicionados ---

import br.com.sosescolar.Service.RelatorioProfService;
import org.springframework.beans.factory.annotation.Autowired;
// Imports originais removidos e substituídos por @*
import br.com.sosescolar.Model.Denuncia;

import java.util.List;

@RestController
@RequestMapping("api/relatorio")
public class RelatorioProfController {

    @Autowired
    private RelatorioProfService relatorioProfService;

    @Autowired
    private DenunciaService denunciaService;

    // --- ENDPOINT MODIFICADO ---
    /**
     * Endpoint seguro para a Equipe Pedagógica (Professor)
     * obter um relatório de denúncias, com filtros opcionais.
     * @param tipo (Opcional) Filtra por tipo de denúncia (BULLYING, RACISMO, etc)
     * @param data (Opcional) Filtra por data de criação (formato YYYY-MM-DD)
     * @param status (Opcional) Filtra por situação (Recebida, Em Andamento, etc)
     */
    @GetMapping("/prof")
    @PreAuthorize("hasRole('PROFESSOR')") //
    public List<Denuncia> getRelatorioProf(
            @RequestParam(required = false) TipoDeDenun tipo,
            @RequestParam(required = false) String data,
            @RequestParam(required = false) String status
    ) {
        // Chama o novo serviço de filtragem
        return relatorioProfService.getRelatorioFiltrado(tipo, data, status);
    }
    // --- FIM DA MODIFICAÇÃO ---

    /**
     * Endpoint seguro para a Equipe Pedagógica (Professor)
     * ver os detalhes de uma denúncia específica pelo protocolo.
     */
    @GetMapping("/denuncia/{protocolo}")
    @PreAuthorize("hasRole('PROFESSOR')") //
    public ResponseEntity<DenunciaDTO> getDenunciaDetalhada(@PathVariable String protocolo) {
        try {
            DenunciaDTO denuncia = denunciaService.buscarPorProtocolo(protocolo);
            return ResponseEntity.ok(denuncia);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint seguro para a Equipe Pedagógica (Professor)
     * atualizar o status de uma denúncia (ex: "Em Andamento", "Finalizada").
     */
    @PatchMapping("/denuncia/{protocolo}/status")
    @PreAuthorize("hasRole('PROFESSOR')") //
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