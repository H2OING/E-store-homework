package com.example.demo;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.Model.Bill;
import com.example.demo.Model.Payment_Method;
import com.example.demo.Repository.Bill_Repository;
import com.example.demo.Service.Bill_Service;

@ExtendWith(MockitoExtension.class)
public class BillServiceTest {
	
	@Mock
	Bill_Repository billRepo;
	@InjectMocks
	Bill_Service billService;
	
	
	@Test
	@DisplayName("Test should pass if Cart is saved once")
	void SaveCart() {
		Bill bill = new Bill(Payment_Method.BANK_TRANSFER, new Date(), 345678, null);
		billRepo.save(bill);
		verify(billRepo,times(1)).save(ArgumentMatchers.any(Bill.class));
	}
	
	@Test
	@DisplayName("Test should pass when a cart is deleted")
	void deleteCart() {
		Bill bill = new Bill(Payment_Method.BANK_TRANSFER, new Date(), 345678, null);
		billRepo.save(bill);
		when(billRepo.findById(bill.getIdBi())).thenReturn(Optional.of(bill));
		billService.deleteBill(bill.getIdBi());
		verify(billRepo,times(1)).deleteById(bill.getIdBi());
	}
	 

}
