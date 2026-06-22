package xyz.usepontual.pontual.appointment;

import java.time.Instant;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentEntity, UUID> {

    @Query("""
    SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END
    FROM AppointmentEntity a
    WHERE a.startsAt < :endsAt AND a.endsAt > :startsAt
    AND a.providerId = :providerId
    """)
    boolean existsOverlappingAppointment(
            @Param("startsAt") Instant startsAt, @Param("endsAt") Instant endsAt, @Param("providerId") UUID providerId);
}
