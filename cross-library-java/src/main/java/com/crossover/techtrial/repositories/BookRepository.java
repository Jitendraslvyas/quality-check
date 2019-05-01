package com.crossover.techtrial.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.crossover.techtrial.model.Book;

/**
 * Book repository for basic operations on Book entity.
 * 
 * @author crossover
 */
@RepositoryRestResource(exported = false)
public interface BookRepository extends CrudRepository<Book, Long> {

    List <Book> findAll();

    Optional <Book> findById(Long bookId);

    Optional <Book> findByTitle(String bookTitle);
}
