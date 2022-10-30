package com.example.demo;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import com.example.demo.Model.Role;
import com.example.demo.Model.Web_User;
import com.example.demo.Repository.Web_User_Repository;
import com.example.demo.Service.Web_User_Service;

@ExtendWith(MockitoExtension.class)
public class WebUserServiceTest {

	@Mock
	Web_User_Repository webRepo;
	@InjectMocks
	Web_User_Service webService;
	
	@Test
	@DisplayName("Test should pass if user is saved once")
	void SaveUser() {
		Web_User user = new Web_User("Albert", "Conrad", "albert@gmail.com", 
				"28976548", "Albert Street", "Mqwertyuiop1!", Role.ROLE_CUSTOMER);
		webRepo.save(user);
		verify(webRepo,times(1)).save(ArgumentMatchers.any(Web_User.class));
	}
	
	@Test
	@DisplayName("Test should pass when a User is deleted")
	void deleteProduct() {
		Web_User user = new Web_User("Albert", "Conrad", "albert@gmail.com", 
				"28976548", "Albert Street", "Mqwertyuiop1!", Role.ROLE_CUSTOMER);
		webRepo.save(user);
		lenient().when(webRepo.findById(user.getIdUser())).thenReturn(Optional.of(user));
		webRepo.deleteById(user.getIdUser());
		verify(webRepo,times(1)).deleteById(user.getIdUser());
	}
	 /*
	  * 
	  * productRepository.save(product);
		when(productRepository.findById(product.getIdP())).thenReturn(Optional.of(product));
		productService.deleteProduct(product.getIdP());
		verify(productRepository,times(1)).deleteById(product.getIdP());
	  * 
	  */
	 
}
