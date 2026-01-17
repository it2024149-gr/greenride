package gr.hua.dit.greenride.core.service;



import gr.hua.dit.greenride.core.model.Rating;
import gr.hua.dit.greenride.core.model.Ride;
import gr.hua.dit.greenride.core.model.User;
import gr.hua.dit.greenride.core.repository.RatingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;

    public RatingServiceImpl(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    @Override
    @Transactional
    public Rating rate(User rater, User ratee, Ride ride, int stars, String comment) {

        if (rater.getId().equals(ratee.getId())) {
            throw new IllegalArgumentException("Cannot rate yourself");
        }
        if (stars < 1 || stars > 5) {
            throw new IllegalArgumentException("stars must be 1..5");
        }
        if (ratingRepository.existsByRideAndRaterAndRatee(ride, rater, ratee)) {
            throw new IllegalArgumentException("Already rated this user for this ride");
        }

        Rating rating = new Rating();
        rating.setRide(ride);
        rating.setRater(rater);
        rating.setRatee(ratee);
        rating.setStars(stars);
        rating.setComment(comment);

        return ratingRepository.save(rating);
    }
}
