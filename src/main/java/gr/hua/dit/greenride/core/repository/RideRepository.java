package gr.hua.dit.greenride.core.repository;



import gr.hua.dit.greenride.core.model.Ride;
import gr.hua.dit.greenride.core.model.RideStatus;
import gr.hua.dit.greenride.core.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository για οντότητα Ride.
 */
@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {

    // Πόσες ενεργές (PLANNED) διαδρομές έχει ο οδηγός
    int countByDriverAndStatus(User driver, RideStatus status);

    // Αναζήτηση διαδρομών
    List<Ride> findByOriginAndDestinationAndDepartureTimeAfterAndStatus(
            String origin,
            String destination,
            LocalDateTime departureTime,
            RideStatus status
    );

    // Όλες οι διαδρομές ενός οδηγού
    List<Ride> findByDriver(User driver);
}
