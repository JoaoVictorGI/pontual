package xyz.usepontual.pontual.appointment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    private AppointmentMapper map;

    @Mock
    private AppointmentRepository repository;

    @InjectMocks
    private AppointmentService service;

    @Test
    void shouldScheduleNewAppointment() {
        var startsAt = Instant.now().plus(4, ChronoUnit.DAYS);
        var request = new ScheduleAppointmentRequest(
                UUID.randomUUID(), UUID.randomUUID(), startsAt, startsAt.plus(1, ChronoUnit.HOURS));

        var mappedAppointment = new AppointmentEntity();
        mappedAppointment.setProviderId(request.providerId());
        mappedAppointment.setCustomerId(request.customerId());
        mappedAppointment.setStartsAt(request.startsAt());
        mappedAppointment.setEndsAt(request.endsAt());

        var savedAppointment = new AppointmentEntity(
                UUID.randomUUID(),
                request.providerId(),
                request.customerId(),
                request.startsAt(),
                request.endsAt(),
                AppointmentStatus.SCHEDULED,
                Instant.now(),
                Instant.now());

        var response = new ScheduleAppointmentResponse(
                savedAppointment.getId(), savedAppointment.getStartsAt(), savedAppointment.getEndsAt());

        when(repository.existsOverlappingAppointment(request.startsAt(), request.endsAt(), request.providerId()))
                .thenReturn(false);

        when(map.scheduleAppointmentRequestToAppointment(any(ScheduleAppointmentRequest.class)))
                .thenReturn(mappedAppointment);

        when(repository.save(any(AppointmentEntity.class))).thenReturn(savedAppointment);
        when(map.appointmentToScheduleAppointmentResponse(savedAppointment)).thenReturn(response);

        var result = service.scheduleAppointment(request);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(response);
    }

    @Test
    void shouldThrowExceptionWhenOverlapExists() {
        var startsAt = Instant.now().plus(4, ChronoUnit.DAYS);
        var request = new ScheduleAppointmentRequest(
                UUID.randomUUID(), UUID.randomUUID(), startsAt, startsAt.plus(1, ChronoUnit.HOURS));

        when(repository.existsOverlappingAppointment(request.startsAt(), request.endsAt(), request.providerId()))
                .thenReturn(true);

        assertThatThrownBy(() -> service.scheduleAppointment(request))
                .isInstanceOf(TimeAlreadyScheduledException.class);
    }
}
