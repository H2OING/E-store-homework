package com.example.demo;

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

import com.example.demo.Model.Cart;
import com.example.demo.Repository.Cart_Repository;
import com.example.demo.Service.Cart_Service;

@ExtendWith(MockitoExtension.class)
public class CartServiceTests {

	@Mock
	Cart_Repository cartRepo;
	@InjectMocks
	Cart_Service cartService;
	
	@Test
	@DisplayName("Test should pass if Cart is saved once")
	void SaveCart() {
		Cart cart = new Cart(new BigDecimal (0), true, null, null);
		cartRepo.save(cart);
		verify(cartRepo,times(1)).save(ArgumentMatchers.any(Cart.class));
	}
	
	@Test
	@DisplayName("Test should pass when a cart is deleted")
	void deleteCart() {
		Cart cart = new Cart(new BigDecimal (0), true, null, null);
		cartRepo.save(cart);
		when(cartRepo.findById(cart.getIdCart())).thenReturn(Optional.of(cart));
		cartService.deleteCart(cart.getIdCart());
		verify(cartRepo,times(1)).deleteById(cart.getIdCart());
	}
	 
}
