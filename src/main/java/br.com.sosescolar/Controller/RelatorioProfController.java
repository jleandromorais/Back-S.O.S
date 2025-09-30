package br.com.sosescolar.Controller;

import br.com.sosescolar.Service.RelatorioProfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import  br.com.sosescolar.Model.Denuncia;

import java.util.List;

@RestController
@RequestMapping("api/relatorio")
public class RelatorioProfController {

    @Autowired
    private RelatorioProfService relatorioProfService;

    @GetMapping("/prof")
    public List<Denuncia> getRelatorioProf() {
        return relatorioProfService.getRelatorio();
    }
}
