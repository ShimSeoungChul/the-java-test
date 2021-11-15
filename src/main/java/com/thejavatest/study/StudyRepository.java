package com.thejavatest.study;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thejavatest.domain.Study;

public interface StudyRepository extends JpaRepository<Study, Long> {
}
