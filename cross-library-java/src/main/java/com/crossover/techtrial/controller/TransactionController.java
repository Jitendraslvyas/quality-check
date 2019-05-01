package com.crossover.techtrial.controller;

import java.time.LocalDateTime;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.crossover.techtrial.model.Book;
import com.crossover.techtrial.model.Member;
import com.crossover.techtrial.model.Transaction;
import com.crossover.techtrial.repositories.TransactionRepository;
import com.crossover.techtrial.service.BookService;
import com.crossover.techtrial.service.MemberService;

/**
 * TransactionController for Transaction related APIs.
 * 
 * @author crossover
 */
@RestController
public class TransactionController {
    private static final Logger LOG = LoggerFactory.getLogger(TransactionController.class);
    private static final Long MAX_BOOK_CAN_BE_ISSUE = 5L;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private BookService bookService;

    /**
     * This method is used to save a transaction details i.e. particular book that
     * identify by book-id issue to respective member who identify by member-id.
     * 
     * @param params request parameter as { "bookId":1,"memberId":33 }.
     * @return Transaction Details if pass all validation & save successfully.
     */
    @PostMapping(path = "/api/transaction")
    public ResponseEntity<Transaction> issueBookToMember(@RequestBody Map<String, Long> params) {

        Long bookId = params.get("bookId");
        Long memberId = params.get("memberId");

        Member member = memberService.findById(memberId);
        if (member != null) {

            Book book = bookService.findById(bookId);
            if (book != null) {

                Long notReturnBookCountForMember = transactionRepository.countByMemberIdAndDateOfReturnIsNull(memberId);
                if (notReturnBookCountForMember < MAX_BOOK_CAN_BE_ISSUE) {

                    Transaction bookTransaction = transactionRepository.findByBookIdAndDateOfReturnIsNull(bookId);
                    if (bookTransaction == null) {
                        Transaction transaction = new Transaction();
                        transaction.setMember(member);
                        transaction.setBook(book);
                        transaction.setDateOfIssue(LocalDateTime.now());
                        LOG.info("Provided Transaction details: {} save successfully", transaction);
                        return ResponseEntity.ok().body(transactionRepository.save(transaction));
                    } else {
                        LOG.info("Provided book-id: {} already issued to member-id: {}", bookId,
                            bookTransaction.getMember().getId());
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                    }
                } else {
                    LOG.info(
                        "memberId: {} already isuued with {} books that not return yet, So can't issue more than {}",
                        memberId, notReturnBookCountForMember, MAX_BOOK_CAN_BE_ISSUE);
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                }
            } else {
                LOG.info("Provided book-id: {} not found", bookId);
                return ResponseEntity.notFound().build();
            }
        } else {
            LOG.info("Provided member-id: {} not found", memberId);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * This method is used to update 'dateOfReturn' by transactionId, If
     * transactionId is present & it's 'dateOfReturn' is not yet updated.
     * 
     * @param transactionId transaction's Id.
     * @return Transaction Details with updated 'dateOfReturn'.
     */
    @PatchMapping(path = "/api/transaction/{transaction-id}/return")
    public ResponseEntity<Transaction> returnBookTransaction(
        @PathVariable(name = "transaction-id") Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId).orElse(null);

        if (transaction == null) {
            LOG.info("Provided transaction-id: {} not found", transactionId);
            return ResponseEntity.notFound().build();
        } else if (transaction.getDateOfReturn() != null) {
            LOG.info("Book's dateOfReturn already updated for transaction-id: {}", transactionId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } else {
            transaction.setDateOfReturn(LocalDateTime.now());
            LOG.info("Book's dateOfReturn updated successfully for transaction-id: {}", transactionId);
            return ResponseEntity.ok().body(transactionRepository.save(transaction));
        }
    }
}
