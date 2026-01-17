package gr.hua.dit.greenride.core.model;


import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(
        name = "rating",
        uniqueConstraints = @UniqueConstraint(columnNames = {"ride_id", "rater_id", "ratee_id"})
)
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Η διαδρομή που αξιολογείται
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ride_id", nullable = false)
    private Ride ride;

    // Ποιος αξιολογεί
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "rater_id", nullable = false)
    private User rater;

    // Ποιος αξιολογείται
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ratee_id", nullable = false)
    private User ratee;

    // 1..5
    @Column(nullable = false)
    private int stars;

    @Column(length = 500)
    private String comment;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    public Rating() {}

    public Long getId() { return id; }

    public Ride getRide() { return ride; }
    public void setRide(Ride ride) { this.ride = ride; }

    public User getRater() { return rater; }
    public void setRater(User rater) { this.rater = rater; }

    public User getRatee() { return ratee; }
    public void setRatee(User ratee) { this.ratee = ratee; }

    public int getStars() { return stars; }
    public void setStars(int stars) { this.stars = stars; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public Instant getCreatedAt() { return createdAt; }
}
