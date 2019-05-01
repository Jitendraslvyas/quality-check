package com.crossover.techtrial.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.crossover.techtrial.model.Book;
import com.crossover.techtrial.service.BookService;

/**
 * BookController for Book related APIs.
 * 
 * @author crossover
 */
@RestController
public class BookController {
    private static final Logger LOG = LoggerFactory.getLogger(BookController.class);

    @Autowired
    private BookService bookService;

    /**
     * This method is used to get all saved book details.
     * 
     * @return List<Book>> List of saved book details, Also can be empty if no book
     *         saved yet.
     */
    @GetMapping(path = "/api/book")
    public ResponseEntity<List<Book>> getBooks() {
        return ResponseEntity.ok(bookService.getAll());
    }

    /**
     * This method is used to save a book.
     * 
     * @param book It is a Book type, Required book details to save it.
     * @return Book Details if pass all validation & save successfully.
     */
    @PostMapping(path = "/api/book")
    public ResponseEntity<Book> saveBook(@RequestBody Book book) {

        if (bookService.findByTitle(book.getTitle()) == null) {
            LOG.info("Provided Book details: {} save successfully", book);
            return ResponseEntity.ok(bookService.save(book));
        }

        LOG.info("Provided book-title: {} already available", book.getTitle());
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    /**
     * This method is used to get a saved book details by Id.
     * 
     * @param bookId A Book Id, by which book saved.
     * @return Book saved book details, Also can be empty if no saved book details
     *         found for provided bookId.
     */
    @GetMapping(path = "/api/book/{book-id}")
    public ResponseEntity<Book> getRideById(@PathVariable(name = "book-id", required = true) Long bookId) {

    	Book book = bookService.findById(bookId);
        if (book != null) {
            return ResponseEntity.ok(book);
        }

        LOG.info("Provided book-id: {} not found", bookId);
        return ResponseEntity.notFound().build();
    }
}
