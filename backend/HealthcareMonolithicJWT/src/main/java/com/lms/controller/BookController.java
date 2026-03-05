package com.lms.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lms.dtos.BookDTO;
import com.lms.service.BookService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

	private final BookService bookService;

	@PostMapping
	public ResponseEntity<BookDTO> addBook(@Valid @RequestBody BookDTO bookDTO) {
		return ResponseEntity.status(HttpStatus.CREATED).body(bookService.addBook(bookDTO));
	}

	@GetMapping("/{id}")
	public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
		return ResponseEntity.ok(bookService.getBookById(id));
	}

	@GetMapping("/search/title")
	public ResponseEntity<List<BookDTO>> searchByTitle(@RequestParam String title) {
		return ResponseEntity.ok(bookService.searchBooksByTitle(title));
	}

	@GetMapping("/search/author")
	public ResponseEntity<List<BookDTO>> searchByAuthor(@RequestParam String author) {
		return ResponseEntity.ok(bookService.searchBooksByAuthor(author));
	}

	@GetMapping("/category/{categoryId}")
	public ResponseEntity<List<BookDTO>> getBooksByCategory(@PathVariable Long categoryId) {
		return ResponseEntity.ok(bookService.getBooksByCategoryId(categoryId));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteBook(@PathVariable Long id) {
		bookService.deleteBook(id);
		return ResponseEntity.ok("Book deleted successfully!");
	}
}