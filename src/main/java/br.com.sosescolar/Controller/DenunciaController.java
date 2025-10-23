package br.com.sosescolar.Controller;

import br.com.sosescolar.DTO.DenunciaDTO;
import br.com.sosescolar.Service.DenunciaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
<<<<<<< HEAD
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
=======
import org.springframework.web.bind.annotation.*;
>>>>>>> 022a2289939e2772ab87bcdfbac84ae27dcc0ee1

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
