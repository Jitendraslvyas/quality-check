package com.crossover.techtrial.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crossover.techtrial.model.Book;
import com.crossover.techtrial.repositories.BookRepository;

/**
 * BookServiceImpl is implementation of BookService interface.
 * 
 * @author crossover
 */
@Service
public class BookServiceImpl implements BookService {

	@Autowired
	private BookRepository bookRepository;

	@Override
	public List<Book> getAll() {
		return bookRepository.findAll();
	}

	@Override
	public Book save(Book p) {
		return bookRepository.save(p);
	}

	@Override
	public Book findById(Long bookId) {
		Optional<Book> optionalBook = bookRepository.findById(bookId);
		if (optionalBook.isPresent()) {
			return optionalBook.get();
		}
		return null;
	}

	@Override
	public Book findByTitle(String bookTitle) {
		Optional<Book> optionalBook = bookRepository.findByTitle(bookTitle);
		if (optionalBook.isPresent()) {
			return optionalBook.get();
		}
		return null;
	}
}
