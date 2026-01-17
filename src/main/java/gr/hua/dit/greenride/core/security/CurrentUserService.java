package gr.hua.dit.greenride.core.security;



import gr.hua.dit.greenride.core.model.User;
import gr.hua.dit.greenride.core.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    private final UserRepository userRepository;

    public CurrentUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User requireUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            throw new IllegalStateException("No authenticated user");
        }
        return userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new IllegalStateException("User not found: " + auth.getName()));
    }

    public boolean isAdmin(User u) {
        return u.getRole() != null && u.getRole().name().equalsIgnoreCase("ADMIN");
    }
}
