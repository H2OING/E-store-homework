package com.example.demo;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.Model.Cart;
import com.example.demo.Model.Customer_Order;
import com.example.demo.Model.Order_Status;
import com.example.demo.Model.Product;
import com.example.demo.Model.Role;
import com.example.demo.Model.Web_User;
import com.example.demo.Repository.Customer_Order_Repository;


@ExtendWith(MockitoExtension.class)
public class CustomerOrderService {
	
	@Mock
	Customer_Order_Repository custRepo;
	@InjectMocks
	CustomerOrderService custService;
	
	@Test
	@DisplayName("Test should pass if Custoomer order is saved once")
	void SaveCustomerOrder() {
		Product prod = new Product("Camera", "New", 4, new BigDecimal(270), null, null);
		Customer_Order order = new Customer_Order(Order_Status.NEW, new Date(), new Date(), 
				null, new Cart(new BigDecimal (70), false, 
				new Web_User("Arnold", "Desk", "Desk@gmail.com", "28989898", "Pumpkin street", "Kasdfghjkl!1", Role.ROLE_CUSTOMER),
				new ArrayList<>(Arrays.asList(prod))));
		custRepo.save(order);
		verify(custRepo,times(1)).save(ArgumentMatchers.any(Customer_Order.class));
	}
	
	@Test
	@DisplayName("Test should pass when a Customer Order is deleted ")
	void deleteCustomerOrder() {
		Product prod = new Product("Camera", "New", 4, new BigDecimal(270), null, null);
		Customer_Order order = new Customer_Order(Order_Status.NEW, new Date(), new Date(), 
				null, new Cart(new BigDecimal (70), false, 
				new Web_User("Arnold", "Desk", "Desk@gmail.com", "28989898", "Pumpkin street", "Kasdfghjkl!1", Role.ROLE_CUSTOMER),
				new ArrayList<>(Arrays.asList(prod))));
		custRepo.save(order);
		lenient().when(custRepo.findById(order.getIdCO())).thenReturn(Optional.of(order));
		custRepo.deleteById(order.getIdCO());
		verify(custRepo,times(1)).deleteById(order.getIdCO());
	}
	 
	
	

}
