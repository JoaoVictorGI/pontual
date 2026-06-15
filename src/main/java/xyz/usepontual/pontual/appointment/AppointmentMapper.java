package xyz.usepontual.pontual.appointment;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import xyz.usepontual.pontual.appointment.dto.request.ScheduleAppointmentRequest;
import xyz.usepontual.pontual.appointment.dto.response.ScheduleAppointmentResponse;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    @Mapping(target = "appointmentId", source = "id")
    ScheduleAppointmentResponse appointmentToScheduleAppointmentResponse(AppointmentEntity appointment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    AppointmentEntity scheduleAppointmentRequestToAppointment(ScheduleAppointmentRequest request);
}
