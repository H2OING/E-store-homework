package com.example.demo.Service;

import com.example.demo.Model.Cart;
import com.example.demo.Model.Category;
import com.example.demo.Repository.Cart_Repository;
import com.example.demo.Repository.Category_Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class Category_Service {
    @Autowired
    Category_Repository categoryRepository;

    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }

    public Category getCategoryById(long id){
        Optional<Category> category = categoryRepository.findById(id);
        if(category.isPresent()){
            return category.get();
        } else{
            throw new EntityNotFoundException();
        }
    }

    public boolean createCategory(Category category){
        categoryRepository.save(category);
        return true;
    }

    public boolean updateCategory(Long id, Category category){
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if(optionalCategory.isPresent()){
            Category existingCategory = optionalCategory.get();
            existingCategory.setName(category.getName());
            existingCategory.setDescription(category.getDescription());
            existingCategory.setProducts(category.getProducts());
            categoryRepository.save(existingCategory);
            return true;
        } else{
            throw new EntityNotFoundException();
        }
    }

    public void deleteCategory(Long id){
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if(optionalCategory.isPresent()){
            categoryRepository.deleteById(id);
        } else{
            throw new EntityNotFoundException();
        }
    }
}
