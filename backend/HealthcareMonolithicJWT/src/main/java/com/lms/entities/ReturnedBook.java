package com.lms.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "returned_books")
@Getter
@Setter
public class ReturnedBook {
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long returnId;

	    @OneToOne
	    @JoinColumn(name = "issue_id" , unique = true)
	    private IssuedBook issuedBook;

	    private LocalDate returnDate;

	    private double fineAmount;

}
