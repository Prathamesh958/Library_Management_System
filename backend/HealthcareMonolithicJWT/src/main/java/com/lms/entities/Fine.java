package com.lms.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="fines")
@Getter
@Setter
public class Fine 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "student_id" , nullable = false)
	private Student student;
	
	private double amount;
	
	@OneToOne
	@JoinColumn(name = "issue_id")
	private IssuedBook issuedBook;

	@Column(nullable = false)
	private boolean paid;
	
	private LocalDate fineDate = LocalDate.now();

}
