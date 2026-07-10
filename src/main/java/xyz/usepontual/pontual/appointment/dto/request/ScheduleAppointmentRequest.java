package xyz.usepontual.pontual.appointment.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Future;
import java.time.Instant;
import java.util.UUID;
import xyz.usepontual.pontual.appointment.Appointment;

public record ScheduleAppointmentRequest(
        UUID providerId,
        UUID customerId,
        @Future Instant startsAt,
        @Future Instant endsAt) {

    @AssertTrue(message = "endsAt must be after startsAt") public boolean isEndsAtAfterStartsAt() {
        return startsAt != null && endsAt != null && endsAt.isAfter(startsAt);
    }

    public static Appointment toEntity(ScheduleAppointmentRequest dto) {
        var appointment = new Appointment();

        appointment.setProviderId(dto.providerId());
        appointment.setCustomerId(dto.customerId());
        appointment.setStartsAt(dto.startsAt());
        appointment.setEndsAt(dto.endsAt());

        return appointment;
    }
}
