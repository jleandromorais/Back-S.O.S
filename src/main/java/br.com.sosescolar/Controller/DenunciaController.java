package br.com.sosescolar.Controller;

import br.com.sosescolar.DTO.DenunciaDTO;
import br.com.sosescolar.Service.DenunciaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/denuncias")
public class DenunciaController {

    @Autowired
    private DenunciaService denunciaService;

    @PostMapping
    public ResponseEntity<DenunciaDTO> criarDenuncia(@Valid @RequestBody DenunciaDTO dto) {
        DenunciaDTO novaDenuncia = denunciaService.criarDenuncia(dto);
        return new ResponseEntity<>(novaDenuncia, HttpStatus.CREATED);
    }

    @GetMapping("/{protocolo}")
    public ResponseEntity<DenunciaDTO> buscarPorProtocolo(@PathVariable String protocolo) {
        try {
            DenunciaDTO denuncia = denunciaService.buscarPorProtocolo(protocolo);
            return ResponseEntity.ok(denuncia);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
    }
}
}
