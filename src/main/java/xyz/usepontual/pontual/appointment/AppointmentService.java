package xyz.usepontual.pontual.appointment;

import jakarta.transaction.Transactional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import xyz.usepontual.pontual.appointment.dto.request.ScheduleAppointmentRequest;
import xyz.usepontual.pontual.appointment.dto.response.FindAppointmentByIdResponse;
import xyz.usepontual.pontual.appointment.dto.response.ScheduleAppointmentResponse;
import xyz.usepontual.pontual.appointment.exception.AppointmentNotFoundException;
import xyz.usepontual.pontual.appointment.exception.TimeAlreadyScheduledException;

@Service
public class AppointmentService {

    private final AppointmentRepository repository;

    public AppointmentService(AppointmentRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ScheduleAppointmentResponse schedule(ScheduleAppointmentRequest request) {
        if (repository.existsOverlappingAppointment(request.startsAt(), request.endsAt(), request.providerId())) {
            throw new TimeAlreadyScheduledException(
                    "Time slot overlaps with an existing appointment for this provider");
        }

        return ScheduleAppointmentResponse.fromEntity(repository.save(ScheduleAppointmentRequest.toEntity(request)));
    }

    public FindAppointmentByIdResponse findById(UUID id) {
        return FindAppointmentByIdResponse.fromEntity(repository
                .findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment " + id + " not found")));
    }
}
