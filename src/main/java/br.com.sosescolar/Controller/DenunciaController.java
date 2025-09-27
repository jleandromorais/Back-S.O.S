package S.O.S.Escola.CESAR.Controller;

import S.O.S.Escola.CESAR.DTO.DenunciaDTO;
import S.O.S.Escola.CESAR.Service.DenunciaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
