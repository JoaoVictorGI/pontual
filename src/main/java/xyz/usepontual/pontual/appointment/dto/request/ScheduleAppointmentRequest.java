package xyz.usepontual.pontual.appointment.dto.request;

import java.time.Instant;
import java.util.UUID;
import xyz.usepontual.pontual.appointment.Appointment;

public record ScheduleAppointmentRequest(UUID providerId, UUID customerId, Instant startsAt, Instant endsAt) {

    public static Appointment toEntity(ScheduleAppointmentRequest dto) {
        var appointment = new Appointment();

        appointment.setProviderId(dto.providerId());
        appointment.setCustomerId(dto.customerId());
        appointment.setStartsAt(dto.startsAt());
        appointment.setEndsAt(dto.endsAt());

        return appointment;
    }
}
