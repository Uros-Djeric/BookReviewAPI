package com.example.bookreview.DTO;

import lombok.Data;

@Data
public class BookReviewUpdateDTO {
    private String reviewerName;
    private Integer rating;
    private String comment;
}