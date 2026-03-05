package com.lms.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lms.dtos.CategoryDTO;
import com.lms.entities.Category;
import com.lms.repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {
	@Autowired private CategoryRepository categoryRepo;
    @Autowired private ModelMapper modelMapper;

	@Override
	public CategoryDTO addCategory(CategoryDTO categoryDto) {
		Category category = modelMapper.map(categoryDto, Category.class);
        return modelMapper.map(categoryRepo.save(category), CategoryDTO.class);
	}

	@Override
	public List<CategoryDTO> getAllCategories() {
		return categoryRepo.findAll().stream()
                .map(cat -> modelMapper.map(cat, CategoryDTO.class)).collect(Collectors.toList());
	}

}
