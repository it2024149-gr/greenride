package gr.hua.dit.greenride.core.repository;

import gr.hua.dit.greenride.core.model.Booking;
import gr.hua.dit.greenride.core.model.Ride;
import gr.hua.dit.greenride.core.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository για οντότητα Booking.
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Έχει ήδη κάνει κράτηση αυτός ο επιβάτης για την ίδια διαδρομή;
    boolean existsByRideAndPassenger(Ride ride, User passenger);

    // Όλες οι κρατήσεις ενός επιβάτη (φέρνει και ride + driver για το UI)
    @Query("""
    select b from Booking b
    join fetch b.ride r
    join fetch r.driver
    where b.passenger = :passenger
    """)
    List<Booking> findByPassengerWithRide(@Param("passenger") User passenger);

    // (προαιρετικά) κρατάμε και το απλό
    List<Booking> findByPassenger(User passenger);
}
