package com.lms.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "librarians")
@Getter
@Setter
public class Librarian 
{
	@Id
	private Long userId;
	
	@OneToOne
	@MapsId
	@JoinColumn(name = "user_id")
	private User user;
	
	private String employeeId;

}
