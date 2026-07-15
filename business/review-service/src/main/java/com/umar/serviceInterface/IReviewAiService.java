package com.umar.serviceInterface;

public interface IReviewAiService {
    void sentimentAnalysis(String review);
    void replyGeneration(String review);
}
