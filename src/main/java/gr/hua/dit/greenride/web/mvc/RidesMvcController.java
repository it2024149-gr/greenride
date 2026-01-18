package gr.hua.dit.greenride.web.mvc;



import gr.hua.dit.greenride.core.model.Ride;
import gr.hua.dit.greenride.core.model.User;
import gr.hua.dit.greenride.core.repository.UserRepository;
import gr.hua.dit.greenride.core.service.BookingService;
import gr.hua.dit.greenride.core.service.RideService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

        import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/rides")
public class RidesMvcController {

    private final RideService rideService;
    private final BookingService bookingService;
    private final UserRepository userRepository;

    public RidesMvcController(RideService rideService, BookingService bookingService, UserRepository userRepository) {
        this.rideService = rideService;
        this.bookingService = bookingService;
        this.userRepository = userRepository;
    }

    // --------- FORM DTO (για Thymeleaf binding) ----------
    public static class RideCreateForm {
        @NotBlank public String origin;
        @NotBlank public String destination;

        @NotNull
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        public LocalDateTime departureTime;

        @Min(1) public int totalSeats = 1;

        public String getOrigin() { return origin; }
        public void setOrigin(String origin) { this.origin = origin; }
        public String getDestination() { return destination; }
        public void setDestination(String destination) { this.destination = destination; }
        public LocalDateTime getDepartureTime() { return departureTime; }
        public void setDepartureTime(LocalDateTime departureTime) { this.departureTime = departureTime; }
        public int getTotalSeats() { return totalSeats; }
        public void setTotalSeats(int totalSeats) { this.totalSeats = totalSeats; }
    }

    private User currentUser(Principal principal) {
        String username = principal.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Logged-in user not found in DB: " + username));
    }

    // --------- PAGE ----------
    @GetMapping
    public String ridesPage(
            @RequestParam(required = false) String origin,
            @RequestParam(required = false) String destination,
            Model model,
            Principal principal
    ) {
        User me = currentUser(principal);

        List<Ride> myRides = rideService.getRidesByDriver(me);
        model.addAttribute("myRides", myRides);

        // Search results (προαιρετικό)
        if ((origin != null && !origin.isBlank()) || (destination != null && !destination.isBlank())) {
            List<Ride> results = rideService.searchRides(
                    origin == null ? "" : origin,
                    destination == null ? "" : destination,
                    LocalDateTime.now().minusYears(5) // “afterTime” χαλαρό για UI
            );
            model.addAttribute("results", results);
        }

        model.addAttribute("origin", origin == null ? "" : origin);
        model.addAttribute("destination", destination == null ? "" : destination);

        if (!model.containsAttribute("createForm")) {
            model.addAttribute("createForm", new RideCreateForm());
        }

        return "rides";
    }

    // --------- CREATE RIDE ----------
    @PostMapping("/create")
    public String createRide(
            @ModelAttribute("createForm") RideCreateForm form,
            BindingResult br,
            Principal principal,
            Model model
    ) {
        if (br.hasErrors()) {
            model.addAttribute("msgErr", "Please fill all required fields.");
            return ridesPage(null, null, model, principal);
        }

        User me = currentUser(principal);

        try {
            rideService.createRide(me, form.origin.trim(), form.destination.trim(), form.departureTime, form.totalSeats);
            return "redirect:/rides?created=1";
        } catch (Exception ex) {
            model.addAttribute("msgErr", ex.getMessage());
            return ridesPage(null, null, model, principal);
        }
    }

    // --------- BOOK RIDE ----------
    @PostMapping("/{rideId}/book")
    public String bookRide(@PathVariable Long rideId, Principal principal) {
        User me = currentUser(principal);
        Ride ride = rideService.getRideById(rideId)
                .orElseThrow(() -> new IllegalArgumentException("Ride not found"));

        bookingService.createBooking(ride, me);
        return "redirect:/bookings?booked=1";
    }

    // --------- CANCEL RIDE ----------
    @PostMapping("/{rideId}/cancel")
    public String cancelRide(@PathVariable Long rideId, Principal principal) {
        User me = currentUser(principal);
        rideService.cancelRide(rideId, me);
        return "redirect:/rides?canceled=1";
    }
}
