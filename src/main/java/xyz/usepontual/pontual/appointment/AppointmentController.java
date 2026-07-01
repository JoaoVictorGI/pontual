package xyz.usepontual.pontual.appointment;

import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.usepontual.pontual.appointment.dto.request.ScheduleAppointmentRequest;
import xyz.usepontual.pontual.appointment.dto.response.CustomerAppointmentsResponse;
import xyz.usepontual.pontual.appointment.dto.response.ScheduleAppointmentResponse;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {

    private final AppointmentService service;

    public AppointmentController(AppointmentService service) {
        this.service = service;
    }

    @PostMapping("/schedule")
    public ResponseEntity<ScheduleAppointmentResponse> scheduleAppointment(
            @RequestBody @Valid ScheduleAppointmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.scheduleAppointment(request));
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<Page<CustomerAppointmentsResponse>> findCustomerAppointments(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(service.findCustomerAppointments(id, pageable));
    }
}
