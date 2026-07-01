package xyz.usepontual.pontual.appointment.dto.response;

import java.time.Instant;
import java.util.UUID;
import xyz.usepontual.pontual.appointment.AppointmentStatus;

public record CustomerAppointmentsResponse(
        UUID id, UUID providerId, Instant startsAt, Instant endsAt, AppointmentStatus status) {}
