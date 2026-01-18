package gr.hua.dit.greenride.web.mvc;



import gr.hua.dit.greenride.core.model.Booking;
import gr.hua.dit.greenride.core.model.User;
import gr.hua.dit.greenride.core.repository.UserRepository;
import gr.hua.dit.greenride.core.service.BookingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

        import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/bookings")
public class BookingsMvcController {

    private final BookingService bookingService;
    private final UserRepository userRepository;

    public BookingsMvcController(BookingService bookingService, UserRepository userRepository) {
        this.bookingService = bookingService;
        this.userRepository = userRepository;
    }

    private User currentUser(Principal principal) {
        String username = principal.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Logged-in user not found in DB: " + username));
    }

    @GetMapping
    public String bookingsPage(Model model, Principal principal) {
        User me = currentUser(principal);
        List<Booking> bookings = bookingService.getBookingsByPassenger(me);
        model.addAttribute("bookings", bookings);
        return "bookings";
    }

    @PostMapping("/{bookingId}/cancel")
    public String cancelBooking(@PathVariable Long bookingId, Principal principal) {
        User me = currentUser(principal);
        bookingService.cancelBooking(bookingId, me);
        return "redirect:/bookings?canceled=1";
    }
}
