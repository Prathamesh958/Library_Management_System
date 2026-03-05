package com.lms.service;

import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lms.custom_exceptions.ResourceNotFoundException;
import com.lms.custom_exceptions.APIException;
import com.lms.dtos.BookDTO;
import com.lms.entities.Book;
import com.lms.entities.Category;
import com.lms.entities.IssuedBook;
import com.lms.entities.Status;
import com.lms.repository.BookRepository;
import com.lms.repository.CategoryRepository;
import com.lms.repository.IssueRepository;

import jakarta.transaction.Transactional;

@Service
public class BookServiceImpl implements BookService {
    @Autowired private BookRepository bookRepo;
    @Autowired private CategoryRepository categoryRepo;
    @Autowired private IssueRepository issueRepo;
    @Autowired private ModelMapper modelMapper;

    @Override
    @Transactional
    public BookDTO addBook(BookDTO bookDto) {
        // 1. DTO se Entity map karo
        Book book = modelMapper.map(bookDto, Book.class);
        
        // 2. ASLI FIX: ID ko null set karo taaki Hibernate hamesha INSERT kare
        // Iske bina agar ModelMapper ne purani ID uthayi, toh update hone lagega
        book.setBookId(null); 
        
        // 3. Category verify aur link karo
        Category category = categoryRepo.findById(bookDto.getCategoryId())
            .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + bookDto.getCategoryId()));
        
        book.setCategory(category); 
        
        // 4. Save record
        Book savedBook = bookRepo.save(book);
        
        // 5. Response mapping (Manual fields for safety)
        BookDTO responseDto = modelMapper.map(savedBook, BookDTO.class);
        responseDto.setCategoryId(savedBook.getCategory().getCategoryId());
        responseDto.setCategoryName(savedBook.getCategory().getCategoryName());
        
        return responseDto;
    }

    @Override
    @Transactional
    public void deleteBook(Long id) {
        Book book = bookRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + id));
        
        
        List<IssuedBook> history = issueRepo.findByBook_BookId(id);
        boolean isCurrentlyIssued = history.stream()
                .anyMatch(i -> i.getStatus().equals(Status.ACTIVE));
        
        if (isCurrentlyIssued) {
            throw new APIException("Book is currently active!!! , return that book first");
        }
       
        if (!history.isEmpty()) {
            issueRepo.deleteAll(history); 
        }

       
        bookRepo.delete(book);
    }

    
    @Override public BookDTO getBookById(Long id) { Book book = bookRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book not found")); BookDTO dto = modelMapper.map(book, BookDTO.class); dto.setCategoryId(book.getCategory().getCategoryId()); dto.setCategoryName(book.getCategory().getCategoryName()); return dto; }
    @Override public List<BookDTO> searchBooksByTitle(String title) { return bookRepo.findByTitleContainingIgnoreCase(title).stream().map(b -> modelMapper.map(b, BookDTO.class)).collect(Collectors.toList()); }
    @Override public List<BookDTO> searchBooksByAuthor(String author) { return bookRepo.findByAuthorContainingIgnoreCase(author).stream().map(b -> modelMapper.map(b, BookDTO.class)).collect(Collectors.toList()); }
    @Override public List<BookDTO> getBooksByCategoryId(Long categoryId) { Category cat = categoryRepo.findById(categoryId).orElseThrow(); return bookRepo.findByCategory(cat).stream().map(b -> modelMapper.map(b, BookDTO.class)).collect(Collectors.toList()); }
}