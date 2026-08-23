package com.umar.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umar.events.ReviewEventProducer;
import com.umar.exceptions.common.exception.ApiException;
import com.umar.exchange.SalonClient;
import com.umar.exchange.ServiceClient;
import com.umar.exchange.UserClient;
import com.umar.model.Review;
import com.umar.payload.enums.review.SentimentLabel;
import com.umar.payload.enums.review.Tone;
import com.umar.payload.enums.user.UserRole;
import com.umar.payload.request.review.ReplayReviewRequest;
import com.umar.payload.request.review.ReviewSentimentalScoreEvent;
import com.umar.payload.request.review.ReviewSummaryResponse;
import com.umar.payload.request.review.SentimentalAnalysisResponse;
import com.umar.payload.request.user.UserProfileResponse;
import com.umar.payload.request.user.UserValidateResponse;
import com.umar.payload.response.review.ReviewReplyResponse;
import com.umar.payload.response.review.ThemeCount;
import com.umar.payload.response.salon.SalonResponse;
import com.umar.payload.response.salon.SalonResponseV1;
import com.umar.repository.ReviewRepository;
import com.umar.serviceInterface.IReviewAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewAiService implements IReviewAiService {



    private final ChatClient chatClient;
    private final ReviewRepository reviewRepository;
    @Value("classpath:prompts/review-sentiment-prompt.st")
    private final Resource sentimentResource;

    @Value("classpath:prompts/review-reply-prompt.st")
    private final Resource replayResource;
    private final Executor executor;
    private final ReviewEventProducer eventProducer;
    private final UserClient userClient;
    private final SalonClient salonClient;
    private final ServiceClient serviceClient;

    @Override
    public void sentimentAnalysis(Long reviewId) {
        Review review = this.reviewRepository.findById(reviewId).orElseThrow(()->new ApiException(HttpStatus.BAD_REQUEST,"REVIEW_NOT_FOUND","review.not.found"));
        if(review.getSentimentLabel()!=null){
            return;
        }
        SentimentalAnalysisResponse response = this.chatClient
                .prompt().user(promptUserSpec ->
                        promptUserSpec.text(sentimentResource).param(
                                "reviewTitle",review.getTitle()
                        ).param("reviewBody",review.getBody())
                                .param("starRating",review.getRating()))
                .call().entity(SentimentalAnalysisResponse.class);
        review.setSentimentLabel(response.getSentiment());
        review.setSentimentScore(response.getScore());
        review.setSentimentScoredAt(LocalDateTime.now());
        this.reviewRepository.save(review);
        executor.execute(()->{
            eventProducer.publishReviewSentimentScoreEvent(
                    ReviewSentimentalScoreEvent.builder()
                            .reviewId(reviewId)
                            .salonId(review.getSalonId())
                            .scoreAt(LocalDateTime.now())
                            .sentiment(response.getSentiment())
                            .sentimentScore(response.getScore())
                            .themes(response.getThemes())
                            .build()
            );
        });

    }

    @Override
    public ReviewReplyResponse replyGeneration(ReplayReviewRequest request, Long reviewId) {
        Review review = this.reviewRepository.findById(reviewId).orElseThrow(()->new ApiException(HttpStatus.BAD_REQUEST,"REVIEW_NOT_FOUND","review.not.found"));
        UserValidateResponse userResponse = this.userClient.getUserValidation();
        SalonResponseV1 salonResponseV1 = salonClient.getSalonByClient(review.getSalonId());
        if(!userResponse.getRole().equals(UserRole.ADMIN.name()) && !salonResponseV1.getOwnerId().equals(userResponse.getUserId())){
            throw new ApiException(HttpStatus.FORBIDDEN,"UNAUTHORIZED","unauthorized");
        }
        Tone tone;
        if(request.getTone()==null){
           tone =  switch (review.getSentimentLabel()){
                case POSITIVE ->  Tone.APPRECIATE;
                case NEUTRAL ->   Tone.PROFESSIONAL;
                case NEGATIVE ->  Tone.APOLOGETIC;
            };
        }
        else {
            tone = request.getTone();
        }
        UserProfileResponse profileResponse = this.userClient.viewUserProfile(review.getUserId());
        ReviewReplyResponse replyResponse = this.chatClient.prompt()
                .user(promptUserSpec -> {
                    promptUserSpec.text(replayResource)
                            .param("reviewerName",profileResponse.getFirstName())
                            .param("rating",review.getRating())
                            .param("reviewTitle",review.getTitle())
                            .param("reviewBody",review.getBody())
                            .param("sentimentLabel",review.getSentimentLabel())
                            .param("salonName",salonResponseV1.getName())
                            .param("serviceName",review.getServiceName())
                            .param("tone",tone)
                            .param("customContext",request.getCustomContext())
                            .param("includeDiscount",request.getIncludeDiscount());
                }).call().entity(ReviewReplyResponse.class);
        review.setAiDraft(replyResponse.getReply());
        this.reviewRepository.save(review);
        return replyResponse;
    }

    @Override
    public ReviewSummaryResponse getReviewSummary(Long reviewId, Boolean forceRefresh, Integer maxReviews) {
        if(maxReviews==null || maxReviews==0){
            maxReviews=10;
        }
        List<Review> reviewList = this.reviewRepository.findBySalonIdAndIsVisibleFalse(reviewId,maxReviews).orElseThrow(()->new ApiException(HttpStatus.BAD_REQUEST,"REVIEW_NOT_FOUND","review.not.found"));
        if(reviewList.isEmpty() || reviewList.size()<=3){
            throw new ApiException(HttpStatus.BAD_REQUEST,"REVIEW_NOT_FOUND","review.not.found");
        }
        ReviewSummaryResponse response = new ReviewSummaryResponse();
        response.setSentimentDistribution(calculateSentimentDistribution(reviewList));
        response.setTopPositiveThemes(getTopThemes(reviewList,SentimentLabel.POSITIVE));
        response.setTopNegativeThemes(getTopThemes(reviewList,SentimentLabel.NEGATIVE));
        String summaryResponse = this.chatClient.prompt().user(
                promptUserSpec ->
                        promptUserSpec
                                .param("reviewCount",reviewList.size())
                                .param("avgRating",response.getAvgRating())
                                .param("pos",response.getSentimentDistribution().get(SentimentLabel.POSITIVE.name()))
                                .param("neu",response.getSentimentDistribution().get(SentimentLabel.NEUTRAL.name()))
                                .param("neg",response.getSentimentDistribution().get(SentimentLabel.NEGATIVE.name()))
                                .param("positiveThemes",response.getTopPositiveThemes())
                                .param("negativeThemes",response.getTopNegativeThemes())

        ).call().entity(String.class);
        response.setSummaryText(summaryResponse);
        return response;
    }

    public Map<String,Double> calculateSentimentDistribution(List<Review> reviewList){
        long total = reviewList.size();
        Map<String,Long> counts = reviewList.stream().collect(Collectors.groupingBy(
                r->r.getSentimentLabel().name(),
                Collectors.counting()));
        Map<String,Double> result = new LinkedHashMap<>();
        for(SentimentLabel sentimentLabel :SentimentLabel.values()){
            long count = counts.getOrDefault(sentimentLabel.name(),0L);
            double percentage = total==0?0.0:(count*100.0)/total;
            result.put(sentimentLabel.name(),Math.round(percentage*100.0)/100.0);
        }
        return result;
    }

    private List<String> getTopThemes(List<Review> reviewList,SentimentLabel sentimentLabel){
        Map<String,Long> themeCount = new HashMap<>();
        return reviewList.stream().filter(
                r->r.getSentimentLabel().equals(sentimentLabel)
        ).sorted(Comparator.comparingLong(Review::getRating).reversed())
                .map(Review::getTitle).toList();
    }

    private List<String> parseThemes(String themes){
        ObjectMapper objectMapper = new ObjectMapper();
        if(themes==null || themes.isBlank()){
            return List.of();
        }
        try{
            return objectMapper.readValue(themes,new TypeReference<List<String>>(){});
        }catch (Exception e){
            throw new ApiException(HttpStatus.BAD_REQUEST,"INVALID_THEME_FORMAT","invalid.theme.format");
        }
    }


}
