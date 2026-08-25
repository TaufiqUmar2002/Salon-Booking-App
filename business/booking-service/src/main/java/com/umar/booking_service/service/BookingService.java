package com.umar.booking_service.service;

import com.umar.booking_service.events.BookingEventProducer;
import com.umar.booking_service.exchange.SalonClient;
import com.umar.booking_service.exchange.ServiceClient;
import com.umar.booking_service.exchange.UserClient;
import com.umar.booking_service.mapper.BookingMapper;
import com.umar.booking_service.model.Booking;
import com.umar.booking_service.repository.BookingRepository;
import com.umar.booking_service.serviceinterface.IBookingService;
import com.umar.events.booking.*;
import com.umar.exceptions.common.exception.ApiException;
import com.umar.payload.enums.booking.BookingStatus;
import com.umar.payload.enums.booking.CancelledBy;
import com.umar.payload.enums.user.UserRole;
import com.umar.payload.request.booking.*;
import com.umar.payload.request.user.UserProfileResponse;
import com.umar.payload.request.user.UserValidateResponse;
import com.umar.payload.response.booking.*;
import com.umar.payload.response.salon.SalonResponseV1;
import com.umar.payload.response.services.ServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.Executor;


@RequiredArgsConstructor
@Service
public class BookingService implements IBookingService {

    private final BookingRepository bookingRepository;
    private final ServiceClient serviceClient;
    private final SalonClient salonClient;
    private final UserClient userClient;
    private final BookingMapper bookingMapper;
    private final BookingEventProducer eventProducer;
    private final Executor executor;


    @Override
    public BookingResponse createBooking(BookingRequest request) {
        SalonResponseV1 salonResponseV1 = salonClient.getSalonById(request.getSalonId());
        ServiceResponse serviceResponse = serviceClient.getServiceById(request.getServiceId());
        UserValidateResponse userValidateResponse = userClient.getUserValidation();
        UserProfileResponse profileResponse = null;
        if(request.getStaffId()!=null){
             profileResponse =userClient.viewUserProfile(request.getStaffId());
        }
        String slotStartTime = request.getSlotStartTime();
        LocalDateTime   appointmentTime;
        try{
             appointmentTime = LocalDateTime.parse(slotStartTime);
        }
        catch (Exception e){
            throw new ApiException(HttpStatus.EXPECTATION_FAILED,"INVALID_ISO_FORMAT","booking.inValidISOFormat");
        }
        LocalDateTime now = LocalDateTime.now();
        if(!appointmentTime.isAfter(now)){
            throw new ApiException(HttpStatus.EXPECTATION_FAILED,"INVALID_SLOT_TIME","booking.invalidSlotTime");
        }
        String currentWeekday = appointmentTime.getDayOfWeek().name().toLowerCase(Locale.ROOT);
        String openingClosingTime= salonResponseV1.getOpeningHours().get(currentWeekday);
        if(!isTimeSlotValid(appointmentTime,openingClosingTime)){
            throw new ApiException(HttpStatus.EXPECTATION_FAILED,"OUTSIDE_SALON_HOURS","booking.outsideSalonHours");
        }
        LocalDateTime slotEndTime = appointmentTime.plusMinutes(serviceResponse.getDurationMinutes());
        List<BookingStatus> activeStatuses = List.of(BookingStatus.CONFIRMED, BookingStatus.PENDING);
        Integer booingCount = bookingRepository.findBookingWithExistingSlotTime(appointmentTime,slotEndTime,request.getSalonId(),request.getServiceId(),activeStatuses);
        if(booingCount>0){
            throw new ApiException(HttpStatus.EXPECTATION_FAILED,"SLOT_CONFLICT","booking.salonConflict");
        }
        Booking booking = Booking.builder()
                .status(BookingStatus.PENDING)
                .customerNotes(request.getCustomerNotes())
                .salonId(request.getSalonId())
                .slotEndTime(slotEndTime)
                .totalServices(1L)
                .serviceId(request.getServiceId())
                .customerId(userValidateResponse.getUserId())
                .currency("INR")
                .staffId(profileResponse==null?null:profileResponse.getUserId())
                .slotStartTime(appointmentTime)
                .staffId(request.getStaffId())
                .totalPrice(serviceResponse.getCurrentPrice()
                        ).build();
        Booking persistBooking =bookingRepository.save(booking);
        BookingResponse response = bookingMapper.toResponse(persistBooking);
        executor.execute(()->{
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
        });
        return response;
    }

    @Override
    public BookingAvailabilityResponse getAvailableSlot(BookingAvailabilityRequest request) {
        SalonResponseV1 salonResponseV1 = salonClient.getSalonById(request.getSalonId());
        ServiceResponse serviceResponse = serviceClient.getServiceById(request.getServiceId());
        Integer durationMinutes = serviceResponse.getDurationMinutes();
        LocalDateTime   appointmentTime;
        try{
            appointmentTime = LocalDateTime.parse(request.getDate());
        }
        catch (Exception e){
            throw new ApiException(HttpStatus.EXPECTATION_FAILED,"INVALID_ISO_FORMAT","booking.inValidISOFormat");
        }
        UserProfileResponse profileResponse = null;
        if(request.getStaffId()!=null){
            profileResponse =userClient.viewUserProfile(request.getStaffId());
        }
        String currentWeekday = appointmentTime.getDayOfWeek().name().toLowerCase(Locale.ROOT);
        String businessHoursStr= salonResponseV1.getOpeningHours().get(currentWeekday);
        List<LocalDateTime> possibleSlots = new ArrayList<>(this.generateAllPossibleSlots(LocalDate.now(), businessHoursStr, durationMinutes).stream().filter(
                localDateTime -> {
                    return localDateTime.isAfter(LocalDateTime.now());
                }
        ).sorted().toList());
        if(possibleSlots.isEmpty()){
            throw new ApiException(HttpStatus.EXPECTATION_FAILED,"NO_SLOTS_AVAILABLE","booking.noSlotsAvailable");
        }
        List<Pair<LocalDateTime,LocalDateTime>> allBookedSlots = bookingRepository.fetchALlBookedSlots(possibleSlots.getFirst(),possibleSlots.getLast(),request.getSalonId(),
                request.getServiceId(),List.of(BookingStatus.PENDING,BookingStatus.COMPLETED),profileResponse==null?null:profileResponse.getUserId());
        if(!allBookedSlots.isEmpty()){
            possibleSlots.removeAll(allBookedSlots.stream().map(Pair::getFirst).toList());
        }
        return BookingAvailabilityResponse.builder()
                .salonId(request.getSalonId())
                .serviceId(request.getServiceId())
                .serviceDuration(serviceResponse.getDurationMinutes())
                .date(request.getDate())
                .availableSlots(possibleSlots.stream().map(LocalDateTime::toString).toList())
                .fullyBooked(Boolean.FALSE)
                .totalAvailable(possibleSlots.size())
                .build();
    }

    @Override
    public BookingResponseV1 getBookingById(Long id) {
        Booking booking = this.bookingRepository.findById(id).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"BOOKING_NOT_FOUND","booking.notFound"));
        UserValidateResponse userValidateResponse = userClient.getUserValidation();
        if(userValidateResponse.getRole().equals(UserRole.CUSTOMER.name()) && !Objects.equals(booking.getUserId(), userValidateResponse.getUserId())){
            throw new ApiException(HttpStatus.FORBIDDEN,"FORBIDDEN","booking.notAuthorized");
        }
        return bookingMapper.toResponseV1(booking);
    }

    @Override
    public UserBookingResponse getBookingByUser(Long userId, UserBookingParamRequest request) {
        UserValidateResponse userValidateResponse = userClient.getUserValidation();
        if(userValidateResponse.getRole().equals(UserRole.CUSTOMER.name()) && !Objects.equals(userId, userValidateResponse.getUserId())){
            throw new ApiException(HttpStatus.FORBIDDEN,"FORBIDDEN","booking.notAuthorized");
        }
        LocalDateTime startDate ;
        LocalDateTime endDate;
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        try{
            startDate = LocalDateTime.parse(request.getFrom());
            endDate = LocalDateTime.parse(request.getTo());
        }
        catch (Exception e){
            throw new ApiException(HttpStatus.EXPECTATION_FAILED,"INVALID_ISO_FORMAT","booking.inValidISOFormat");
        }
        List<Booking> bookings = this.bookingRepository.fetchAllBookingSByUserId(userId,pageable,startDate,endDate,BookingStatus.PENDING);
        if(bookings.isEmpty()){
            throw new ApiException(HttpStatus.NOT_FOUND,"NOT_FOUND","booking.notFound");
        }
        return UserBookingResponse.builder()
                .totalElements(Long.valueOf(bookings.size()))
                .summary(bookings.stream().map(bookingMapper::toUserBookingResponse).toList())
                .build();
    }

    @Override
    public UserBookingResponse getBookingBySalonId(Long id, SalonBookingParamRequest request) {
        List<Booking> booking = this.bookingRepository.getBookingBySalonId(id);
        if(booking.isEmpty()){
           throw new ApiException(HttpStatus.NOT_FOUND,"NOT_FOUND","booking.notFound");
        }
        return UserBookingResponse.builder()
                .totalElements(Long.valueOf(booking.size()))
                .summary(booking.stream().map(bookingMapper::toUserBookingResponse).toList())
                .build();
    }

    @Override
    public BookingResponse cancelBooking(String reason, Long id) {
        Booking booking = this.bookingRepository.findById(id).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"BOOKING_NOT_FOUND","booking.notFound"));
        UserValidateResponse userValidateResponse = userClient.getUserValidation();
        if(userValidateResponse.getRole().equals(UserRole.CUSTOMER.name()) && !booking.getUserId().equals(userValidateResponse.getUserId())){
            throw new ApiException(HttpStatus.FORBIDDEN,"FORBIDDEN","booking.notAuthorized");
        }
        if(!booking.getStatus().equals(BookingStatus.PENDING) && !booking.getStatus().equals(BookingStatus.CONFIRMED)){
            throw new ApiException(HttpStatus.BAD_REQUEST,"INVALID_BOOKING_STATUS","booking.invalidStatusForCancellation");
        }

        if(userValidateResponse.getRole().equals(UserRole.CUSTOMER.name()) && LocalDateTime.now().isAfter(booking.getSlotStartTime().minusHours(1))){
            booking.setCancellationFeeApplied(Boolean.TRUE);
            booking.setCancellationFee(booking.getTotalPrice().divide(new BigDecimal(2)));
        }
        if(userValidateResponse.getRole().equals(UserRole.CUSTOMER.name()) && LocalDateTime.now().isAfter(booking.getSlotStartTime())){
            booking.setCancellationFeeApplied(Boolean.TRUE);
            booking.setCancellationFee(booking.getTotalPrice());
        }
        booking.setCancelledBy(CancelledBy.CUSTOMER);
        booking.setCancellationReason(reason);
        booking.setStatus(BookingStatus.CANCELLED);
        Booking cancelledBooking = bookingRepository.save(booking);
        executor.execute(()->{
            CancelBookingEvent cancelBookingEvent = CancelBookingEvent.builder()
                    .cancellationFeeApplied(booking.getCancellationFeeApplied())
                    .cancelledBy(booking.getCancelledBy())
                    .userId(userValidateResponse.getUserId())
                    .bookingId(booking.getId())
                    .salonId(booking.getSalonId())
                    .serviceId(booking.getServiceId())
                    .slotStartTime(booking.getSlotStartTime())
                    .build();
            eventProducer.sendCancelBookingEvent(cancelBookingEvent);
        });
        return bookingMapper.toResponse(cancelledBooking);
    }

    @Override
    public BookingResponse rescheduleBooking(Long id, RescheduleBookingRequest request) {
        Booking booking = this.bookingRepository.findById(id).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"BOOKING_NOT_FOUND","booking.notFound"));
        UserValidateResponse userValidateResponse = userClient.getUserValidation();
        if(userValidateResponse.getRole().equals(UserRole.CUSTOMER.name()) && !userValidateResponse.getUserId().equals(booking.getCustomerId())){
            throw new ApiException(HttpStatus.FORBIDDEN,"FORBIDDEN","booking.notAuthorized");
        }
        if(!booking.getStatus().equals(BookingStatus.PENDING) && !booking.getStatus().equals(BookingStatus.CONFIRMED)){
            throw new ApiException(HttpStatus.BAD_REQUEST,"INVALID_BOOKING_STATUS","booking.invalidStatusForCancellation");
        }
        if(booking.getRescheduleCount()>3){
            throw new ApiException(HttpStatus.NOT_ACCEPTABLE,"RESCHEDULE_LIMIT_REACHED","booking.rescheduleLimitReached");
        }
        LocalDateTime newSlotStartTime;
        try{
            newSlotStartTime = LocalDateTime.parse(request.getNewSlotStartTime());
            if(newSlotStartTime.isBefore(LocalDateTime.now())){
                throw new ApiException(HttpStatus.BAD_REQUEST,"INVALID_SLOT_START_TIME","booking.invalidSlotStartTime");
            }
        }
        catch (Exception e){
            throw new ApiException(HttpStatus.EXPECTATION_FAILED,"INVALID_ISO_FORMAT","booking.inValidISOFormat");
        }
        SalonResponseV1 salonResponseV1 = salonClient.getSalonById(booking.getSalonId());
        String currentWeekday = newSlotStartTime.getDayOfWeek().name().toLowerCase(Locale.ROOT);
        String openingClosingTime= salonResponseV1.getOpeningHours().get(currentWeekday);
        if(!isTimeSlotValid(newSlotStartTime,openingClosingTime)){
            throw new ApiException(HttpStatus.EXPECTATION_FAILED,"OUTSIDE_SALON_HOURS","booking.outsideSalonHours");
        }
        ServiceResponse serviceResponse = serviceClient.getServiceById(booking.getServiceId());
        List<BookingStatus> activeStatuses = List.of(BookingStatus.CONFIRMED, BookingStatus.PENDING);
        Integer booingCount = bookingRepository.findBookingWithExistingSlotTime(newSlotStartTime,newSlotStartTime.plusHours( serviceResponse.getDurationMinutes()),salonResponseV1.getSalonId(),booking.getServiceId(),activeStatuses);
        if(booingCount>0){
            throw new ApiException(HttpStatus.EXPECTATION_FAILED,"SLOT_CONFLICT","booking.salonConflict");
        }
        booking.setRescheduledFrom(booking.getSlotStartTime());
        booking.setSlotStartTime(newSlotStartTime);
        booking.setSlotEndTime(newSlotStartTime.plusHours( serviceResponse.getDurationMinutes()));
        booking.setRescheduleCount(booking.getRescheduleCount()+1);
        Booking savedBooking  = bookingRepository.save(booking);

        executor.execute(()->{
            BookingRescheduledEvent event = BookingRescheduledEvent.
                    builder()
                    .rescheduleCount(savedBooking.getRescheduleCount())
                    .bookingId(savedBooking.getId())
                    .newSlotEndTime(newSlotStartTime)
                    .newSlotEndTime(savedBooking.getSlotEndTime())
                    .originalSlotStartTime(savedBooking.getRescheduledFrom())
                    .userId(savedBooking.getUserId())
                    .salonId(savedBooking.getSalonId())
                    .build();
            eventProducer.publishRescheduleBookingEvent(event);
        });
        return bookingMapper.toResponse(savedBooking);
    }

    @Override
    public BookingResponse completeBooking(Long id) {
        Booking booking = this.bookingRepository.findById(id).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"BOOKING_NOT_FOUND","booking.notFound"));
        UserValidateResponse userValidateResponse = userClient.getUserValidation();
        if(userValidateResponse.getRole().equals(UserRole.SALON_OWNER.name()) && !userValidateResponse.getUserId().equals(booking.getSalonId())){
            throw new ApiException(HttpStatus.FORBIDDEN,"UNAUTHORIZED","booking.unauthorized");
        }
        if(booking.getSlotEndTime().isAfter(LocalDateTime.now())){
            throw new ApiException(HttpStatus.EXPECTATION_FAILED,"NOT_YET_STARTED","booking.notYetStarted");
        }
        booking.setStatus(BookingStatus.COMPLETED);
        Booking savedBooking = bookingRepository.save(booking);
        executor.execute(()->{
            BookingCompletedEvent event = BookingCompletedEvent.builder()
                    .bookingId(savedBooking.getId())
                    .salonId(booking.getSalonId())
                    .serviceId(booking.getServiceId())
                    .userId(booking.getUserId())
                    .slotStartTime(booking.getSlotStartTime())
                    .totalPrice(booking.getTotalPrice())
                    .build();
            eventProducer.publishBookingCompletedEvent(event);
        });
        return bookingMapper.toResponse(savedBooking);
    }

    @Override
    public BookingResponse noShowBooking(Long id) {
        Booking booking = this.bookingRepository.findById(id).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"BOOKING_NOT_FOUND","booking.notFound"));
        UserValidateResponse userValidateResponse = userClient.getUserValidation();
        if(userValidateResponse.getRole().equals(UserRole.SALON_OWNER.name()) && !userValidateResponse.getUserId().equals(booking.getSalonId())){
            throw new ApiException(HttpStatus.FORBIDDEN,"UNAUTHORIZED","booking.unauthorized");
        }
        if(booking.getSlotEndTime().isBefore(LocalDateTime.now())){
            throw new ApiException(HttpStatus.EXPECTATION_FAILED,"NOT_YET_STARTED","booking.notYetStarted");
        }
        if(booking.getSlotEndTime().plusMinutes(15).isBefore(LocalDateTime.now())){
            throw new ApiException(HttpStatus.EXPECTATION_FAILED,"NOT_YET_STARTED","booking.notYetStarted");
        }
        if(!booking.getStatus().equals(BookingStatus.CONFIRMED)){
            throw new ApiException(HttpStatus.EXPECTATION_FAILED,"NOT_YET_STARTED","booking.notYetStarted");
        }
        booking.setStatus(BookingStatus.NO_SHOW);
        Booking savedBooking = bookingRepository.save(booking);
        executor.execute(()->{
            BookingNoShowEvent event = BookingNoShowEvent.builder()
                    .noShowAt(savedBooking.getUpdatedAt())
                    .salonId(savedBooking.getSalonId())
                    .serviceId(savedBooking.getServiceId())
                    .userId(savedBooking.getUserId())
                    .bookingId(savedBooking.getId())
                    .slotStartTime(savedBooking.getSlotStartTime())
                    .totalPrice(savedBooking.getTotalPrice())
                    .build();
            eventProducer.publishBookingNoShowEvent(event);
        });
        return bookingMapper.toResponse(savedBooking);
    }

    @Override
    public BookingSummaryResponse salonSummary(Long salonId) {
        UserValidateResponse userValidateResponse = userClient.getUserValidation();
        if(userValidateResponse.getRole().equals(UserRole.SALON_OWNER.name()) && !userValidateResponse.getUserId().equals(salonId)){
            throw new ApiException(HttpStatus.FORBIDDEN,"UNAUTHORIZED","booking.unauthorized");
        }
        List<Booking> bookings = bookingRepository.findAllBySalonId(salonId);
        BookingSummaryResponse response = new BookingSummaryResponse();
        response.setSalonId(salonId);
        response.setTotalBookings(bookings.size());
        return response;
    }

    @Override
    public UserBookingResponse getBookingByCategory(Long categoryId) {
        return null;
    }


    private boolean isTimeSlotValid(LocalDateTime appointmentDateTime,String businessHoursStr){
        if("CLOSED".equalsIgnoreCase(businessHoursStr)){
            throw new ApiException(HttpStatus.EXPECTATION_FAILED,"BUSINESS_CLOSED","booking.businessClosed");
        }
        LocalTime appointmentTime = appointmentDateTime.toLocalTime();
        String[] hours = businessHoursStr.split("-");
        LocalTime openingTime = LocalTime.parse(hours[0]); // Parses "09:00"
        LocalTime closingTime = LocalTime.parse(hours[1]);
        return !appointmentTime.isBefore(openingTime) && !appointmentTime.isAfter(closingTime);
    }

    @Override
    public List<LocalDateTime> generateAllPossibleSlots(LocalDate targetDate, String businessHoursStr, int durationMinutes) {
        List<LocalDateTime> possibleSlots = new ArrayList<>();
        if(businessHoursStr==null){
            return possibleSlots;
        }
        if("CLOSED".equalsIgnoreCase(businessHoursStr)){
            throw new ApiException(HttpStatus.EXPECTATION_FAILED,"BUSINESS_CLOSED","booking.businessClosed");
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
