package com.umar.booking_service.repository;

import com.umar.booking_service.model.Booking;
import com.umar.payload.enums.booking.BookingStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.util.Pair;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking,Long> {


    @Query(value = """
      SELECT count(b) FROM Booking b where b.salonId=:salonId and b.serviceId=:serviceId
      and b.slotStartTime <:slotStartTime and b.slotEndTime >:slotEndTime and b.status in (:activeStatus)
""",nativeQuery = false)
    Integer findBookingWithExistingSlotTime(LocalDateTime slotStartTime, LocalDateTime slotEndTime, Long salonId, Long serviceId, List<BookingStatus> activeStatus);


    @Query("""
      SELECT b.slotStartTime, b.slotEndTime FROM Booking b where b.salonId=:salonId and b.serviceId=:serviceId
      and b.slotStartTime <:slotStartTime and b.slotEndTime >:slotEndTime and b.status in (:activeStatus) AND CASE when :staffId is not null then b.staffId=:staffId else true end
""")
    List<Pair<LocalDateTime,LocalDateTime>> fetchALlBookedSlots(LocalDateTime slotStartTime, LocalDateTime slotEndTime, Long salonId, Long serviceId, List<BookingStatus> activeStatus,Long staffId);


    @Query("""
      SELECT b FROM Booking b where b.userId=:userId and b.status in (:activeStatus) and b.slotStartTime >= :startDate and b.slotEndTime <= :endDate
""")
    List<Booking> fetchAllBookingSByUserId(Long userId, Pageable pageable,LocalDateTime startDate,LocalDateTime endDate,BookingStatus activeStatus);

    @Query("""
      SELECT b FROM Booking b where b.salonId=:salonId
""")
    List<Booking> findAllBySalonId(@Param("salonId") Long salonId);
}
