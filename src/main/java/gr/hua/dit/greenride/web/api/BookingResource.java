package gr.hua.dit.greenride.web.api;



import gr.hua.dit.greenride.core.model.Booking;
import gr.hua.dit.greenride.core.security.CurrentUserService;
import gr.hua.dit.greenride.core.service.BookingService;
import gr.hua.dit.greenride.core.service.RideService;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingResource {

    private final BookingService bookingService;
    private final RideService rideService;
    private final CurrentUserService currentUser;

    public BookingResource(BookingService bookingService,
                           RideService rideService,
                           CurrentUserService currentUser) {
        this.bookingService = bookingService;
        this.rideService = rideService;
        this.currentUser = currentUser;
    }

    public record CreateBookingRequest(@NotNull Long rideId) {}

    @PostMapping
    public Booking create(@RequestBody CreateBookingRequest req) {
        var ride = rideService.getRideById(req.rideId())
                .orElseThrow(() -> new IllegalArgumentException("Ride not found: " + req.rideId()));

        return bookingService.createBooking(ride, currentUser.requireUser());
    }

    @GetMapping("/me")
    public List<Booking> myBookings() {
        return bookingService.getBookingsByPassenger(currentUser.requireUser());
    }

    @PostMapping("/{id}/cancel")
    public void cancel(@PathVariable Long id) {
        bookingService.cancelBooking(id, currentUser.requireUser());
    }
}
