package com.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.entities.Student;

public interface StudentRepository extends JpaRepository<Student , Long>
{
	Student findByRollNo(String rollNo);

}
