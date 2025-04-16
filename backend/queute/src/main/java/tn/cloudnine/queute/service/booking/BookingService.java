package tn.cloudnine.queute.service.booking;

import tn.cloudnine.queute.dto.booking.requests.BookingRequestDTO;
import tn.cloudnine.queute.dto.booking.response.BookingResponseDTO;

import java.util.List;

public interface BookingService {

    BookingResponseDTO createBooking(BookingRequestDTO request);
    BookingResponseDTO getBookingById(Long id);
    List<BookingResponseDTO> getAllBookings();
    BookingResponseDTO updateBooking(Long id, BookingRequestDTO request);
    void deleteBooking(Long id);
    List<BookingResponseDTO> getBookingsByUserId(Long userId);
    List<BookingResponseDTO> getBookingsByServiceId(Long serviceId);
}
