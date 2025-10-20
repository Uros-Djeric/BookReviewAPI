package com.example.bookreview.service;

import com.example.bookreview.DTO.BookReviewUpdateDTO;
import com.example.bookreview.exception.ResourceNotFoundException;
import com.example.bookreview.model.BookReview;
import com.example.bookreview.repository.BookRepository;
import com.example.bookreview.repository.BookReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookReviewService {

    private final BookReviewRepository reviewRepository;
    private final BookRepository bookRepository;

    public BookReview addReview(BookReview review) {
        if (!bookRepository.existsById(review.getBookId())) {
            throw new IllegalArgumentException("Book with id " + review.getBookId() + " does not exist");
        }
        if (review.getCreatedAt() == null) {
            review.setCreatedAt(LocalDateTime.now());
        }
        return reviewRepository.save(review);
    }

    public List<BookReview> getReviewsByBookId(Long bookId) {
        return reviewRepository.findByBookId(bookId);
    }

    public void deleteReviewById(Long id) {
        if (!reviewRepository.existsById(id)) {
            throw new ResourceNotFoundException("Review with provided id not found");
        }
        reviewRepository.deleteById(id);
    }

    public BookReview updateReview(Long id, BookReviewUpdateDTO dto) {
        BookReview existing = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));

        if (dto.getReviewerName() != null) {
            existing.setReviewerName(dto.getReviewerName());
        }
        if (dto.getRating() != null) {
            existing.setRating(dto.getRating());
        }
        if (dto.getComment() != null) {
            existing.setComment(dto.getComment());
        }

        return reviewRepository.save(existing);
    }

}