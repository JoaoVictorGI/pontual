package xyz.usepontual.pontual.appointment.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Future;
import java.time.Instant;
import java.util.UUID;

public record ScheduleAppointmentRequest(
        UUID providerId,
        UUID customerId,
        @Future Instant startsAt,
        @Future Instant endsAt) {

    @AssertTrue(message = "endsAt must be after startsAt") public boolean isEndsAtAfterStartsAt() {
        return startsAt != null && endsAt != null && endsAt.isAfter(startsAt);
    }
}
