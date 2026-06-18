package xyz.usepontual.pontual.appointment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import xyz.usepontual.pontual.TestcontainersConfiguration;
import xyz.usepontual.pontual.appointment.dto.request.ScheduleAppointmentRequest;
import xyz.usepontual.pontual.appointment.exception.TimeAlreadyScheduledException;

@Sql(value = "/db/seeding/test_schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Import(TestcontainersConfiguration.class)
@SpringBootTest
class AppointmentServiceIntegrationTest {

    @Autowired
    private AppointmentService service;

    @Autowired
    private AppointmentRepository repository;

    private static final UUID CUSTOMER_ID = UUID.fromString("fe8836fe-c8ef-4489-a18f-3f56c6527725");
    private static final UUID PROVIDER_ID = UUID.fromString("31fcf919-a5c0-48cf-97a7-48d194b5714f");

    @Test
    void shouldScheduleNewAppointment() {
        var startsAt = Instant.now().plus(4, ChronoUnit.DAYS).truncatedTo(ChronoUnit.MICROS);
        var endsAt = startsAt.plus(1, ChronoUnit.HOURS);

        var request = new ScheduleAppointmentRequest(PROVIDER_ID, CUSTOMER_ID, startsAt, endsAt);

        var result = service.scheduleAppointment(request);

        assertThat(result).isNotNull();
        assertThat(result.appointmentId()).isNotNull();

        var persisted = repository.findById(result.appointmentId()).orElseThrow();

        assertThat(persisted).isNotNull();
        assertThat(persisted.getProviderId()).isEqualTo(PROVIDER_ID);
        assertThat(persisted.getCustomerId()).isEqualTo(CUSTOMER_ID);
        assertThat(persisted.getStartsAt()).isEqualTo(startsAt);
        assertThat(persisted.getEndsAt()).isEqualTo(endsAt);
    }

    @Test
    void shouldThrowExceptionWhenOverlapExists() {
        var startsAt = Instant.now().plus(4, ChronoUnit.DAYS).truncatedTo(ChronoUnit.MICROS);
        var endsAt = startsAt.plus(1, ChronoUnit.HOURS);

        var request = new ScheduleAppointmentRequest(PROVIDER_ID, CUSTOMER_ID, startsAt, endsAt);

        assertThatThrownBy(() -> service.scheduleAppointment(request))
                .isInstanceOf(TimeAlreadyScheduledException.class)
                .hasMessage("This time is already scheduled");
    }
}
