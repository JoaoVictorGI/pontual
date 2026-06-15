package xyz.usepontual.pontual.appointment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import xyz.usepontual.pontual.TestcontainersConfiguration;
import xyz.usepontual.pontual.appointment.dto.request.ScheduleAppointmentRequest;
import xyz.usepontual.pontual.appointment.dto.response.ScheduleAppointmentResponse;
import xyz.usepontual.pontual.appointment.exception.TimeAlreadyScheduledException;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class AppointmentServiceIntegrationTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private AppointmentService service;

    @Test
    void shouldScheduleNewAppointment() {
        var startsAt = Instant.now().plus(4, ChronoUnit.DAYS).truncatedTo(ChronoUnit.MICROS);
        var request = new ScheduleAppointmentRequest(
                UUID.randomUUID(), UUID.randomUUID(), startsAt, startsAt.plus(1, ChronoUnit.HOURS));

        var result = service.scheduleAppointment(request);

        assertThat(result).isNotNull();
        assertThat(result)
                .isEqualTo(entityManager
                        .createQuery("""
                        SELECT new xyz.usepontual.pontual.appointment.dto.response.ScheduleAppointmentResponse(
                          p.id,
                          p.startsAt,
                          p.endsAt
                        )
                        FROM AppointmentEntity p
                        WHERE p.id = :appointmentId
                        """, ScheduleAppointmentResponse.class)
                        .setParameter("appointmentId", result.appointmentId())
                        .getSingleResult());
    }

    @Test
    void shouldThrowExceptionWhenOverlapExists() {
        var startsAt = Instant.now().plus(4, ChronoUnit.DAYS).plus(1, ChronoUnit.HOURS);
        var request = new ScheduleAppointmentRequest(
                UUID.randomUUID(), UUID.randomUUID(), startsAt, startsAt.plus(1, ChronoUnit.DAYS));

        service.scheduleAppointment(request);

        assertThatThrownBy(() -> service.scheduleAppointment(request))
                .isInstanceOf(TimeAlreadyScheduledException.class)
                .hasMessage("This time is already scheduled");
    }
}
