package xyz.usepontual.pontual.appointment.dto.response;

import java.time.Instant;
import java.util.UUID;
import xyz.usepontual.pontual.appointment.Appointment;
import xyz.usepontual.pontual.appointment.AppointmentStatus;

public record FindAppointmentByIdResponse(
        UUID id, UUID providerId, UUID customerId, Instant startsAt, Instant endsAt, AppointmentStatus status) {

    public static FindAppointmentByIdResponse fromEntity(Appointment entity) {
        return new FindAppointmentByIdResponse(
                entity.getId(),
                entity.getProviderId(),
                entity.getCustomerId(),
                entity.getStartsAt(),
                entity.getEndsAt(),
                entity.getStatus());
    }
}
