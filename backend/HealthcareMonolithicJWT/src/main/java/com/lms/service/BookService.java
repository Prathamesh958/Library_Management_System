package com.lms.service;

import java.util.List;

import com.lms.dtos.BookDTO;



public interface BookService {
    BookDTO addBook(BookDTO bookDto);
    BookDTO getBookById(Long id);
    List<BookDTO> searchBooksByTitle(String title);
    List<BookDTO> searchBooksByAuthor(String author);
    List<BookDTO> getBooksByCategoryId(Long categoryId);
    void deleteBook(Long id);
}