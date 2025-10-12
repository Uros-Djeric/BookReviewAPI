package com.example.bookreview.controller;

import com.example.bookreview.DTO.BookReviewUpdateDTO;
import com.example.bookreview.exception.ResourceNotFoundException;
import com.example.bookreview.model.BookReview;
import com.example.bookreview.service.BookReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Validated
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class BookReviewController {

    private final BookReviewService reviewService;

    @PostMapping
    public ResponseEntity<?> addReview(@Valid @RequestBody BookReview request) {
        if (request.getBookId() == null) {
            throw new IllegalArgumentException("Bad request! Field 'bookId' is missing");
        }
        BookReview review = reviewService.addReview(request);
        return ResponseEntity.ok(review);
    }

    @GetMapping
    public ResponseEntity<?> getReviews(@RequestParam(required = true) Long bookId) {
        List<BookReview> reviews = reviewService.getReviewsByBookId(bookId);
        if (reviews.isEmpty()) {
            throw new ResourceNotFoundException("No reviews found for this book.");
        }
        return ResponseEntity.ok(reviews);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteReview(@RequestParam Long id) {
        try {
            reviewService.deleteReviewById(id);
            return ResponseEntity.ok("Review deleted successfully.");
        } catch (ResourceNotFoundException e) {

            String errorMessage = "Error code: 404\n" +
                    "Error message: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(errorMessage);
        }
    }//Primer dobre prakse, konzistentno sa ostatkom koda
    //    @DeleteMapping
//    public ResponseEntity<?> deleteReview(@RequestParam Long id) {
//        reviewService.deleteReviewById(id);
//        return ResponseEntity.ok(Map.of("message", "Review deleted successfully."));
//    }


    @PutMapping("/update")
    public ResponseEntity<?> updateReview(@RequestParam(required = true) Long id,
                                          @Valid @RequestBody BookReviewUpdateDTO reviewDTO) {
        BookReview updated = reviewService.updateReview(id, reviewDTO);
        if (updated == null) {
            throw new ResourceNotFoundException("Review not found");
        }
        return ResponseEntity.ok(updated);
    }
}