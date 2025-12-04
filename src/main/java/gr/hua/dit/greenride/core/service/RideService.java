package gr.hua.dit.greenride.core.service;

import gr.hua.dit.greenride.core.model.Ride;
import gr.hua.dit.greenride.core.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Υπηρεσία διαχείρισης διαδρομών (Ride).
 */
public interface RideService {

    /**
     * Δημιουργεί νέα διαδρομή από συγκεκριμένο οδηγό.
     */
    Ride createRide(User driver, String origin, String destination,
                    LocalDateTime departureTime, int totalSeats);

    /**
     * Αναζητά διαδρομές με βάση origin, destination και ώρα.
     */
    List<Ride> searchRides(String origin, String destination,
                           LocalDateTime afterTime);

    /**
     * Επιστρέφει διαδρομή βάσει id.
     */
    Optional<Ride> getRideById(Long id);

    /**
     * Ακυρώνει διαδρομή (μόνο ο οδηγός ή admin μπορεί).
     */
    void cancelRide(Long rideId, User requester);

    /**
     * Επιστρέφει όλες τις διαδρομές που έχει δημιουργήσει ο οδηγός.
     */
    List<Ride> getRidesByDriver(User driver);
}
