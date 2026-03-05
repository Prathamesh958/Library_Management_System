package com.lms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.entities.Book;
import com.lms.entities.Category;

public interface BookRepository extends JpaRepository<Book,Long> {
	List<Book> findByTitleContainingIgnoreCase(String title);
	List<Book> findByAuthorContainingIgnoreCase(String author);
	List<Book> findByCategory(Category category);


}
