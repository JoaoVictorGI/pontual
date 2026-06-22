package xyz.usepontual.pontual.appointment.dto.response;

import java.time.Instant;
import java.util.UUID;

public record ScheduleAppointmentResponse(UUID appointmentId, Instant startsAt, Instant endsAt) {}
