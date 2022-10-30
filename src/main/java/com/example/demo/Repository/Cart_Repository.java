package com.example.demo.Repository;

import com.example.demo.Model.Cart;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Cart_Repository extends JpaRepository<Cart, Long> {

	//Collection<Cart> findByProductIdP(long id);
}
