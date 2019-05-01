package com.crossover.techtrial.service;

import java.util.List;

import com.crossover.techtrial.model.Member;

/**
 * MemberService interface for Member.
 * 
 * @author crossover
 */
public interface MemberService {

    public Member save(Member member);

    public Member findById(Long memberId);

    public Member findByEmail(String memberEmail);

    public List <Member> findAll();
}
