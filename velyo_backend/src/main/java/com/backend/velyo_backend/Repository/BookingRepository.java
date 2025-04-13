package com.backend.velyo_backend.Repository;

import com.backend.velyo_backend.Entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {

    List<Booking> findBookingByUser_Email(String userEmail);

    @Query("SELECT b FROM bookings b WHERE b.user.email = :email AND (:date is NULL or (b.checkOut >= :date))")
    List<Booking> findBookingByUserEmailAndDate(@Param("email") String userEmail, LocalDate date);

    @Query("SELECT b from bookings b WHERE b.accommodation.id = :accommodationId")
    List<Booking> findByAccommodationId(UUID accommodationId);

    @Query("SELECT b FROM bookings b WHERE b.accommodation.id = :accommodationId AND " +
    "((:checkIn BETWEEN b.checkIn AND b.checkOut) OR " +
    "(:checkOut BETWEEN b.checkIn AND b.checkOut) OR " +
    "(b.checkIn BETWEEN :checkIn AND :checkOut) OR " +
    "(b.checkOut BETWEEN :checkIn AND :checkOut))")
    List<Booking> findOverlappingBookings(@Param("checkIn") LocalDate checkIn,
                                          @Param("checkOut") LocalDate checkOut,
                                          @Param("accommodationId") UUID accommodationId);


}
