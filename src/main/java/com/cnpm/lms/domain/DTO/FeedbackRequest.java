package com.cnpm.lms.domain.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class FeedbackRequest {
    public String content;

    @Min(value = 1, message = "Rating must be between 1 and 5")
    @Max(value = 5, message = "Rating must be between 1 and 5")
    public Integer rating;
}
