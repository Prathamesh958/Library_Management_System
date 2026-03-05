package com.lms.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryDTO {

  private Long categoryId;
  @NotBlank(message = "Category name cannot be blank!!")
  private String categoryName;
}