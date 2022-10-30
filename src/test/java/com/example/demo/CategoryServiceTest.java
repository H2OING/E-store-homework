package com.example.demo;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.Model.Category;
import com.example.demo.Repository.Category_Repository;
import com.example.demo.Service.Category_Service;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

	@Mock
	Category_Repository catRepo;
	@InjectMocks
	Category_Service catService;
	
	
	@Test
	@DisplayName("Test should pass if Category is saved once")
	void SaveCategory() {
		Category category = new Category("Wood", "Spoon");
		
		catRepo.save(category);
		verify(catRepo,times(1)).save(ArgumentMatchers.any(Category.class));
	}
	
	@Test
	@DisplayName("Test should pass when a category is deleted")
	void deleteCategory() {
		Category category = new Category("Wood", "Spoon");
		catRepo.save(category);
		when(catRepo.findById(category.getIdCat())).thenReturn(Optional.of(category));
		catService.deleteCategory(category.getIdCat());
		verify(catRepo,times(1)).deleteById(category.getIdCat());
	}
	 
}
