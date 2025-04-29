package tn.cloudnine.queute.service.booking.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.cloudnine.queute.dto.booking.requests.BookingRequestDTO;
import tn.cloudnine.queute.dto.booking.response.BookingResponseDTO;
import tn.cloudnine.queute.model.booking.Booking;
import tn.cloudnine.queute.repository.booking.BookingRepository;
import tn.cloudnine.queute.repository.serviceRepo.ServiceRepository;
import tn.cloudnine.queute.repository.user.UserRepository;
import tn.cloudnine.queute.service.blockchain.BlockchainService;
import tn.cloudnine.queute.service.booking.BookingService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    @Autowired
    private final BookingRepository bookingRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final ServiceRepository serviceRepository;
    @Autowired
    private final BlockchainService blockchainService;

    @Override
    public BookingResponseDTO createBooking(BookingRequestDTO request) {
        var user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + request.getUserId()));

        var service = serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new IllegalArgumentException("Service not found with id: " + request.getServiceId()));

        Booking booking = new Booking();
        booking.setReference(UUID.randomUUID().toString());
        booking.setStatus(request.getStatus());
        booking.setUser(user);
        booking.setServices(service);
        booking.setCreated_at(LocalDateTime.now());
        booking.setUpdated_at(LocalDateTime.now());

        Booking savedBooking = bookingRepository.save(booking);
        
        // Add to blockchain
        boolean blockchainResult = blockchainService.createBookingOnBlockchain(savedBooking);
        if (!blockchainResult) {
            // Log the blockchain failure but still return the database booking
            // In a production environment, you might consider implementing a retry mechanism
            // or reverting the database transaction
        }
        
        return mapToResponseDTO(savedBooking);
    }

    @Override
    public BookingResponseDTO getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + id));
        return mapToResponseDTO(booking);
    }

    @Override
    public List<BookingResponseDTO> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());    }

    @Override
    public BookingResponseDTO updateBooking(Long id, BookingRequestDTO request) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + id));

        booking.setStatus(request.getStatus());
        booking.setUpdated_at(LocalDateTime.now());

        Booking updatedBooking = bookingRepository.save(booking);
        
        // Update on blockchain
        boolean blockchainResult = blockchainService.updateBookingOnBlockchain(updatedBooking);
        if (!blockchainResult) {
            // Log the blockchain failure but still return the updated database booking
        }
        
        return mapToResponseDTO(updatedBooking);
    }

    @Override
    public void deleteBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + id));
        
        // Delete from blockchain first
        boolean blockchainResult = blockchainService.deleteBookingOnBlockchain(booking.getReference());
        if (!blockchainResult) {
            // Log the blockchain failure but still proceed with database deletion
        }
        
        bookingRepository.deleteById(id);
    }

    @Override
    public List<BookingResponseDTO> getBookingsByUserId(Long userId) {
        return bookingRepository.findBookingsByUser_UserId(userId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingResponseDTO> getBookingsByServiceId(Long serviceId) {
        return bookingRepository.findBookingsByServices_ServiceId(serviceId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
    
    private BookingResponseDTO mapToResponseDTO(Booking booking) {
        BookingResponseDTO dto = new BookingResponseDTO();
        dto.setId(booking.getId());
        dto.setReference(booking.getReference());
        dto.setStatus(booking.getStatus());
        dto.setUserId(booking.getUser().getUserId());
        dto.setUserName(booking.getUser().getFullName());
        dto.setServiceId(booking.getServices().getServiceId());
        dto.setServiceName(booking.getServices().getServiceName());
        dto.setCreatedAt(booking.getCreated_at());
        dto.setUpdatedAt(booking.getUpdated_at());
        
        // Add blockchain verification status
        dto.setBlockchainVerified(blockchainService.verifyBookingOnBlockchain(booking.getReference()));
        
        return dto;
    }
}
