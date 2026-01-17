package gr.hua.dit.greenride.web.api;


import gr.hua.dit.greenride.core.model.Rating;
import gr.hua.dit.greenride.core.model.Ride;
import gr.hua.dit.greenride.core.model.User;
import gr.hua.dit.greenride.core.repository.RatingRepository;
import gr.hua.dit.greenride.core.repository.RideRepository;
import gr.hua.dit.greenride.core.repository.UserRepository;
import gr.hua.dit.greenride.core.security.CurrentUserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ratings")
public class RatingResource {

    private final RatingRepository ratingRepository;
    private final RideRepository rideRepository;
    private final UserRepository userRepository;
    private final CurrentUserService currentUser;

    public RatingResource(RatingRepository ratingRepository,
                          RideRepository rideRepository,
                          UserRepository userRepository,
                          CurrentUserService currentUser) {
        this.ratingRepository = ratingRepository;
        this.rideRepository = rideRepository;
        this.userRepository = userRepository;
        this.currentUser = currentUser;
    }

    public record RateRequest(
            @NotNull Long rideId,
            @NotNull Long rateeUserId,
            @Min(1) @Max(5) int stars,
            String comment
    ) {}

    @PostMapping
    public Rating rate(@Valid @RequestBody RateRequest req) {

        // logged-in user (από JWT)
        User rater = currentUser.requireUser();

        // user που αξιολογείται
        User ratee = userRepository.findById(req.rateeUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + req.rateeUserId()));

        if (rater.getId().equals(ratee.getId())) {
            throw new IllegalArgumentException("Cannot rate yourself");
        }

        // ride
        Ride ride = rideRepository.findById(req.rideId())
                .orElseThrow(() -> new IllegalArgumentException("Ride not found: " + req.rideId()));

        // (προαιρετικό) να μην βαθμολογεί 2 φορές το ίδιο άτομο για το ίδιο ride
        if (ratingRepository.existsByRideAndRaterAndRatee(ride, rater, ratee)) {
            throw new IllegalArgumentException("Already rated this user for this ride");
        }

        Rating rating = new Rating();
        rating.setRide(ride);
        rating.setRater(rater);
        rating.setRatee(ratee);
        rating.setStars(req.stars());
        rating.setComment(req.comment());

        return ratingRepository.save(rating);
    }
}
