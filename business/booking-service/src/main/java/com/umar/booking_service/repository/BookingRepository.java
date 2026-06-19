package com.umar.booking_service.repository;

import com.umar.booking_service.model.Booking;
import com.umar.payload.constants.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking,Long> {


    @Query(value = """
      SELECT count(b) FROM Booking b where b.salonId=:salonId and b.serviceId=:serviceId
      and b.slotStartTime <:slotStartTime and b.slotEndTime >:slotEndTime and b.status in (:activeStatus)
""",nativeQuery = false)
    Integer findBookingWithExistingSlotTime(LocalDateTime slotStartTime, LocalDateTime slotEndTime, Long salonId, Long serviceId, List<BookingStatus> activeStatus);

}
