package gr.hua.dit.greenride.core.service;



import gr.hua.dit.greenride.core.model.Rating;
import gr.hua.dit.greenride.core.model.Ride;
import gr.hua.dit.greenride.core.model.User;

public interface RatingService {
    Rating rate(User rater, User ratee, Ride ride, int stars, String comment);
}
