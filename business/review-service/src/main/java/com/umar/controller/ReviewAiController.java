package com.umar.controller;

import com.umar.serviceInterface.IReviewAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/review/ai")
@RequiredArgsConstructor
public class ReviewAiController {

    private final IReviewAiService reviewAiService;

    public ResponseEntity<Void> sentimentAnalysis(@RequestBody String review) {
        reviewAiService.sentimentAnalysis(review);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> replyGeneration(@RequestBody String review) {
        reviewAiService.replyGeneration(review);
        return ResponseEntity.ok().build();
    }
}
