package com.example.demo.Service;

import com.example.demo.Model.Cart;
import com.example.demo.Model.Product;
import com.example.demo.Model.Web_User;
import com.example.demo.Repository.Cart_Repository;
import com.example.demo.Repository.Product_Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class Cart_Service {
    @Autowired
    Cart_Repository cartRepository;
    @Autowired
    Product_Repository productRepository;

    public List<Cart> getAllCarts(){
        return cartRepository.findAll();
    }

    public Cart getCartById(long id){
        Optional<Cart> cart = cartRepository.findById(id);
        if(cart.isPresent()){
            return cart.get();
        } else{
            throw new EntityNotFoundException();
        }
    }

    public Cart createCart(Cart cart){
        return cartRepository.save(cart);
    }

    public Cart updateCart(Long id, Cart cart){
        Optional<Cart> optionalCart = cartRepository.findById(id);
        if(optionalCart.isPresent()){
            Cart existingCart = optionalCart.get();
            existingCart.setTotal(cart.getTotal());
            existingCart.setEmpty(cart.isEmpty());
            existingCart.setWebUser(cart.getWebUser());
            existingCart.setOrders(cart.getOrders());
            existingCart.setProducts(cart.getProducts());
            return cartRepository.save(existingCart);
        } else{
            throw new EntityNotFoundException();
        }
    }

    public void deleteCart(Long id){
        Optional<Cart> optionalCart = cartRepository.findById(id);
        if(optionalCart.isPresent()){
            cartRepository.deleteById(id);
        } else{
            throw new EntityNotFoundException();
        }
    }

    public void addToCart(Long productId, Web_User loggedInUser) {
        Product product = productRepository.findById(productId).get();
        Cart cart = loggedInUser.getCart();
        cart.addProduct(product);
        cart.setTotal(product.getPrice().add(cart.getTotal()));
        product.addCart(cart);
        cartRepository.save(cart);
        productRepository.save(product);
    }

    public void removeFromCart(Long productId, Web_User loggedInUser) {
        Product product = productRepository.findById(productId).get();
        Cart cart = loggedInUser.getCart();
        cart.removeProduct(product);
        cart.setTotal(cart.getTotal().subtract(product.getPrice()));
        product.removeCart(cart);
        cartRepository.save(cart);
        productRepository.save(product);
    }
}
