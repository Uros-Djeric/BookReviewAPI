package com.example.bookreview

import com.example.bookreview.model.Book
import com.example.bookreview.repository.BookRepository
import com.example.bookreview.service.BookService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@SpringBootTest
@ContextConfiguration(classes = BookReviewApplication)
class BookServiceTestExample extends Specification {

    @Autowired
    BookService bookService

    @Autowired
    BookRepository bookRepository

    def "should add and retrieve a book"() {
        given: "a new book"
        def book = new Book(title: "Groovy in Action", author: "Dierk König")

        when: "the book is saved"
        def saved = bookService.addBook(book)

        then: "it can be retrieved from repository"
        def found = bookRepository.findById(saved.id).get()
        found.title == "Groovy in Action"
        found.author == "Dierk König"
    }
}