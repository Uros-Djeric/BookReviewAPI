package com.example.bookreview.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jdk.jfr.Description;
import lombok.Data;

@Data
public class BookReviewUpdateDTO {

    private String reviewerName;

    @Description("Rating that should be between 1 and 5")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;

    @Description("Optional comment")
    private String comment;
}