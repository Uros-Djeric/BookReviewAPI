package com.example.bookreview.repository;

import com.example.bookreview.model.BookReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookReviewRepository extends JpaRepository<BookReview, Long> {
    List<BookReview> findByBookId(Long bookId);
}