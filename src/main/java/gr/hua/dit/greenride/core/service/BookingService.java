package gr.hua.dit.greenride.core.service;


import gr.hua.dit.greenride.core.model.Booking;
import gr.hua.dit.greenride.core.model.Ride;
import gr.hua.dit.greenride.core.model.User;

import java.util.List;

public interface BookingService {

    /**
     * Δημιουργεί νέα κράτηση για επιβάτη σε συγκεκριμένη διαδρομή.
     */
    Booking createBooking(Ride ride, User passenger);

    /**
     * Ακυρώνει κράτηση (μόνο ο ίδιος επιβάτης ή admin).
     */
    void cancelBooking(Long bookingId, User requester);

    /**
     * Επιστρέφει όλες τις κρατήσεις ενός επιβάτη.
     */
    List<Booking> getBookingsByPassenger(User passenger);
}
