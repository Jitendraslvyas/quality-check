package com.crossover.techtrial.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.crossover.techtrial.model.Transaction;

/**
 * Transaction repository for basic operations on Transaction entity.
 * 
 * @author crossover
 */
@RepositoryRestResource(exported = false)
public interface TransactionRepository extends CrudRepository<Transaction, Long> {

    Long countByMemberIdAndDateOfReturnIsNull(Long memberId);

    Transaction findByBookIdAndDateOfReturnIsNull(Long bookId);
    
	@Query(value = "SELECT member.id AS memberId, member.name, member.email, Count(transaction.member_id) AS bookCount FROM member INNER JOIN transaction ON member.id = transaction.member_id WHERE transaction.date_of_issue >= :startTime AND transaction.date_of_return <= :endTime GROUP BY transaction.member_id ORDER BY bookCount DESC LIMIT 5", nativeQuery = true)
	List<Object[]> getTopMembers(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}
