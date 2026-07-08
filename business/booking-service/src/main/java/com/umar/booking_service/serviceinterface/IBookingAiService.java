package com.umar.booking_service.serviceinterface;

import com.umar.payload.request.booking.ai.AiBookingSuggestAttribute;
import com.umar.payload.request.booking.ai.BookingAiChatRequest;
import com.umar.payload.response.booking.ai.BookingAiChatResponse;
import com.umar.payload.response.booking.ai.BookingAiSuggestResponse;

public interface IBookingAiService {

    BookingAiChatResponse chat(BookingAiChatRequest request);
    BookingAiSuggestResponse suggestSlot(Long userId, AiBookingSuggestAttribute attribute);
}
