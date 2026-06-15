package xyz.usepontual.pontual.appointment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.time.Instant;
import java.util.UUID;

public record ScheduleAppointmentRequest(
        @NotBlank UUID professionalId,
        @NotBlank UUID clientId,
        @NotEmpty Instant startsAt,
        @NotEmpty Instant endsAt) {}
