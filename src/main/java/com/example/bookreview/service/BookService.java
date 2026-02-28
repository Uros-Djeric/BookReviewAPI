package com.example.bookreview.service;

import com.example.bookreview.DTO.BookSearchParamsDTO;
import com.example.bookreview.exception.ResourceNotFoundException;
import com.example.bookreview.model.Book;
import com.example.bookreview.model.BookReview;
import com.example.bookreview.repository.BookRepository;
import com.example.bookreview.repository.BookReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final BookReviewRepository reviewRepository;


    public Book addBook(Book book) {
        if (book.getTitle() == null || book.getTitle().isBlank()) {
            book.setTitle("Unknown");
        }
        return bookRepository.save(book);
    }
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public void deleteBookById(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book with provided id not found");
        }
        bookRepository.deleteById(id);
    }

    public List<Book> searchBooks(BookSearchParamsDTO params) {
        // Ako nije prosleđen sortField, podrazumevano sortiraj po ID
        String sortField = (params.getSortField() != null) ? params.getSortField() : "id";

        // Ako nije prosleđen sortDir, koristi rastuće
        Sort.Direction direction = "desc".equalsIgnoreCase(params.getSortDir())
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Sort sort = Sort.by(direction, sortField);
        Pageable pageable = Pageable.unpaged(sort);

        Page<Book> page = bookRepository.search(params.getTitle(), params.getAuthor(), pageable);

        return page.getContent();
    }

    public Map<String, Object> getBookStats() {
        long totalBooks = bookRepository.count();

        Double avgRating = reviewRepository.findAll().stream()
                .mapToInt(BookReview::getRating)
                .average()
                .orElse(0.0);

        return Map.of(
                "totalBooks", totalBooks,
                "averageRating", avgRating
        );
    }
}

