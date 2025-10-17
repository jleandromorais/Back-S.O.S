package br.com.sosescolar.Service;

import br.com.sosescolar.Repository.UserRepository;
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
}
