package gr.hua.dit.greenride;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import gr.hua.dit.greenride.core.model.User;
import gr.hua.dit.greenride.core.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;



@SpringBootApplication
public class GreenrideApplication {

    public static void main(String[] args) {
        SpringApplication.run(GreenrideApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(UserRepository userRepository) {
        return args -> {
            // Δημιουργούμε έναν test χρήστη
            User u = new User();
            u.setUsername("tester");
            u.setPasswordHash("1234");
            u.setFullName("Test User");
            u.setEmail("test@example.com");
            userRepository.save(u);

            System.out.println("✅ Saved user: " + u);

            // Διαβάζουμε όλους τους χρήστες
            System.out.println("📋 All users:");
            userRepository.findAll().forEach(System.out::println);
        };
    }
}
