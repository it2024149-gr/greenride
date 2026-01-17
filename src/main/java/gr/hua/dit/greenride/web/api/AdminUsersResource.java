package gr.hua.dit.greenride.web.api;



import gr.hua.dit.greenride.core.model.User;
import gr.hua.dit.greenride.core.repository.UserRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;

        import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/users")
public class AdminUsersResource {

    private final UserRepository userRepository;

    public AdminUsersResource(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public record UserSummary(
            Long id,
            String username,
            String email,
            String fullName,
            String role,
            boolean enabled
    ) {}

    @GetMapping
    public List<UserSummary> listUsers() {
        return userRepository.findAll().stream()
                .map(u -> new UserSummary(
                        u.getId(),
                        u.getUsername(),
                        u.getEmail(),
                        u.getFullName(),
                        u.getRole().name(),
                        u.isEnabled()
                ))
                .toList();
    }

    @PostMapping("/{id}/disable")
    public UserSummary disable(@PathVariable @NotNull Long id) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        u.setEnabled(false);
        userRepository.save(u);
        return new UserSummary(u.getId(), u.getUsername(), u.getEmail(), u.getFullName(), u.getRole().name(), u.isEnabled());
    }

    @PostMapping("/{id}/enable")
    public UserSummary enable(@PathVariable @NotNull Long id) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        u.setEnabled(true);
        userRepository.save(u);
        return new UserSummary(u.getId(), u.getUsername(), u.getEmail(), u.getFullName(), u.getRole().name(), u.isEnabled());
    }
}
