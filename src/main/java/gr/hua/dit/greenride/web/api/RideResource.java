package gr.hua.dit.greenride.web.api;



import gr.hua.dit.greenride.core.model.Ride;
import gr.hua.dit.greenride.core.security.CurrentUserService;
import gr.hua.dit.greenride.core.service.RideService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/rides")
public class RideResource {

    private final RideService rideService;
    private final CurrentUserService currentUser;

    public RideResource(RideService rideService, CurrentUserService currentUser) {
        this.rideService = rideService;
        this.currentUser = currentUser;
    }

    public record CreateRideRequest(
            @NotBlank String origin,
            @NotBlank String destination,
            @NotNull LocalDateTime departureTime,
            @Min(1) int totalSeats
    ) {}

    @PostMapping
    public Ride create(@Valid @RequestBody CreateRideRequest req) {
        return rideService.createRide(
                currentUser.requireUser(),
                req.origin(),
                req.destination(),
                req.departureTime(),
                req.totalSeats()
        );
    }

    @GetMapping("/search")
    public List<Ride> search(@RequestParam String origin,
                             @RequestParam String destination,
                             @RequestParam LocalDateTime afterTime) {
        return rideService.searchRides(origin, destination, afterTime);
    }

    @GetMapping("/{id}")
    public Ride getById(@PathVariable Long id) {
        return rideService.getRideById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ride not found: " + id));
    }

    @GetMapping("/me/driver")
    public List<Ride> myDriverRides() {
        return rideService.getRidesByDriver(currentUser.requireUser());
    }

    @PostMapping("/{id}/cancel")
    public void cancel(@PathVariable Long id) {
        rideService.cancelRide(id, currentUser.requireUser());
    }
}
