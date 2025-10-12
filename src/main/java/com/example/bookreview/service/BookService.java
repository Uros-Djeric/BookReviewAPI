package com.example.bookreview.service;

import com.example.bookreview.exception.ResourceNotFoundException;
import com.example.bookreview.model.Book;
import com.example.bookreview.model.BookReview;
import com.example.bookreview.repository.BookRepository;
import com.example.bookreview.repository.BookReviewRepository;
import lombok.RequiredArgsConstructor;
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

    public List<Book> searchBooks(String title, String author) {
        return bookRepository.findAll().stream()
                .filter(b -> title == null || b.getTitle().toLowerCase().contains(title.toLowerCase()))
                .filter(b -> author == null || b.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .collect(Collectors.toList());
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

