package br.com.sosescolar.Controller;

import br.com.sosescolar.DTO.DenunciaDTO; // NOVO IMPORT
import br.com.sosescolar.Service.DenunciaService; // NOVO IMPORT
import br.com.sosescolar.Service.RelatorioProfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity; // NOVO IMPORT
import org.springframework.security.access.prepost.PreAuthorize; // NOVO IMPORT (Ver Passo 2)
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable; // NOVO IMPORT
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.sosescolar.Model.Denuncia;

import java.util.List;

@RestController
@RequestMapping("api/relatorio")
public class RelatorioProfController {

    @Autowired
    private RelatorioProfService relatorioProfService;

    // --- NOVO ---
    // Injete o DenunciaService para reutilizar a lógica de busca
    @Autowired
    private DenunciaService denunciaService;
    // --- FIM DO NOVO ---

    @GetMapping("/prof")
    @PreAuthorize("hasRole('PROFESSOR')") // Protege o endpoint de lista
    public List<Denuncia> getRelatorioProf() {
        // NOTA: O ideal seria este método também retornar List<DenunciaDTO>
        // em vez da entidade completa, mas por agora focamos na nova funcionalidade.
        return relatorioProfService.getRelatorio();
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
            // Reutiliza a mesma lógica do DenunciaService
            DenunciaDTO denuncia = denunciaService.buscarPorProtocolo(protocolo);
            return ResponseEntity.ok(denuncia);
        } catch (IllegalArgumentException e) {
            // Se o protocolo não for encontrado
            return ResponseEntity.notFound().build();
        }
    }
    // --- FIM DO NOVO ENDPOINT ---
}