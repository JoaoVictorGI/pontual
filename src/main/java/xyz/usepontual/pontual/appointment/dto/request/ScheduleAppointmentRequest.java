package xyz.usepontual.pontual.appointment.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.time.Instant;
import java.util.UUID;

public record ScheduleAppointmentRequest(
        @NotBlank UUID providerId,
        @NotBlank UUID customerId,
        @NotEmpty @Future Instant startsAt,
        @NotEmpty @Future Instant endsAt) {}
