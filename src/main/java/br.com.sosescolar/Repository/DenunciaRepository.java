package br.com.sosescolar.Repository;

import br.com.sosescolar.Model.Denuncia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DenunciaRepository extends JpaRepository<Denuncia, Long> {

    // --- MÉTODOS DE CONSULTA PERSONALIZADOS (OPCIONAL) ---
    // O Spring Data JPA cria a consulta SQL automaticamente a partir do nome do método.

    /**
     * Encontra todas as denúncias com um status específico.
     * Exemplo de uso: denunciaRepository.findByStatus("Em Análise");
     * @param status O status a ser pesquisado.
     * @return Uma lista de denúncias que correspondem ao status.
     */
    List<Denuncia> findByStatus(String status);

    /**
     * Encontra todas as denúncias de um tipo específico.
     * Exemplo de uso: denunciaRepository.findByTipo(TipoDenuncia.BULLYING);
     * @param tipo O tipo da denúncia.
     * @return Uma lista de denúncias daquele tipo.
     */


}