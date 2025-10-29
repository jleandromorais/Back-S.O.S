package br.com.sosescolar.Service;

import br.com.sosescolar.DTO.AlunoSignUpRequest;
import br.com.sosescolar.DTO.ProfessorSignUpRequest;
import br.com.sosescolar.Model.User;
import br.com.sosescolar.Repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerProfessor(ProfessorSignUpRequest signUpRequest) {
        if (userRepository.findByEmail(signUpRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Erro: Email já está em uso!");
        }
        User user = new User();
        user.setNome(signUpRequest.getNome());
        user.setEmail(signUpRequest.getEmail());
        user.setSenha(passwordEncoder.encode(signUpRequest.getSenha()));
        user.setRole("PROFESSOR");
        return userRepository.save(user);
    }


    public User registerAluno(AlunoSignUpRequest signUpRequest) {
        if (userRepository.findByEmail(signUpRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Erro: Email já está em uso!");
        }

        User user = new User();
        user.setNome(signUpRequest.getNome());
        user.setEmail(signUpRequest.getEmail());
        // <-- A LINHA user.setMatricula(...) FOI REMOVIDA DAQUI -->
        user.setSenha(passwordEncoder.encode(signUpRequest.getSenha()));
        user.setRole("ALUNO");
        return userRepository.save(user);
    }
}