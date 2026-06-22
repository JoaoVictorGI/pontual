package xyz.usepontual.pontual.appointment;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import xyz.usepontual.pontual.appointment.dto.request.ScheduleAppointmentRequest;
import xyz.usepontual.pontual.appointment.dto.response.ScheduleAppointmentResponse;
import xyz.usepontual.pontual.appointment.exception.TimeAlreadyScheduledException;

@Service
public class AppointmentService {

    private final AppointmentRepository repository;
    private final AppointmentMapper map;

    public AppointmentService(AppointmentRepository repository, AppointmentMapper map) {
        this.repository = repository;
        this.map = map;
    }

    @Transactional
    public ScheduleAppointmentResponse scheduleAppointment(ScheduleAppointmentRequest appointment) {
        if (repository.existsOverlappingAppointment(
                appointment.startsAt(), appointment.endsAt(), appointment.providerId())) {
            throw new TimeAlreadyScheduledException("This time is already scheduled");
        }

        var newAppointment = map.scheduleAppointmentRequestToAppointment(appointment);
        newAppointment.setStatus(AppointmentStatus.SCHEDULED);

        return map.appointmentToScheduleAppointmentResponse(repository.save(newAppointment));
    }
}
