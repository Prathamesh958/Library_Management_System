package com.lms.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookDTO {
    private Long bookId;
    @NotBlank(message = "Title cannot be empty")
    private String title;
    @NotBlank(message = "Author cannot be empty")
    private String author;
    @NotBlank(message = "ISBN is required")
    private String isbn;
    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
    @NotNull(message = "Category ID is required")
    private Long categoryId;
    private String categoryName;
}