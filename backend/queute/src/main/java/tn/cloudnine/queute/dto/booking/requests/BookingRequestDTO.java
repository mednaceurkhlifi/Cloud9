package tn.cloudnine.queute.dto.booking.requests;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingRequestDTO {
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Service ID is required")
    private Long serviceId;

    @NotNull(message = "Status is required")
    private String status;
}
