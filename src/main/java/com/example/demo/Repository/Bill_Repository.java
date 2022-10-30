package com.example.demo.Repository;

import com.example.demo.Model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Bill_Repository extends JpaRepository<Bill, Long> {
}
