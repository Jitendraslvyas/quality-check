package com.crossover.techtrial.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.crossover.techtrial.dto.TopMemberDTO;
import com.crossover.techtrial.model.Transaction;
import com.crossover.techtrial.repositories.BookRepository;
import com.crossover.techtrial.repositories.MemberRepository;
import com.crossover.techtrial.repositories.TransactionRepository;
import com.crossover.techtrial.util.TestUtil;

/**
 * TransactionControllerTest Test cases for TransactionController.
 * 
 * @author crossover
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TransactionControllerTest {

    private MockMvc mockMvc;
    private static Long memberId;
    private static Long bookId;
    private static Long transactionId;

    @Autowired
    private TestRestTemplate template;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private WebApplicationContext wac;

    @Before
    public void setUp() throws Exception {
        DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
        this.mockMvc = builder.build();

        memberId = memberRepository.findByEmail("test1000000000000test1@gmail.com").get().getId();
        bookId = bookRepository.findByTitle("title1000000000000title1").get().getId();
    }

    /**
     * Test case for Transaction related APIs
     */
    @Test
    public void eTransactionShouldSaveAndUpdate() throws Exception {

        // Arrange
        HttpEntity<Object> transaction1 = TestUtil.getHttpEntity("{\"bookId\": " + bookId + ", \"memberId\": " + memberId + "}");
        HttpEntity<Object> transaction2 = TestUtil.getHttpEntity("{\"bookId\": " + bookId + ", \"memberId\": 110011001100}");
        HttpEntity<Object> transaction3 = TestUtil.getHttpEntity("{\"bookId\": 110011001100, \"memberId\": " + memberId + "}");

        // Act
        // POST
        ResponseEntity<Transaction> resultTransactionShouldSaveReturnOkStatus = template.postForEntity("/api/transaction", transaction1, Transaction.class);
        ResponseEntity<Transaction> resultTransactionShouldNotSaveWhenBookAlreadyIssuedtoSomeMemberReturnForbiddenStatus = template.postForEntity("/api/transaction", transaction1, Transaction.class);
        ResponseEntity<Transaction> resultTransactionShouldNotSaveWhenMemberIdNotAvailableReturnNotFoundStatus = template.postForEntity("/api/transaction", transaction2, Transaction.class);
        ResponseEntity<Transaction> resultTransactionShouldNotSaveWhenBookIdNotAvailableReturnNotFoundStatus = template.postForEntity("/api/transaction", transaction3, Transaction.class);
        transactionId = resultTransactionShouldSaveReturnOkStatus.getBody().getId();

        // PATCH
        MockHttpServletResponse resultTransactionShouldUpdateReturnOkStatus = mockMvc.perform(MockMvcRequestBuilders.patch("/api/transaction/" + String.valueOf(transactionId) + "/return")).andReturn().getResponse();
        MockHttpServletResponse resultTransactionShouldNotUpdateReturnForbiddenStatus = mockMvc.perform(MockMvcRequestBuilders.patch("/api/transaction/" + String.valueOf(transactionId) + "/return")).andReturn().getResponse();
        MockHttpServletResponse resultTransactionShouldNotUpdateReturnNotFoundStatus = mockMvc.perform(MockMvcRequestBuilders.patch("/api/transaction/000000000000/return")).andReturn().getResponse();

        // Assert
        Assert.assertEquals(resultTransactionShouldSaveReturnOkStatus.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(resultTransactionShouldNotSaveWhenBookAlreadyIssuedtoSomeMemberReturnForbiddenStatus.getStatusCode(), HttpStatus.FORBIDDEN);
        Assert.assertEquals(resultTransactionShouldNotSaveWhenMemberIdNotAvailableReturnNotFoundStatus.getStatusCode(), HttpStatus.NOT_FOUND);
        Assert.assertEquals(resultTransactionShouldNotSaveWhenBookIdNotAvailableReturnNotFoundStatus.getStatusCode(), HttpStatus.NOT_FOUND);
        Assert.assertEquals(resultTransactionShouldUpdateReturnOkStatus.getStatus(), HttpStatus.OK.value());
        Assert.assertEquals(resultTransactionShouldNotUpdateReturnForbiddenStatus.getStatus(), HttpStatus.FORBIDDEN.value());
        Assert.assertEquals(resultTransactionShouldNotUpdateReturnNotFoundStatus.getStatus(), HttpStatus.NOT_FOUND.value());
    }

    /**
     * Test case for Transaction entity.
     */
    @Test
    public void fTransactiontoString() {

        // Arrange
        Transaction transaction = new Transaction();

        // Act
        transaction.setId(1L);
        String transactionStr = transaction.toString();

        // Assert
        Assert.assertEquals(transaction.toString(), transactionStr);
    }

    /**
     * Test case for Top Members related APIs.
     */
    @Test
    public void gTopMembersShouldNotBeGet() throws Exception {

        // Act
        // GET
        ResponseEntity<TopMemberDTO[]> resultTopMembersShouldNotBeGetReturnNotFoundStatus = template.getForEntity(
            "/api/member/top-member?startTime=2018-08-08T12:12:57&endTime=2018-08-08T12:12:58",
            TopMemberDTO[].class);
        ResponseEntity<TopMemberDTO[]> resultTopMembersShouldNotBeGetBadRequestStatus = template.getForEntity(
            "/api/member/top-member?startTime=2018-08-08T12:12:50&endTime=2018-08-08T12:12:49",
            TopMemberDTO[].class);

        // Assert
        Assert.assertEquals(resultTopMembersShouldNotBeGetReturnNotFoundStatus.getStatusCode(), HttpStatus.NOT_FOUND);
        Assert.assertEquals(resultTopMembersShouldNotBeGetBadRequestStatus.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Test case for TopMemberDTO dto.
     */
    @Test
    public void hTopMemberDTOtoString() throws Exception {

        // Arrange
        TopMemberDTO topMemberDTO = new TopMemberDTO(1L, "test1", "test1@gmail.com", 5);

        // Act
        cleanUp();
        topMemberDTO.setMemberId(2L);
        String topMemberDTOStr = topMemberDTO.toString();

        // Assert
        Assert.assertEquals(topMemberDTO.toString(), topMemberDTOStr);
    }

    private void cleanUp() {
        transactionRepository.deleteById(transactionId);
        memberRepository.deleteById(memberId);
        bookRepository.deleteById(bookId);
    }
}
