package com.thejavatest.member;

import java.util.Optional;

import com.thejavatest.domain.Member;
import com.thejavatest.domain.Study;

public interface MemberService {
	Optional<Member> findById(Long memberId);
	void validate(Long memberId);
	void notify(Study newstudy);
	void notify(Member member);
}
