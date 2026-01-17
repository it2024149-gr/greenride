package gr.hua.dit.greenride.web.api;



import gr.hua.dit.greenride.core.repository.RideRepository;
import gr.hua.dit.greenride.core.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminStatsResource {

    private final RideRepository rideRepository;
    private final UserRepository userRepository;

    public AdminStatsResource(RideRepository rideRepository, UserRepository userRepository) {
        this.rideRepository = rideRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/stats")
    public Map<String, Object> stats() {

        double avgOcc = rideRepository.averageOccupancyRatioPlanned();

        return Map.of(
                "usersCount", userRepository.count(),
                "ridesCount", rideRepository.count(),
                "averageOccupancyRatio", avgOcc
        );
    }
}
