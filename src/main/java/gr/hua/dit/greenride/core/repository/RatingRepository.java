package gr.hua.dit.greenride.core.repository;


import gr.hua.dit.greenride.core.model.Rating;
import gr.hua.dit.greenride.core.model.Ride;
import gr.hua.dit.greenride.core.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    boolean existsByRideAndRaterAndRatee(Ride ride, User rater, User ratee);

    List<Rating> findByRatee(User ratee);

    @Query("""
        select coalesce(avg(r.stars * 1.0), 0)
        from Rating r
        where r.ratee = :ratee
    """)
    double averageStarsFor(User ratee);

    @Query("""
        select count(r)
        from Rating r
        where r.ratee = :ratee
    """)
    long countFor(User ratee);
}
