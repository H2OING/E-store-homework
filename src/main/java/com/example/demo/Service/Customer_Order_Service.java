package com.example.demo.Service;

import com.example.demo.Model.Cart;
import com.example.demo.Model.Customer_Order;
import com.example.demo.Repository.Customer_Order_Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class Customer_Order_Service {
    @Autowired
    Customer_Order_Repository customerOrderRepository;

    public List<Customer_Order> getAllCustomerOrders(){
        return customerOrderRepository.findAll();
    }

    public Customer_Order getCustomerOrderById(long id){
        Optional<Customer_Order> customerOrder = customerOrderRepository.findById(id);
        if(customerOrder.isPresent()){
            return customerOrder.get();
        } else{
            throw new EntityNotFoundException();
        }
    }

    public Customer_Order createCustomerOrder(Customer_Order customerOrder){
        return customerOrderRepository.save(customerOrder);
    }

    public Customer_Order updateCustomerOrder(Long id, Customer_Order customerOrder){
        Optional<Customer_Order> optionalCustomerOrder = customerOrderRepository.findById(id);
        if(optionalCustomerOrder.isPresent()){
            Customer_Order existingCustomerOrder = optionalCustomerOrder.get();
            existingCustomerOrder.setStatus(customerOrder.getStatus());
            existingCustomerOrder.setOrderedDate(customerOrder.getOrderedDate());
            existingCustomerOrder.setShippedDate(customerOrder.getShippedDate());
            existingCustomerOrder.setOrderedProducts(customerOrder.getOrderedProducts());
            existingCustomerOrder.setCart(customerOrder.getCart());
            existingCustomerOrder.setBill(customerOrder.getBill());
            return customerOrderRepository.save(existingCustomerOrder);
        } else{
            throw new EntityNotFoundException();
        }
    }

    public void deleteCustomerOrder(Long id){
        Optional<Customer_Order> optionalCustomerOrder = customerOrderRepository.findById(id);
        if(optionalCustomerOrder.isPresent()){
            customerOrderRepository.deleteById(id);
        } else{
            throw new EntityNotFoundException();
        }
    }
}
