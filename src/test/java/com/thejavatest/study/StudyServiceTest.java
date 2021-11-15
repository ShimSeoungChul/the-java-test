package com.thejavatest.study;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import com.thejavatest.domain.Member;
import com.thejavatest.domain.Study;
import com.thejavatest.member.MemberService;

@ExtendWith(MockitoExtension.class)
class StudyServiceTest {

	static MemberService memberService;
	static StudyRepository studyRepository;
	static Member member;
	static StudyService studyService;
	static Study study;

	@BeforeAll
	static void beforeAll(){
		member = new Member();
		member.setId(1L);
		member.setEmail("ted@email.com");
		study = new Study(10, "테스트");
		memberService = Mockito.mock(MemberService.class);
		studyRepository = Mockito.mock(StudyRepository.class);
		studyService = new StudyService(memberService, studyRepository);
	}

	@Test
	void createStudyService(){
		assertNotNull(studyService);

		when(memberService.findById(any()))
			.thenReturn(Optional.of(member))
			.thenThrow(new RuntimeException())
			.thenReturn(Optional.empty());

		Optional<Member> byId = memberService.findById(1L);
		assertEquals("ted@email.com",byId.get().getEmail());

		assertThrows(RuntimeException.class, ()->{
			memberService.findById(1L);
		});

		assertEquals(Optional.empty(), memberService.findById(3L));
	}

	@Test
	void when_thenReturn사용(){
		when(memberService.findById(1L)).thenReturn(Optional.of(member));
		assertEquals("ted@email.com",memberService.findById(1L).get().getEmail());
	}

	@Test
	void when_thenThrow사용(){
		when(memberService.findById(1L)).thenThrow(new RuntimeException());
		assertThrows(RuntimeException.class, ()->{
			memberService.findById(1L);
		});
	}

	@Test
	void doThrow사용(){
		doThrow(new IllegalArgumentException()).when(memberService).validate(1L);

		// validate() 메서드에 1L을 입력하면 예외 처리
		assertThrows(IllegalArgumentException.class, ()->{
			memberService.validate(1L);
		});

		// validate() 메서드에 1L 이외의 값을 입력하면 예외 처리 x
		memberService.validate(2L);
	}

	@Test
	void any사용(){
		when(memberService.findById(any())).thenReturn(Optional.of(member));

		assertEquals("ted@email.com",memberService.findById(1L).get().getEmail());
		assertEquals("ted@email.com",memberService.findById(999L).get().getEmail());
	}

	@Test
	void 메서드체이닝으로_여러번_호출되는_메서드의_행동_조작(){
		when(memberService.findById(any()))
			.thenReturn(Optional.of(member))
			.thenThrow(new RuntimeException())
			.thenReturn(Optional.empty());

		Optional<Member> byId = memberService.findById(1L);
		assertEquals("ted@email.com",byId.get().getEmail());

		assertThrows(RuntimeException.class, ()->{
			memberService.findById(1L);
		});

		assertEquals(Optional.empty(), memberService.findById(3L));
	}

	@Test
	void Study객체를_생성하면_notify_1번_호출(){
		when(memberService.findById(1L)).thenReturn(Optional.of(member));
		when(studyRepository.save(study)).thenReturn(study);

		studyService.createNewStudy(1L, study);

		// memberService에서 notify(study) 메서드가 한 번 호출 되었는지 확인
		verify(memberService, times(1)).notify(study);
		then(memberService).should(times(1)).notify(study);

		// 특정 메서드가 호출되지 않았는지 확인
		verify(memberService, never()).validate(any());
	}

	@Test
	void Study객체를_생성할때_validate는_호출_안됨(){
		when(memberService.findById(1L)).thenReturn(Optional.of(member));
		when(studyRepository.save(study)).thenReturn(study);

		studyService.createNewStudy(1L, study);

		// 특정 메서드가 호출되지 않았는지 확인
		verify(memberService, never()).validate(any());
	}

	@Test
	void Study객체를_생성할때_notify_study_이후에_notify_member가_호출(){

		when(memberService.findById(1L)).thenReturn(Optional.of(member));
		when(studyRepository.save(study)).thenReturn(study);

		studyService.createNewStudy(1L, study);

		// 특정 메서드가 어떤 순서로 호출 되었는지 확인
		InOrder inOrder = inOrder(memberService);
		inOrder.verify(memberService).notify(study);
		inOrder.verify(memberService).notify(member);
	}
}