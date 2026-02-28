package com.example.bookreview.service;

import com.example.bookreview.DTO.BookReviewUpdateDTO;
import com.example.bookreview.exception.ResourceNotFoundException;
import com.example.bookreview.model.BookReview;
import com.example.bookreview.repository.BookRepository;
import com.example.bookreview.repository.BookReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.beans.PropertyDescriptor;
import java.time.LocalDateTime;
import java.util.Arrays;
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
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        copyNonNullProperties(dto, existing);

        return reviewRepository.save(existing);
    }

    private void copyNonNullProperties(Object src, Object target) {
        String[] nullProps = Arrays.stream(BeanUtils.getPropertyDescriptors(src.getClass()))
                .map(PropertyDescriptor::getName)
                .filter(name -> {
                    try {
                        Object value = new PropertyDescriptor(name, src.getClass())
                                .getReadMethod().invoke(src);
                        return value == null;
                    } catch (Exception e) {
                        return false;
                    }
                })
                .toArray(String[]::new);

        BeanUtils.copyProperties(src, target, nullProps);
    }
}