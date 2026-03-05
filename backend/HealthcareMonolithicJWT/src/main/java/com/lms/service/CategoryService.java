package com.lms.service;

import java.util.List;

import com.lms.dtos.CategoryDTO;

public interface CategoryService {
	CategoryDTO addCategory(CategoryDTO categoryDto);
    List<CategoryDTO> getAllCategories();

}
