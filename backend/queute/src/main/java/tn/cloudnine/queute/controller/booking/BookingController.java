package tn.cloudnine.queute.controller.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.cloudnine.queute.dto.booking.requests.BookingRequestDTO;
import tn.cloudnine.queute.dto.booking.response.BookingResponseDTO;
import tn.cloudnine.queute.repository.booking.BookingRepository;
import tn.cloudnine.queute.service.blockchain.BlockchainService;
import tn.cloudnine.queute.service.booking.BookingService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BookingController {
    @Autowired
    private final BookingService bookingService;
    @Autowired
    private final BlockchainService blockchainService;

    @PostMapping
    public ResponseEntity<BookingResponseDTO> createBooking(@Valid @RequestBody BookingRequestDTO request) {
        return ResponseEntity.ok(bookingService.createBooking(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @GetMapping
    public ResponseEntity<List<BookingResponseDTO>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> updateBooking(
            @PathVariable Long id,
            @Valid @RequestBody BookingRequestDTO request) {
        return ResponseEntity.ok(bookingService.updateBooking(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(bookingService.getBookingsByUserId(userId));
    }

    @GetMapping("/service/{serviceId}")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByServiceId(@PathVariable Long serviceId) {
        return ResponseEntity.ok(bookingService.getBookingsByServiceId(serviceId));
    }
    
    @GetMapping("/verify/{reference}")
    public ResponseEntity<Map<String, Boolean>> verifyBookingOnBlockchain(@PathVariable String reference) {
        boolean verified = blockchainService.verifyBookingOnBlockchain(reference);
        return ResponseEntity.ok(Map.of("verified", verified));
    }
}
