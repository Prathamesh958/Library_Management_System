package com.lms.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.lms.entities.Book;
import com.lms.entities.Category;
import com.lms.entities.Librarian;
import com.lms.entities.Role;
import com.lms.entities.Student;
import com.lms.entities.User;
import com.lms.repository.BookRepository;
import com.lms.repository.CategoryRepository;
import com.lms.repository.LibrarianRepository;
import com.lms.repository.StudentRepository;
import com.lms.repository.UserRepository;

import com.lms.entities.Status;
import jakarta.transaction.Transactional;

@Configuration
public class DataInitializer {

    @Bean
    @Transactional
    public CommandLineRunner initData(UserRepository userRepo, 
                                     LibrarianRepository libRepo, 
                                     StudentRepository stuRepo,
                                     CategoryRepository catRepo,
                                     BookRepository bookRepo,
                                     PasswordEncoder encoder) {
        return args -> {
            
            // 1. Check for specific categories instead of count()
            // Isse agar Biography pehle se hai, toh code crash nahi hoga
            Category csCat = catRepo.findByCategoryName("Computer Science")
                .orElseGet(() -> {
                    Category c = new Category();
                    c.setCategoryName("Computer Science");
                    return catRepo.save(c);
                });

            // Agar tujhe Biography wali book add karni hai, toh usey yahan fetch karo
            Category bioCat = catRepo.findByCategoryName("Biography").orElse(null);

            // 2. BOOKS: Agar table mein ye specific book nahi hai, toh hi dalo
            if (bookRepo.count() == 0) {
                Book b1 = new Book();
                b1.setTitle("Clean Code");
                b1.setAuthor("Robert C. Martin");
                b1.setIsbn("ISBN-001");
                b1.setQuantity(5);
                b1.setCategory(csCat);
                bookRepo.save(b1);
                System.out.println(">>> Sample Books Added!");
            }

            // 3. LIBRARIAN (admin@test.com)
            if (!userRepo.existsByEmail("admin@test.com")) {
                User user1 = new User();
                user1.setName("Librarian Admin");
                user1.setEmail("admin@test.com");
                user1.setPassword(encoder.encode("admin123"));
                user1.setRole(Role.LIBRARIAN);
                user1.setStatus(Status.ACTIVE);
                User savedLibUser = userRepo.save(user1);

                Librarian lib = new Librarian();
                lib.setUser(savedLibUser);
                lib.setEmployeeId("EMP001");
                libRepo.save(lib);
                System.out.println(">>> Librarian Created!");
            }

            // 4. STUDENT (rahul@test.com)
            if (!userRepo.existsByEmail("rahul@test.com")) {
                User user2 = new User();
                user2.setName("Rahul Kumar");
                user2.setEmail("rahul@test.com");
                user2.setPassword(encoder.encode("rahul123"));
                user2.setRole(Role.STUDENT);
                user2.setStatus(Status.ACTIVE);
                User savedStuUser = userRepo.save(user2);

                Student stu = new Student();
                stu.setUser(savedStuUser);
                stu.setRollNo("ROLL101");
                stu.setCourse("DAC");
                stu.setYear(2025);
                stuRepo.save(stu);
                System.out.println(">>> Student Created!");
            }
        };
    }
}