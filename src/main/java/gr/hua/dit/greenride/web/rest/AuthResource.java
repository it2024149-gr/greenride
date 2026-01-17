package gr.hua.dit.greenride.web.rest;



import gr.hua.dit.greenride.core.model.User;
import gr.hua.dit.greenride.core.model.UserRole;
import gr.hua.dit.greenride.core.repository.UserRepository;
import gr.hua.dit.greenride.core.security.JwtService;
import gr.hua.dit.greenride.web.rest.model.*;

        import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.*;
        import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
        import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthResource {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthResource(AuthenticationManager authenticationManager,
                        JwtService jwtService,
                        UserRepository userRepository,
                        PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public TokenResponse login(@RequestBody @Valid LoginRequest req) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.username(), req.password())
            );

            // subject = username (απλό και καθαρό)
            String token = jwtService.issue(req.username(), List.of(roleFromDb(req.username())));
            return new TokenResponse(token, "Bearer", 120L * 60L);

        } catch (BadCredentialsException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody @Valid RegisterRequest req) {
        if (userRepository.findByUsername(req.username()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }

        User u = new User();
        u.setUsername(req.username());
        u.setPasswordHash(passwordEncoder.encode(req.password()));
        u.setFullName(req.fullName());
        u.setEmail(req.email());
        u.setRole(UserRole.USER);
        u.setEnabled(true);

        userRepository.save(u);
    }

    private String roleFromDb(String username) {
        return userRepository.findByUsername(username)
                .map(u -> u.getRole().name())
                .orElse(UserRole.USER.name());
    }
}
