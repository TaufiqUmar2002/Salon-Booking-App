package com.umar.controller;

import com.umar.service.AiRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/ai-recommendation")
public class AiRecommendationController {

    private final AiRecommendationService aiRecommendationService;


}
