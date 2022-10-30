package com.example.demo.Repository;

import com.example.demo.Model.Cart;
import com.example.demo.Model.Customer_Order;
import com.example.demo.Model.Order_Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Customer_Order_Repository extends JpaRepository<Customer_Order, Long> {
}
