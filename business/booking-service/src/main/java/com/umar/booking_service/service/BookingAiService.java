package com.umar.booking_service.service;

import com.umar.booking_service.exchange.SalonClient;
import com.umar.booking_service.exchange.ServiceClient;
import com.umar.booking_service.exchange.UserClient;
import com.umar.booking_service.model.Booking;
import com.umar.booking_service.repository.BookingRepository;
import com.umar.booking_service.serviceinterface.IBookingAiService;
import com.umar.booking_service.serviceinterface.IBookingService;
import com.umar.exceptions.common.exception.ApiException;
import com.umar.payload.enums.booking.BookingStatus;
import com.umar.payload.request.booking.ai.AiBookingSuggestAttribute;
import com.umar.payload.request.booking.ai.BookingAiChatRequest;
import com.umar.payload.request.user.UserValidateResponse;
import com.umar.payload.response.booking.ai.BookingAiChatResponse;
import com.umar.payload.response.booking.ai.BookingAiSuggestResponse;
import com.umar.payload.response.salon.SalonResponseV1;
import com.umar.payload.response.services.ServiceResponse;
import com.umar.payload.response.user.ai.UserPreferenceProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class BookingAiService implements IBookingAiService {

    private final SalonClient salonClient;
    private final ServiceClient serviceClient;
    private final UserClient userClient;
    private final IBookingService bookingService;
    private final BookingRepository bookingRepository;
    private final ChatClient chatClient;

    @Value("classpath:slot-suggest-client.st")
    private final Resource slotSuggestClient;

    @Override
    public BookingAiSuggestResponse suggestSlot(Long userId, AiBookingSuggestAttribute attribute ) {
        SalonResponseV1 salonResponseV1 = salonClient.getSalonById(attribute.getSalonId());
        ServiceResponse serviceResponse = serviceClient.getServiceById(attribute.getServiceId());
        UserValidateResponse userValidateResponse = userClient.getUserValidation();
        List<LocalDateTime> possibleSlots = new ArrayList<>();
        Integer durationMinutes = serviceResponse.getDurationMinutes();
        for(LocalDateTime currentDate = attribute.getFromDate(); currentDate.isBefore(attribute.getToDate()); currentDate = currentDate.plusDays(1)){
            String currentWeekday = currentDate.getDayOfWeek().name().toLowerCase(Locale.ROOT);
            String businessHourStr = salonResponseV1.getOpeningHours().get(currentWeekday);
            possibleSlots.addAll(this.bookingService.generateAllPossibleSlots(currentDate.toLocalDate(), businessHourStr, durationMinutes).stream().filter(
                    localDateTime -> {return localDateTime.isAfter(LocalDateTime.now());}
            ).sorted().toList());
            List<Pair<LocalDateTime,LocalDateTime>> allBookedSlots = bookingRepository.fetchALlBookedSlots(possibleSlots.getFirst(),possibleSlots.getLast(),attribute.getSalonId(),
                    attribute.getServiceId(),List.of(BookingStatus.PENDING,BookingStatus.COMPLETED),null);
            if(!allBookedSlots.isEmpty()){
                possibleSlots.removeAll(allBookedSlots.stream().map(Pair::getFirst).toList());
            }
        }
        if(possibleSlots.isEmpty()){
            throw new ApiException(HttpStatus.BAD_REQUEST,"","");
        }
        UserPreferenceProfileResponse profileResponse = this.userClient.getUserPreference(userValidateResponse.getUserId());
        List<Booking> bookingList = this.bookingRepository.findBookingByUserId(userId,BookingStatus.COMPLETED);
        if(bookingList.isEmpty()){
            throw new ApiException(HttpStatus.BAD_REQUEST,"","");
        }
        return this.chatClient.prompt()
                .user(promptUserSpec ->
                        promptUserSpec.text(slotSuggestClient)
                                .param("availableSlots",possibleSlots)
                                .param("preferredDay",profileResponse.getPreferredDayOfWeek())
                                .param("preferredTime",profileResponse.getPreferredTimeOfDay())
                                .param("frequency",2)).call()
                .entity(BookingAiSuggestResponse.class);
    }

    @Override
    public BookingAiChatResponse chat(BookingAiChatRequest request) {
        return null;
    }
}
