package gr.hua.dit.greenride;

import gr.hua.dit.greenride.core.model.User;
import gr.hua.dit.greenride.core.model.UserRole;
import gr.hua.dit.greenride.core.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class GreenrideApplication {

    public static void main(String[] args) {
        SpringApplication.run(GreenrideApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(UserRepository userRepository,
                                  PasswordEncoder passwordEncoder) {
        return args -> {

            if (!userRepository.existsByUsername("tester")) {
                User u = new User();
                u.setUsername("tester");
                u.setPasswordHash(passwordEncoder.encode("1234")); // ✅ ΣΩΣΤΟ
                u.setFullName("Test User");
                u.setEmail("test@example.com");
                u.setRole(UserRole.ADMIN);
                u.setEnabled(true);

                userRepository.save(u);
                System.out.println(" Saved user 'tester' with BCrypt password");
            } else {
                System.out.println("ℹ User 'tester' already exists, skipping insert.");
            }
        };
    }
}
