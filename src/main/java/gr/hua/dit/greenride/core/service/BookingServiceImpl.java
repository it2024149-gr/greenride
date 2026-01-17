package gr.hua.dit.greenride.core.service;



import gr.hua.dit.greenride.core.model.Booking;
import gr.hua.dit.greenride.core.model.BookingStatus;
import gr.hua.dit.greenride.core.model.Ride;
import gr.hua.dit.greenride.core.model.User;
import gr.hua.dit.greenride.core.repository.BookingRepository;
import gr.hua.dit.greenride.core.repository.RideRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final RideRepository rideRepository;

    public BookingServiceImpl(BookingRepository bookingRepository, RideRepository rideRepository) {
        this.bookingRepository = bookingRepository;
        this.rideRepository = rideRepository;
    }

    @Override
    public Booking createBooking(Ride ride, User passenger) {

        if (ride.getAvailableSeats() <= 0) {
            throw new IllegalStateException("Δεν υπάρχουν διαθέσιμες θέσεις!");
        }

        boolean exists = bookingRepository.existsByRideAndPassenger(ride, passenger);
        if (exists) {
            throw new IllegalStateException("Έχετε ήδη κράτηση για αυτή τη διαδρομή!");
        }

        Booking booking = new Booking();
        booking.setRide(ride);
        booking.setPassenger(passenger);
        booking.setStatus(BookingStatus.CONFIRMED);

        ride.setAvailableSeats(ride.getAvailableSeats() - 1);
        rideRepository.save(ride);

        return bookingRepository.save(booking);
    }

    @Override
    public void cancelBooking(Long bookingId, User requester) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Η κράτηση δεν βρέθηκε!"));

        if (!requester.getRole().name().equals("ADMIN")
                && !booking.getPassenger().getId().equals(requester.getId())) {
            throw new SecurityException("Δεν έχετε δικαίωμα ακύρωσης αυτής της κράτησης!");
        }

        booking.setStatus(BookingStatus.CANCELLED);

        Ride ride = booking.getRide();
        ride.setAvailableSeats(ride.getAvailableSeats() + 1);
        rideRepository.save(ride);

        bookingRepository.save(booking);
    }

    @Override
    public List<Booking> getBookingsByPassenger(User passenger) {
        return bookingRepository.findByPassenger(passenger);
    }
}
