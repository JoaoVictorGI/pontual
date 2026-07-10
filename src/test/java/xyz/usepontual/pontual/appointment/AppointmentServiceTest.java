package xyz.usepontual.pontual.appointment;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import xyz.usepontual.pontual.appointment.dto.request.ScheduleAppointmentRequest;
import xyz.usepontual.pontual.appointment.dto.response.ScheduleAppointmentResponse;
import xyz.usepontual.pontual.appointment.exception.TimeAlreadyScheduledException;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {
    @Mock
    private AppointmentRepository repository;

    @InjectMocks
    private AppointmentService service;

    @Test
    void shouldScheduleNewAppointment() {
        var startsAt = Instant.now().plus(4, ChronoUnit.DAYS);
        var endsAt = startsAt.plus(1, ChronoUnit.HOURS);
        var request = new ScheduleAppointmentRequest(UUID.randomUUID(), UUID.randomUUID(), startsAt, endsAt);

        var savedAppointment = ScheduleAppointmentRequest.toEntity(request);
        savedAppointment.setId(UUID.randomUUID());

        when(repository.existsOverlappingAppointment(request.startsAt(), request.endsAt(), request.providerId()))
                .thenReturn(false);

        when(repository.save(any(Appointment.class))).thenReturn(savedAppointment);

        var result = service.schedule(request);

        assertEquals(ScheduleAppointmentResponse.fromEntity(savedAppointment), result);
        assertNotNull(result.id());
    }

    @Test
    void shouldThrowExceptionWhenOverlapExists() {
        var startsAt = Instant.now().plus(4, ChronoUnit.DAYS);
        var endsAt = startsAt.plus(1, ChronoUnit.HOURS);
        var request = new ScheduleAppointmentRequest(UUID.randomUUID(), UUID.randomUUID(), startsAt, endsAt);

        when(repository.existsOverlappingAppointment(request.startsAt(), request.endsAt(), request.providerId()))
                .thenReturn(true);

        assertThrows(TimeAlreadyScheduledException.class, () -> service.schedule(request));
    }
}
