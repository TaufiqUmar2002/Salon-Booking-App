package com.umar.booking_service.service;

import com.umar.booking_service.serviceinterface.IBookingAiService;
import com.umar.payload.request.booking.ai.AiBookingSuggestAttribute;
import com.umar.payload.request.booking.ai.BookingAiChatRequest;
import com.umar.payload.response.booking.ai.BookingAiChatResponse;
import com.umar.payload.response.booking.ai.BookingAiSuggestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingAiService implements IBookingAiService {

    @Override
    public BookingAiSuggestResponse suggestSlot(Long userId, AiBookingSuggestAttribute attribute ) {
        return null;
    }

    @Override
    public BookingAiChatResponse chat(BookingAiChatRequest request) {
        return null;
    }
}
