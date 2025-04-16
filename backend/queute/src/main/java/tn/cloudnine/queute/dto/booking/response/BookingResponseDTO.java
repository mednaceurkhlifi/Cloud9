package tn.cloudnine.queute.dto.booking.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponseDTO {
    private Long id;
    private String reference;
    private String status;
    private Long userId;
    private String userName;
    private Long serviceId;
    private String serviceName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean blockchainVerified;
}
