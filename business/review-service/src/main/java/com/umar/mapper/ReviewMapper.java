package com.umar.mapper;

import com.umar.model.Review;
import com.umar.payload.response.review.ReviewResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(source = "id",target = "reviewId")
    ReviewResponse toResponse(Review review);
}
