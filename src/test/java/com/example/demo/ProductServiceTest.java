package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hamcrest.MatcherAssert;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.Rollback;

import com.example.demo.Model.Category;
import com.example.demo.Model.Product;
import com.example.demo.Repository.Product_Repository;
import com.example.demo.Service.Product_Service;

import aj.org.objectweb.asm.Type;

import javax.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

	@Mock
	Product_Repository productRepository;
	@InjectMocks
	Product_Service productService;
	
	@Test
	@DisplayName("Test should pass when the products name is equal to the given product.getName() and category is 'Electronics'")
	void addedProductTest() {
	
		Category category = new Category("Electronics", "Phone tech");
		Product product = new Product("Camera", "New", 4, new BigDecimal(270), null, category);
		
		assertThat(product.getName()).isEqualTo("Camera");
		assertThat(product.getCategory().getName()).isEqualTo("Electronics");
	}
	@Test
	@DisplayName("Test should pass when a created product is saved in repo once")
	void addedProductRepoTest() {
		Category category = new Category("Electronics", "Phone tech");
		Product product = new Product("Camera", "New", 4, new BigDecimal(270), null, category);
		productRepository.save(product);
		verify(productRepository,times(1)).save(ArgumentMatchers.any(Product.class));
	}
	@Test
	@DisplayName("Test should pass when a created product size is 1")
	void getProductRepoObjectsSize() {
		Category category = new Category("Food", "tasty bread");
		when(productRepository.findAll()).thenReturn(Stream.of(
				new Product("White bread", "New", 4, new BigDecimal(270), null, category))
				.collect(Collectors.toList()));
		assertEquals(1, productService.getAllProducts().size());
	}
	@Test
	@DisplayName("Test should pass when an entity is not found in database")
	void getProductByIdServiceNotFound(){
		doReturn(Optional.empty()).when(productRepository).findById(1l);
		assertThrows(EntityNotFoundException.class, () -> productService.getProductById(1l));
	}
	@Test
	@DisplayName("Test should pass when a product is deleted")
	void deleteProduct() {
		Category category = new Category("Electronics", "Phone tech");
		Product product = new Product("Camera", "New", 4, new BigDecimal(270), null, category);
		productRepository.save(product);
		when(productRepository.findById(product.getIdP())).thenReturn(Optional.of(product));
		productService.deleteProduct(product.getIdP());
		verify(productRepository,times(1)).deleteById(product.getIdP());
	}

	
	@Test
	@Rollback(value = false)
	@DisplayName("Test should pass when a product is updated")
	void updateProduct() {
		Category category = new Category("Electronics", "Phone tech");
		Product product = new Product("Camera", "New", 4, new BigDecimal(270), null, category);
		//product.setName("Fridge");
		productRepository.save(product);
		product.setName("Fridge");
		productService.updateProduct(product.getIdP(), product);
		Product updatedProduct = productRepository.findById(product.getIdP()).get();
		//System.out.println(productR);
		assertThat(updatedProduct.getName()).isEqualTo("Fridge");
		//assertEquals("Fridge",updatedProduct.getName());
	}

	@Test
	@DisplayName("Test should pass when an entity is not found in database")
	void deleteProductNotFound(){
		Category category = new Category("Electronics", "Phone tech");
		Product product = new Product("Camera", "New", 4, new BigDecimal(270), null, category);
		assertThrows(EntityNotFoundException.class, () -> productService.deleteProduct(product.getIdP()));

	}
}
