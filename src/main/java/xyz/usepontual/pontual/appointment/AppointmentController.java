package xyz.usepontual.pontual.appointment;

import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import xyz.usepontual.pontual.appointment.dto.request.ScheduleAppointmentRequest;
import xyz.usepontual.pontual.appointment.dto.response.FindAppointmentByIdResponse;
import xyz.usepontual.pontual.appointment.dto.response.ScheduleAppointmentResponse;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {

    private final AppointmentService service;

    public AppointmentController(AppointmentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ScheduleAppointmentResponse> schedule(
            @RequestBody @Valid ScheduleAppointmentRequest request) {
        var scheduledAppointment = service.schedule(request);

        var uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(scheduledAppointment.id())
                .toUri();

        return ResponseEntity.created(uri).body(scheduledAppointment);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FindAppointmentByIdResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }
}
