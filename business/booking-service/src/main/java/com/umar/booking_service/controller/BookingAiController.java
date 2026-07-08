package com.umar.booking_service.controller;

import com.umar.booking_service.serviceinterface.IBookingAiService;
import com.umar.payload.request.booking.ai.AiBookingSuggestAttribute;
import com.umar.payload.request.booking.ai.BookingAiChatRequest;
import com.umar.payload.response.booking.ai.BookingAiChatResponse;
import com.umar.payload.response.booking.ai.BookingAiSuggestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/booking/ai")
@RequiredArgsConstructor
public class BookingAiController {

    private final IBookingAiService bookingAiService;

    @GetMapping("/suggest-slot/{userId}")
    public ResponseEntity<BookingAiSuggestResponse> suggestSlot(@PathVariable Long userId, @ModelAttribute AiBookingSuggestAttribute attribute){
        BookingAiSuggestResponse response = bookingAiService.suggestSlot(userId,attribute);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/chat")
    public ResponseEntity<BookingAiChatResponse> chat(@RequestBody BookingAiChatRequest request ){
        BookingAiChatResponse response = bookingAiService.chat(request);
        return ResponseEntity.ok(response);
    }


}
