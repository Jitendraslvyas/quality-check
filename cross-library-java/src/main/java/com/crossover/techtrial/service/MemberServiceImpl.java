package com.crossover.techtrial.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crossover.techtrial.model.Member;
import com.crossover.techtrial.repositories.MemberRepository;

/**
 * MemberServiceImpl is implementation of MemberService interface.
 * 
 * @author crossover
 */
@Service
public class MemberServiceImpl implements MemberService {

	@Autowired
	private MemberRepository memberRepository;

	@Override
	public Member save(Member member) {
		return memberRepository.save(member);
	}

	@Override
	public Member findById(Long memberId) {
		Optional<Member> optionalMember = memberRepository.findById(memberId);
		if (optionalMember.isPresent()) {
			return optionalMember.get();
		}
		return null;
	}

	@Override
	public Member findByEmail(String memberEmail) {
		Optional<Member> optionalMember = memberRepository.findByEmail(memberEmail);
		if (optionalMember.isPresent()) {
			return optionalMember.get();
		}
		return null;
	}

	@Override
	public List<Member> findAll() {
		return memberRepository.findAll();
	}
}
