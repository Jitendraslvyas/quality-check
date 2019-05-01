package com.crossover.techtrial.service;

import java.util.List;

import com.crossover.techtrial.model.Book;

/**
 * BookService interface for Book.
 * 
 * @author crossover
 */
public interface BookService {

    public List <Book> getAll();

    public Book save(Book p);

    public Book findById(Long bookId);

    public Book findByTitle(String bookTitle);
}
