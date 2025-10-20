package com.example.bookreview.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(hidden = true)
    private Long id;

    @NotNull(message = "bookId cannot be null")
    private Long bookId;

    private String reviewerName;

    @NotNull(message = "rating cannot be null")
    @Min(value = 1, message = "rating must be at least 1")
    @Max(value = 5, message = "rating cannot be more than 5")
    private Integer rating; // 1-5

    private String comment;

    @Schema(hidden = true)
    private LocalDateTime createdAt;
}