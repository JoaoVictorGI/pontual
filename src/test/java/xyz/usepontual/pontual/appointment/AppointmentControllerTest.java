package xyz.usepontual.pontual.appointment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import tools.jackson.databind.ObjectMapper;
import xyz.usepontual.pontual.appointment.dto.request.ScheduleAppointmentRequest;
import xyz.usepontual.pontual.appointment.dto.response.FindAppointmentByIdResponse;
import xyz.usepontual.pontual.appointment.dto.response.ScheduleAppointmentResponse;
import xyz.usepontual.pontual.appointment.exception.AppointmentNotFoundException;
import xyz.usepontual.pontual.appointment.exception.TimeAlreadyScheduledException;

@WebMvcTest(controllers = {AppointmentController.class})
@WithMockUser(username = "admin")
class AppointmentControllerTest {

    @Autowired
    private MockMvcTester mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AppointmentService service;

    @Test
    void shouldScheduleNewAppointment() {
        var startsAt = Instant.now().plus(4, ChronoUnit.DAYS);
        var endsAt = startsAt.plus(1, ChronoUnit.HOURS);

        var request = new ScheduleAppointmentRequest(UUID.randomUUID(), UUID.randomUUID(), startsAt, endsAt);

        var response = new ScheduleAppointmentResponse(
                UUID.randomUUID(),
                request.providerId(),
                request.customerId(),
                request.startsAt(),
                request.endsAt(),
                AppointmentStatus.SCHEDULED);

        when(service.schedule(any(ScheduleAppointmentRequest.class))).thenReturn(response);

        mvc.post()
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .uri("/appointment")
                .with(csrf())
                .exchange()
                .assertThat()
                .hasStatus(HttpStatus.CREATED)
                .headers()
                .hasHeaderSatisfying(
                        "Location", value -> assertThat(value.getFirst()).endsWith("/appointment/" + response.id()));
    }

    @Test
    void shouldThrowExceptionWhenOverlapExists() {
        var startsAt = Instant.now().plus(4, ChronoUnit.DAYS);
        var endsAt = startsAt.plus(1, ChronoUnit.HOURS);

        var request = new ScheduleAppointmentRequest(UUID.randomUUID(), UUID.randomUUID(), startsAt, endsAt);

        when(service.schedule(any(ScheduleAppointmentRequest.class))).thenThrow(TimeAlreadyScheduledException.class);

        mvc.post()
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .uri("/appointment")
                .with(csrf())
                .exchange()
                .assertThat()
                .hasStatus(HttpStatus.CONFLICT);
    }

    @Test
    void shouldFindAppointmentById() {
        var startsAt = Instant.now().plus(4, ChronoUnit.DAYS);
        var endsAt = startsAt.plus(1, ChronoUnit.HOURS);
        var response = new FindAppointmentByIdResponse(
                UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), startsAt, endsAt, AppointmentStatus.SCHEDULED);

        when(service.findById(any(UUID.class))).thenReturn(response);

        mvc.get()
                .contentType(MediaType.APPLICATION_JSON)
                .uri("/appointment/" + response.id())
                .with(csrf())
                .exchange()
                .assertThat()
                .hasStatus(HttpStatus.OK)
                .bodyJson()
                .isEqualTo(objectMapper.writeValueAsString(response));
    }

    @Test
    void shouldThrowExceptionWhenAppointmentNotFound() {
        when(service.findById(any(UUID.class))).thenThrow(AppointmentNotFoundException.class);

        mvc.get()
                .contentType(MediaType.APPLICATION_JSON)
                .uri("/appointment/" + UUID.randomUUID())
                .with(csrf())
                .exchange()
                .assertThat()
                .hasStatus(HttpStatus.NOT_FOUND);
    }
}
