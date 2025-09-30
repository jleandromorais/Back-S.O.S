package S.O.S.Escola.CESAR.Controller;// Em S.O.S.Escola.CESAR.Controller.DenunciaController.java

import S.O.S.Escola.CESAR.DTO.DenunciaDTO;
import S.O.S.Escola.CESAR.Service.DenunciaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*; // Importe o PathVariable

import java.util.List; // Importe o List

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
}