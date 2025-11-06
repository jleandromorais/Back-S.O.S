package br.com.sosescolar.Repository;


import br.com.sosescolar.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    // Adicione outros métodos de busca se necessário
}
