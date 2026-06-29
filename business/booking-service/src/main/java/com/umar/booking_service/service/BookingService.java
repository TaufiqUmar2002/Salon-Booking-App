package com.umar.booking_service.service;

import com.umar.booking_service.events.BookingEventProducer;
import com.umar.booking_service.exchange.SalonClient;
import com.umar.booking_service.exchange.ServiceClient;
import com.umar.booking_service.exchange.UserClient;
import com.umar.booking_service.mapper.BookingMapper;
import com.umar.booking_service.model.Booking;
import com.umar.booking_service.repository.BookingRepository;
import com.umar.booking_service.serviceinterface.IBookingService;
import com.umar.events.booking.BookingCreatedEvent;
import com.umar.exceptions.common.exception.ApiException;
import com.umar.payload.enums.booking.BookingStatus;
import com.umar.payload.request.booking.*;
import com.umar.payload.response.booking.BookingAvailabilityResponse;
import com.umar.payload.response.booking.BookingResponse;
import com.umar.payload.response.booking.BookingResponseV1;
import com.umar.payload.response.booking.UserBookingResponse;
import com.umar.payload.response.salon.SalonResponseV1;
import com.umar.payload.response.services.ServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


@RequiredArgsConstructor
@Service
public class BookingService implements IBookingService {

    private final BookingRepository bookingRepository;
    private final ServiceClient serviceClient;
    private final SalonClient salonClient;
    private final UserClient userClient;
    private final BookingMapper bookingMapper;
    private final BookingEventProducer eventProducer;


    @Override
    public BookingResponse createBooking(BookingRequest request) {
        SalonResponseV1 salonResponseV1 = salonClient.getSalonById(request.getSalonId());
        ServiceResponse serviceResponse = serviceClient.getServiceById(request.getServiceId());
//        UserProfileResponse profileResponse =userClient.viewUserProfile(request.getStaffId());
        String slotStartTime = request.getSlotStartTime();
        LocalDateTime   appointmentTime;
        try{
             appointmentTime = LocalDateTime.parse(slotStartTime);
        }
        catch (Exception e){
            throw new ApiException(HttpStatus.EXPECTATION_FAILED,"INVALID_ISO_FORMAT","Invalid ISO 8601 format. Expected YYYY-MM-DDTHH:MM:SS");
        }
        LocalDateTime now = LocalDateTime.now();
        if(!appointmentTime.isAfter(now)){
            throw new ApiException(HttpStatus.EXPECTATION_FAILED,"INVALID_SLOT_TIME","'Appointment time must be at least 15 minutes in the future");
        }
        String currentWeekday = LocalDate.now().getDayOfWeek().name().toLowerCase(Locale.ROOT);
        String openingClosingTime= salonResponseV1.getOpeningHours().get(currentWeekday);
        if(!isTimeSlotValid(appointmentTime,openingClosingTime)){
            throw new ApiException(HttpStatus.EXPECTATION_FAILED,"OUTSIDE_SALON_HOURS","The selected time is outside the salon’s operating hours");
        }
        LocalDateTime slotEndTime = appointmentTime.plusMinutes(serviceResponse.getDurationMinutes());
        List<BookingStatus> activeStatuses = List.of(BookingStatus.CONFIRMED, BookingStatus.PENDING);
        Integer booingCount = bookingRepository.findBookingWithExistingSlotTime(appointmentTime,slotEndTime,request.getSalonId(),request.getServiceId(),activeStatuses);
        if(booingCount>0){
            throw new ApiException(HttpStatus.EXPECTATION_FAILED,"SLOT_CONFLICT","This time slot is already booked. Please choose another");
        }
        Booking booking = Booking.builder()
                .status(BookingStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .customerNotes(request.getCustomerNotes())
                .salonId(request.getSalonId())
                .slotEndTime(slotEndTime)
                .slotStartTime(appointmentTime)
                .staffId(request.getStaffId())
                .totalPrice(serviceResponse.getCurrentPrice()
                        ).build();
        Booking persistBooking =bookingRepository.save(booking);
        BookingResponse response = bookingMapper.toResponse(persistBooking);
        BookingCreatedEvent bookingCreatedEvent = BookingCreatedEvent.builder()
                        .bookingId(persistBooking.getId())
                                .salonId(request.getSalonId())
                .serviceId(request.getServiceId())
                .producedAt(LocalDateTime.now())
                .slotStartTime(appointmentTime)
                .slotEndTime(slotEndTime)
                .totalPrice(serviceResponse.getCurrentPrice())
                .build();
        eventProducer.publishCreateBookingEvent(bookingCreatedEvent);
        return response;
    }

    @Override
    public BookingAvailabilityResponse getAvailableSlot(BookingAvailabilityRequest request) {
        SalonResponseV1 salonResponseV1 = salonClient.getSalonById(request.getSalonId());
        ServiceResponse serviceResponse = serviceClient.getServiceById(request.getServiceId());
        Integer durationMinutes = serviceResponse.getDurationMinutes();
        String currentWeekday = LocalDate.now().getDayOfWeek().name().toLowerCase(Locale.ROOT);
        String businessHoursStr= salonResponseV1.getOpeningHours().get(currentWeekday);
        List<LocalDateTime> possibleSlots = this.generateAllPossibleSlots(LocalDate.now(),businessHoursStr,durationMinutes);
        return BookingAvailabilityResponse.builder()
                .salonId(request.getSalonId())
                .serviceId(request.getServiceId())
                .serviceDuration(serviceResponse.getDurationMinutes())
                .date(request.getDate())
                .availableSlots(possibleSlots.stream().map(LocalDateTime::toString).toList())
                .fullyBooked(!possibleSlots.isEmpty() ?Boolean.FALSE:Boolean.TRUE)
                .totalAvailable(possibleSlots.size())
                .build();
    }

    @Override
    public BookingResponseV1 getBookingById(Long id) {
        Booking booking = this.bookingRepository.findById(id).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"BOOKING_NOT_FOUND","booking.notFound"));
        return bookingMapper.toResponseV1(booking);
    }

    @Override
    public UserBookingResponse getBookingByUser(Long id, UserBookingParamRequest request) {
        return null;
    }

    @Override
    public UserBookingResponse getBookingBySalonId(Long id, SalonBookingParamRequest request) {
        return null;
    }

    @Override
    public BookingResponse cancelBooking(String reason, Long id) {
        return null;
    }

    @Override
    public BookingResponse rescheduleBooking(Long id, RescheduleBookingRequest request) {
        return null;
    }

    @Override
    public BookingResponse completeBooking(Long id) {
        return null;
    }

    @Override
    public BookingResponse noShowBooking(Long id) {
        return null;
    }

    @Override
    public BookingResponse salonSummary(Long salonId) {
        return null;
    }

    @Override
    public UserBookingResponse getBookingByCategory(Long categoryId) {
        return null;
    }


    private boolean isTimeSlotValid(LocalDateTime appointmentDateTime,String businessHoursStr){
        if("CLOSED".equalsIgnoreCase(businessHoursStr)){
            return false;
        }
        LocalTime appointmentTime = appointmentDateTime.toLocalTime();
        String[] hours = businessHoursStr.split("-");
        LocalTime openingTime = LocalTime.parse(hours[0]); // Parses "09:00"
        LocalTime closingTime = LocalTime.parse(hours[1]);
        return !appointmentTime.isBefore(openingTime) && !appointmentTime.isAfter(closingTime);
    }

    public List<LocalDateTime> generateAllPossibleSlots(LocalDate targetDate, String businessHoursStr, int durationMinutes) {
        List<LocalDateTime> possibleSlots = new ArrayList<>();
        if(businessHoursStr==null || "CLOSED".equalsIgnoreCase(businessHoursStr)){
            return possibleSlots;
        }
        String[] hours = businessHoursStr.split("-");
        LocalTime openingTime = LocalTime.parse(hours[0]);
        LocalTime closingTime = LocalTime.parse(hours[1]);
        LocalDateTime currentSlot = LocalDateTime.of(targetDate,openingTime);
        LocalDateTime endOfBusiness = LocalDateTime.of(targetDate,closingTime);
        LocalDateTime currentTime = LocalDateTime.now();
        while (!currentSlot.plusMinutes(durationMinutes).isAfter(endOfBusiness) && !currentTime.isAfter(currentTime.plusMinutes(durationMinutes))){
            possibleSlots.add(currentSlot);
            currentSlot = currentSlot.plusMinutes(durationMinutes);
        }
        return possibleSlots;
    }
}
