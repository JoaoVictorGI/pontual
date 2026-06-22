package xyz.usepontual.pontual.appointment.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

public record ScheduleAppointmentRequest(
        @NotNull UUID providerId,
        @NotNull UUID customerId,
        @NotNull @Future Instant startsAt,
        @NotNull @Future Instant endsAt) {

    @AssertTrue(message = "endsAt must be after startsAt") public boolean isEndsAtAfterStartsAt() {
        return startsAt != null && endsAt != null && endsAt.isAfter(startsAt);
    }
}
