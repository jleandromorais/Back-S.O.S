package br.com.sosescolar.Service;

import br.com.sosescolar.DTO.AlunoSignUpRequest; // <-- NOVO IMPORT
import br.com.sosescolar.DTO.ProfessorSignUpRequest;
import br.com.sosescolar.Model.User;
import br.com.sosescolar.Repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Construtor original (correto para esta abordagem)
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

    // <-- NOVO MÉTODO ADICIONADO -->
    public User registerAluno(AlunoSignUpRequest signUpRequest) {
        if (userRepository.findByEmail(signUpRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Erro: Email já está em uso!");
        }
        // Validação da matrícula (opcional, mas recomendado)
        // if (userRepository.findByMatricula(signUpRequest.getMatricula()).isPresent()) {
        //     throw new RuntimeException("Erro: Matrícula já está em uso!");
        // }

        User user = new User();
        user.setNome(signUpRequest.getNome());
        user.setEmail(signUpRequest.getEmail());
        user.setMatricula(signUpRequest.getMatricula()); // <-- Campo novo
        user.setSenha(passwordEncoder.encode(signUpRequest.getSenha()));
        user.setRole("ALUNO"); // <-- Role de Aluno
        return userRepository.save(user);
    }
}