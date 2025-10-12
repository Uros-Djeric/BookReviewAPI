package com.example.bookreview.controller;

import com.example.bookreview.DTO.BookSearchParams;
import com.example.bookreview.exception.ResourceNotFoundException;
import com.example.bookreview.model.Book;
import com.example.bookreview.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Validated
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<Book> addBook(@Valid @RequestBody Book book) {
        Book saved = bookService.addBook(book);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<?> getBooks(@RequestParam(required = false) Long id) {
        if (id != null) {
            return bookService.getBookById(id)
                    .map(book -> ResponseEntity.ok(Map.of("data", book)))
                    .orElseThrow(() -> new ResourceNotFoundException("Book with provided id not found"));
        }
        return ResponseEntity.ok(Map.of("data", bookService.getAllBooks()));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteBook(@RequestParam Long id) {
        bookService.deleteBookById(id);
        return ResponseEntity.ok(Map.of("message", "Book deleted successfully."));
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchBooks(@Valid @ModelAttribute BookSearchParams params) {
        List<Book> results = bookService.searchBooks(params.getTitle(), params.getAuthor());
        return ResponseEntity.ok(results);
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getStats() {
        return ResponseEntity.ok(bookService.getBookStats());
    }
}