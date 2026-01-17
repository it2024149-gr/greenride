package gr.hua.dit.greenride.core.service;


import gr.hua.dit.greenride.core.model.Ride;
import gr.hua.dit.greenride.core.model.RideStatus;
import gr.hua.dit.greenride.core.model.User;
import gr.hua.dit.greenride.core.repository.RideRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RideServiceImpl implements RideService {

    private final RideRepository rideRepository;

    public RideServiceImpl(RideRepository rideRepository) {
        this.rideRepository = rideRepository;
    }

    @Override
    public Ride createRide(User driver, String origin, String destination,
                           LocalDateTime departureTime, int totalSeats) {

        int activeRides = rideRepository.countByDriverAndStatus(driver, RideStatus.PLANNED);
        if (activeRides >= 3) {
            throw new IllegalStateException("Ο οδηγός έχει ήδη 3 ενεργές διαδρομές!");
        }

        Ride ride = new Ride();
        ride.setDriver(driver);
        ride.setOrigin(origin);
        ride.setDestination(destination);
        ride.setDepartureTime(departureTime);
        ride.setTotalSeats(totalSeats);
        ride.setAvailableSeats(totalSeats);
        ride.setStatus(RideStatus.PLANNED);

        return rideRepository.save(ride);
    }

    @Override
    public List<Ride> searchRides(String origin, String destination, LocalDateTime afterTime) {
        return rideRepository.findByOriginAndDestinationAndDepartureTimeAfterAndStatus(
                origin, destination, afterTime, RideStatus.PLANNED
        );
    }

    @Override
    public Optional<Ride> getRideById(Long id) {
        return rideRepository.findById(id);
    }

    @Override
    public void cancelRide(Long rideId, User requester) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new IllegalArgumentException("Η διαδρομή δεν βρέθηκε!"));

        if (!requester.getRole().name().equals("ADMIN")
                && !ride.getDriver().getId().equals(requester.getId())) {
            throw new SecurityException("Δεν έχετε δικαίωμα ακύρωσης αυτής της διαδρομής!");
        }

        ride.setStatus(RideStatus.CANCELLED);
        rideRepository.save(ride);
    }

    @Override
    public List<Ride> getRidesByDriver(User driver) {
        return rideRepository.findByDriver(driver);
    }
}
