package br.com.sosescolar.Service;

import br.com.sosescolar.Enum.TipoDeDenun;
import br.com.sosescolar.Repository.DenunciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import br.com.sosescolar.Model.Denuncia;
import jakarta.persistence.criteria.Predicate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RelatorioProfService {

    @Autowired
    private DenunciaRepository denunciaRepository;

    /**
     * Busca um relatório de denúncias, aplicando filtros dinâmicos.
     * @param tipo O tipo de denúncia (ex: BULLYING)
     * @param dataStr A data de criação (formato YYYY-MM-DD)
     * @param status A situação atual (ex: Recebida)
     */
    public List<Denuncia> getRelatorioFiltrado(TipoDeDenun tipo, String dataStr, String status) {

        // Cria uma "Especificação" (um construtor de query)
        Specification<Denuncia> spec = (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            // 1. Filtro por Tipo (tipoDenuncia)
            if (tipo != null) {
                predicates.add(criteriaBuilder.equal(root.get("tipoDenuncia"), tipo));
            }

            // 2. Filtro por Status (situacao)
            if (status != null && !status.isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("situacao"), status));
            }

            // 3. Filtro por Data (dataCriacao)
            // (Usa o novo campo 'dataCriacao' adicionado à Denuncia.java)
            if (dataStr != null && !dataStr.isBlank()) {
                try {
                    LocalDate localDate = LocalDate.parse(dataStr); // Ex: "2025-11-06"
                    // Cria um intervalo "entre 00:00:00 e 23:59:59" desse dia
                    predicates.add(criteriaBuilder.between(
                            root.get("dataCriacao"),
                            localDate.atStartOfDay(), // 2025-11-06T00:00:00
                            localDate.atTime(LocalTime.MAX) // 2025-11-06T23:59:59.99...
                    ));
                } catch (Exception e) {
                    // Ignora o filtro de data se o formato for inválido
                }
            }

            // Combina todos os filtros com "AND"
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        // Executa a busca no repositório com os filtros
        return denunciaRepository.findAll(spec);
    }

    // Método antigo
    public List<Denuncia> getRelatorio() {
        return denunciaRepository.findAll();
    }
}