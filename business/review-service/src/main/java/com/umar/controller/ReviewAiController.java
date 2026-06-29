package com.umar.controller;

import com.umar.serviceInterface.IReviewAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/review/ai")
@RequiredArgsConstructor
public class ReviewAiController {

    private final IReviewAiService reviewAiService;
}
