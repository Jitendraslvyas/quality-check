package com.crossover.techtrial.controller;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.crossover.techtrial.model.Book;
import com.crossover.techtrial.util.TestUtil;

/**
 * BookControllerTest Test cases for BookController.
 * 
 * @author crossover
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BookControllerTest {

    @Mock
    private BookController bookController;

    @Autowired
    private TestRestTemplate template;

    @Before
    public void setUp() throws Exception {
        MockMvcBuilders.standaloneSetup(bookController).build();
    }

    /**
     * Test case for Book related APIs.
     */
    @Test
    public void cBookShouldSaveAndGet() throws Exception {

        // Arrange
        Map<String, String> params = new HashMap<>();
        HttpEntity<Object> book1 = TestUtil.getHttpEntity("{\"title\": \"title1000000000000title1\"}");
        HttpEntity<Object> book2 = TestUtil.getHttpEntity("{\"title\": \"\"}");

        // Act
        // POST
        ResponseEntity<Book> resultBookShouldSaveReturnOkStatus = template.postForEntity("/api/book", book1, Book.class);
        ResponseEntity<Book> resultBookShouldNotSaveReturnConflictStatus = template.postForEntity("/api/book", book1, Book.class);
        ResponseEntity<Book> resultBookShouldNotSaveReturnBadRequestStatus = template.postForEntity("/api/book", book2, Book.class);
        Long bookId = resultBookShouldSaveReturnOkStatus.getBody().getId();
        params.put("book-id", String.valueOf(bookId));

        // GET
        ResponseEntity<Book[]> resultAllBooksShouldBeGetReturnOkStatus = template.getForEntity("/api/book", Book[].class);
        ResponseEntity<Book> resultBookShouldBeGetByBookIdReturnOkStatus = template.getForEntity("/api/book/{book-id}", Book.class, params);
        ResponseEntity<Book> resultBookShouldNotBeGetByBookIdReturnNotFoundStatus = template.getForEntity("/api/book/000000000000", Book.class);

        // Assert
        Assert.assertEquals(resultBookShouldSaveReturnOkStatus.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(resultBookShouldNotSaveReturnConflictStatus.getStatusCode(), HttpStatus.CONFLICT);
        Assert.assertEquals(resultBookShouldNotSaveReturnBadRequestStatus.getStatusCode(), HttpStatus.BAD_REQUEST);
        Assert.assertEquals(resultAllBooksShouldBeGetReturnOkStatus.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(resultBookShouldBeGetByBookIdReturnOkStatus.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(resultBookShouldNotBeGetByBookIdReturnNotFoundStatus.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    /**
     * Test case for Book entity.
     */
    @Test
    public void dBookhashCodeAndEqualsAndtoString() {

        // Arrange
        Book book = new Book();

        // Act
        book.setId(1L);
        String book1Str = book.toString();

        // Assert
        Assert.assertEquals(book.toString(), book1Str);
    }
}
