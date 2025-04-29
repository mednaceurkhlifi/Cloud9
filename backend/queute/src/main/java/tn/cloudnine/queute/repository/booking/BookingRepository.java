package tn.cloudnine.queute.repository.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tn.cloudnine.queute.model.booking.Booking;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findByReference(String reference);

    @Query("SELECT b FROM Booking b WHERE b.user.userId = :userId")
    List<Booking> findBookingsByUser_UserId (@Param("userId") Long userId);
    @Query("SELECT b FROM Booking b WHERE b.services.serviceId = :serviceId")
    List<Booking> findBookingsByServices_ServiceId(@Param("serviceId") Long userId);



    Booking findBookingByReference(String reference);
}
