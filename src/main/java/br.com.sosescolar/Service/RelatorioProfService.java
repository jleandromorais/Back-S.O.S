package br.com.sosescolar.Service;
import br.com.sosescolar.Repository.DenunciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import  br.com.sosescolar.Model.Denuncia;

import java.util.List;

@Service
public class RelatorioProfService {

    @Autowired
    private DenunciaRepository denunciaRepository;

    public List<Denuncia> getRelatorio() {
        return denunciaRepository.findAll();
    }
}
