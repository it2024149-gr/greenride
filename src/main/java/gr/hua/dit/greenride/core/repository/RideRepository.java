package gr.hua.dit.greenride.core.repository;



import gr.hua.dit.greenride.core.model.Ride;
import gr.hua.dit.greenride.core.model.RideStatus;
import gr.hua.dit.greenride.core.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface RideRepository extends JpaRepository<Ride, Long> {

    // Πόσες διαδρομές έχει δημιουργήσει ένας οδηγός με συγκεκριμένο status
    int countByDriverAndStatus(User driver, RideStatus status);

    // Αναζήτηση διαδρομών (PLANNED μόνο)
    List<Ride> findByOriginAndDestinationAndDepartureTimeAfterAndStatus(
            String origin,
            String destination,
            LocalDateTime after,
            RideStatus status
    );

    // Όλες οι διαδρομές ενός οδηγού
    List<Ride> findByDriver(User driver);

    // ✅ ADMIN STAT: μέση πληρότητα ΜΟΝΟ για ενεργές (PLANNED) διαδρομές
    @Query("""
        select coalesce(
            avg( (r.totalSeats - r.availableSeats) * 1.0 / r.totalSeats ),
            0
        )
        from Ride r
        where r.status = gr.hua.dit.greenride.core.model.RideStatus.PLANNED
    """)
    double averageOccupancyRatioPlanned();
}
