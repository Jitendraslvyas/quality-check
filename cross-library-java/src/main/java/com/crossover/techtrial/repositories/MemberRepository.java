package com.crossover.techtrial.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.crossover.techtrial.model.Member;

/**
 * Member repository for basic operations on Member entity.
 * 
 * @author crossover
 */
@RepositoryRestResource(exported = false)
public interface MemberRepository extends PagingAndSortingRepository<Member, Long> {

    Optional <Member> findById(Long id);

    Optional <Member> findByEmail(String memberEmail);

    List <Member> findAll();
}
