package com.example.bookreview.controller;

import com.example.bookreview.DTO.BookSearchParams;
import com.example.bookreview.exception.ResourceNotFoundException;
import com.example.bookreview.model.Book;
import com.example.bookreview.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
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

    @Operation(
            summary = "Creation of a new book",
            description = "Creates a new book when providing an author and title"
    )
    @PostMapping
    public ResponseEntity<Book> addBook(@Valid @RequestBody Book book) {
        Book saved = bookService.addBook(book);
        return ResponseEntity.ok(saved);
    }

    @Operation(
            summary = "Gets all books or a single book by id",
            description = "Gets a single book if an id is provided, if not it gets all books in DB"
    )
    @GetMapping
    public ResponseEntity<?> getBooks(@RequestParam(required = false) Long id) {
        if (id != null) {
            return bookService.getBookById(id)
                    .map(book -> ResponseEntity.ok(Map.of("data", book)))
                    .orElseThrow(() -> new ResourceNotFoundException("Book with provided id not found"));
        }
        return ResponseEntity.ok(Map.of("data", bookService.getAllBooks()));
    }

    @Operation(
            summary = "Delete a book by it's id"
    )
    @DeleteMapping
    public ResponseEntity<?> deleteBook(@RequestParam Long id) {
        bookService.deleteBookById(id);
        return ResponseEntity.ok(Map.of("message", "Book deleted successfully."));
    }

    @Operation(
            summary = "Search a book(s) by parameters",
            description = "Search all books by specific field, all fields are optional"
    )
    @GetMapping("/search")
    public ResponseEntity<?> searchBooks(@Valid @ParameterObject BookSearchParams params) {
        List<Book> results = bookService.searchBooks(params.getTitle(), params.getAuthor());
        return ResponseEntity.ok(results);
    }


    @Operation(
            summary = "Get all book statistics",
            description = "Looks at all books and gives average stats for reviews on books")
    @GetMapping("/stats")
    public ResponseEntity<?> getStats() {
        return ResponseEntity.ok(bookService.getBookStats());
    }
}