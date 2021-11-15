package com.thejavatest.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

import org.springframework.data.annotation.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class Member {

	@Id
	@GeneratedValue
	private Long id;
	private String email;

}